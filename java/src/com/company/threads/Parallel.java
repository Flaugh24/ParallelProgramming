package com.company.threads;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import static java.lang.Math.PI;
import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.sin;

public class Parallel {

    public static final int N = 100; //Длина фильтра
    private static final int size = 10_000_000;
    public static double H[] = new double[N]; //Импульсная характеристика фильтра


    public static void main(String[] args) throws ExecutionException, InterruptedException {

        double[] in = generateIn();

        final double Fd = 2000; //Частота дискретизации входных данных
        final double Fs = 20; //Частота полосы пропускания
        final double Fx = 50; //Частота полосы затухания

        double H_id[] = new double[N]; //Идеальная импульсная характеристика
        double W[] = new double[N]; //Весовая функция

        //Расчет импульсной характеристики фильтра
        double Fc = (Fs + Fx) / (2 * Fd);

        for (int i = 0; i < N; i++) {
            if (i == 0) H_id[i] = 2 * PI * Fc;
            else H_id[i] = sin(2 * PI * Fc * i) / (PI * i);
            // весовая функция Блекмена
            W[i] = 0.42 - 0.5 * cos((2 * PI * i) / (N - 1)) + 0.08 * cos((4 * PI * i) / (N - 1));
            H[i] = H_id[i] * W[i];
        }

        //Нормировка импульсной характеристики
        double SUM = 0;
        for (int i = 0; i < N; i++) SUM += H[i];
        for (int i = 0; i < N; i++) H[i] /= SUM; //сумма коэффициентов равна 1

        for (int k = 0; k < 10; k++) {
            List<Future<double[]>> workerFutures = new ArrayList<>();
            ExecutorService executor = Executors.newFixedThreadPool(4);

            long start = System.nanoTime();

            for (int j = 0; j < size; j = j + size / 4) {
                double[] workerIn = new double[N];
                System.arraycopy(in, j, workerIn, 0, N);
                Worker worker = new Worker(workerIn);
                Future<double[]> future = executor.submit(worker);
                workerFutures.add(future);
            }

            for (Future<double[]> workerFuture : workerFutures) {
                workerFuture.get();
            }
            System.out.println("time: " + (System.nanoTime() - start));
        }
    }

    private static double[] generateIn() {
        final Random random = new Random();

        List<Double> inList = DoubleStream.generate(random::nextDouble).limit(size).boxed().collect(Collectors.toList());
        double[] in = new double[size];

        for (int i = 0; i < size; i++) {
            in[i] = inList.get(i);
        }
        return in;
    }
}

