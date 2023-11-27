package org.example;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        LSB.encodeFile(new File("images/greenland.bmp"),
                new File("images/lena.bmp"),
                new File("images/result.bmp"));
    }
}