package singlethread;

import fir.FIR;
import generator.Generator;

import java.util.Arrays;

public class Main {

    private static final int size = 100_000;

    public static void main(String[] args) {

        double in[] = Generator.generate(size);

        //Фильтрация входных данных
        long startTime = System.nanoTime();
        double[] result = FIR.filtration(in);
        System.out.println("execution time: " + (System.nanoTime() - startTime));
        System.out.println("result:\r\n" + Arrays.toString(result));
    }

}

