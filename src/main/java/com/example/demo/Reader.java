package com.example.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Reader {
    private Scanner scanner ;
    private boolean flag = false;
    private String currentString = null;
    private final int CLEAN_COUNTER = 10_000;
    public String getAndClearCurrentString() {
        String str = currentString;
        currentString = null;
        return  str;
    }


    public Reader(File file) throws FileNotFoundException, InterruptedException {
        scanner = new Scanner(file);
        int count = 0;
        if(scanner.hasNextLine()) {
            flag = true;
        }
        if (CLEAN_COUNTER<=count) {
            System.gc();
            count=0;
        }
        count++;

    }
    public void stepNextLine(){
        if(scanner.hasNextLine()){
            currentString = scanner.nextLine();
            flag = true;
        }else{
            flag = false;
        }
    }

    public boolean getFlag(){
        return flag;
    }
    public void stopScanner(){
        scanner.close();
    }
}
