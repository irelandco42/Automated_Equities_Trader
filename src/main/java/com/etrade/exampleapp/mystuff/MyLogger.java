package com.etrade.exampleapp.mystuff;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class MyLogger {

    PrintWriter out;

    public MyLogger(String filepath) {
        try {
            out = new PrintWriter(filepath);
        } catch (FileNotFoundException e) {

        }
    }

    public void logString(String string) {
        out.println(string);
    }

    public void close() {
        out.close();
    }
}
