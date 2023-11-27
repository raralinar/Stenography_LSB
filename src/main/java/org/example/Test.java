package org.example;

import java.util.Optional;

public class Test {
//    interface I {
//        String generate();
//        default void print(String value) {
//            System.out.println(Optional.ofNullable(value).orElseGet(this::generate));
//        }
//    }
    interface I { void print(); }
    public I create() {
        return () -> {
            System.out. println( "Hello!"); };
    }
    private I i = this:: create;
    public void test() {
        i.print ( );
    }


    public static void main(String[] args) {
//        ((I) () -> "Hello!").print(null);
//        float num = 5.0f;
        int num = 0;
        for (int i = 0; i < 100; i++)
            num = num++;
        System.out.println(num);
        float real = 0.0f/0.0f;
        if (real == real)
            System.out.println("Equal");
        else
            System.out.println("Not equal");

        byte num1 = (byte)127;
        num1++;
        System.out.println(num1);

    }

//    public static void main(String[] arr)
//    {
//        System.out.println("Main.main(String[] arr) is called");
//    }
//    public static void main()
//    {
//        System.out.println("Main.main() is called");
//    }


}
