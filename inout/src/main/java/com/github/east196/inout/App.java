package com.github.east196.inout;

public class App {
    public static void main(String[] args) {
        String code = null;
        In in = null;
        InHandler inHandler=InHandlers.get(code);
        Out out=inHandler.handle(in);
        System.out.println(out);
    }
}
