package multithread;

import util.Generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static util.FIR.N;


public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        final int size = 100;
        final int countThreads = 4;
        double[] in = Generator.generate(size);

        List<Future<double[]>> workerFutures = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(countThreads);

        long startTime = System.nanoTime();

        for (int j = 0; j < size; j = j + size / countThreads) {
            double[] workerIn = new double[N];
            System.out.println(j);
            System.arraycopy(in, j, workerIn, 0, N);
            Worker worker = new Worker(workerIn);
            Future<double[]> future = executor.submit(worker);
            workerFutures.add(future);
        }

        for (Future<double[]> workerFuture : workerFutures) {
            System.out.println(Arrays.toString(workerFuture.get()));
        }

        System.out.println("execution time: " + (System.nanoTime() - startTime));
    }
}

