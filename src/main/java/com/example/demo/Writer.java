package com.example.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Writer{
    private PrintWriter pw ;
    public Writer(File file) throws FileNotFoundException {
        pw = new PrintWriter(file);
    }
    public void setNewStr(String str){
        pw.println(str);
    }
    public void setNewInt(int num){
        pw.println(num);
    }
    public void close(){
        pw.close();
    }
}
