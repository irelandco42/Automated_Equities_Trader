package com.etrade.exampleapp.mystuff.outs;

public class SystemPrinter extends OutObject {

    public SystemPrinter() {

    }

    @Override
    public void print(String message) {
        System.out.println(message);
    }
}
