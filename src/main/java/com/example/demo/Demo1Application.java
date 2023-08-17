package com.example.demo;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Demo1Application {
    private static Order order = Order.ASC;
    private static Reader reader;
    private static Writer writer;
    public static void main(String[] args) {

        Type type = null;
        List<String> listPath = new ArrayList<>();
        List<String> listValue = new ArrayList<>();
        for(String arg: args){
            if(arg.trim().equals("-d")){
                order = Order.DESC;
            }
            if(arg.trim().equals("-s")){
                type = Type.STR;
            }
            if(arg.trim().equals("-i")){
                type = Type.INT;
            }
            if(arg.trim().contains(".txt") && isFileByPathExists(arg)){
                listPath.add(arg);
            }
        }

        if(listPath.size() <2){
            System.console().readLine("проверьте количество файлов");
            return;
        }
        if(type == null){
            System.console().readLine("тип не задан");
            return;
        }

        File fOut = new File(listPath.get(0));
        try{
            reader = new Reader(new File(listPath.get(1)));

            for(int i=1; i<listPath.size();i++){
                reader = new Reader(new File(listPath.get(i)));
                while(reader.getFlag()){
                    reader.stepNextLine();
                    String str = reader.getAndClearCurrentString();
                    if(str != null&& !str.equals("")&&!str.trim().contains(" ")){
                        listValue.add(str);
                    }
                }
            }
            reader.stopScanner();
        }catch( FileNotFoundException | InterruptedException e){
            System.console().readLine("Невозможно прочитать файлы");
        }
        try{
            writer = new Writer(fOut);
            if(type==Type.INT){
                int[] arr  = listValue
                        .stream()
                        .map(Integer::valueOf)
                        .mapToInt(Integer::intValue)
                        .toArray();
                int[] sortArrInt = mergeSortInt(arr);
                Arrays.stream(sortArrInt).forEachOrdered(writer::setNewInt);
            }else{
                String[] arr = listValue.toArray(new String[0]);
                String[] sortArr = mergeSortString(arr);
                Arrays.stream(sortArr).forEachOrdered(writer::setNewStr);
            }
            writer.close();
        }catch( FileNotFoundException e){
            System.console().readLine("Невозможно записать данные в файл");
        }catch(NumberFormatException e){
            System.console().readLine("Проверьте формат введенных данных");
        }
    }

    public static boolean isFileByPathExists(String path) {
        File file = new File(path);
        return file.exists() && !file.isDirectory();
    }

    public static String[] mergeSortString(String[] sortArr) {
        String[] buffer1 = Arrays.copyOf(sortArr, sortArr.length);
        String[] buffer2 = new String[sortArr.length];
        String[] result = mergeSortInnerString(buffer1, buffer2, 0, sortArr.length);
        return result;
    }
    public static String[] mergeSortInnerString(String[] buffer1, String[] buffer2, int startIndex, int endIndex) {
        if (startIndex >= endIndex - 1) {
            return buffer1;
        }

        //уже отсортирован
        int middle = startIndex + (endIndex - startIndex) / 2;
        String[] sorted1 = mergeSortInnerString(buffer1, buffer2, startIndex, middle);
        String[] sorted2 = mergeSortInnerString(buffer1, buffer2, middle, endIndex);

        //слияние
        int index1 = startIndex;
        int index2 = middle;
        int destIndex = startIndex;
        String[] result = sorted1 == buffer1 ? buffer2 : buffer1;
        while (index1 < middle && index2 < endIndex) {
            if(order==Order.DESC) {
                result[destIndex++] = sorted1[index1].compareToIgnoreCase(sorted2[index2]) < 0 //sorted1[index1] < sorted2[index2]
                        ? sorted2[index2++] :   sorted1[index1++];
            }else{
                result[destIndex++] = sorted1[index1].compareToIgnoreCase(sorted2[index2]) < 0 //sorted1[index1] < sorted2[index2]
                        ? sorted1[index1++] : sorted2[index2++];
            }
        }
        while (index1 < middle) {
            result[destIndex++] = sorted1[index1++];
        }
        while (index2 < endIndex) {
            result[destIndex++] = sorted2[index2++];
        }
        return result;
    }
    public static int[] mergeSortInt(int[] sortArr) {
        int[] buffer1 = Arrays.copyOf(sortArr, sortArr.length);
        int[] buffer2 = new int[sortArr.length];
        int[] result = mergeSortInnerInt(buffer1, buffer2, 0, sortArr.length);
        return result;
    }
    public static int[] mergeSortInnerInt(int[] buffer1, int[] buffer2, int startIndex, int endIndex) {
        if (startIndex >= endIndex - 1) {
            return buffer1;
        }

        //уже отсортирован
        int middle = startIndex + (endIndex - startIndex) / 2;
        int[] sorted1 = mergeSortInnerInt(buffer1, buffer2, startIndex, middle);
        int[] sorted2 = mergeSortInnerInt(buffer1, buffer2, middle, endIndex);

        //слияние
        int index1 = startIndex;
        int index2 = middle;
        int destIndex = startIndex;
        int[] result = sorted1 == buffer1 ? buffer2 : buffer1;
        while (index1 < middle && index2 < endIndex) {
            if(order==Order.DESC) {
                result[destIndex++] = sorted1[index1] < sorted2[index2]
                        ? sorted2[index2++] : sorted1[index1++];
            }else{
                result[destIndex++] = sorted1[index1] < sorted2[index2]
                        ? sorted1[index1++] : sorted2[index2++];
            }
        }
        while (index1 < middle) {
            result[destIndex++] = sorted1[index1++];
        }
        while (index2 < endIndex) {
            result[destIndex++] = sorted2[index2++];
        }
        return result;
    }
}
