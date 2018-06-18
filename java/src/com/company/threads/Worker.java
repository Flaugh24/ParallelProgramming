package com.company.threads;

import java.util.concurrent.Callable;

import static com.company.threads.Parallel.H;
import static com.company.threads.Parallel.N;

public class Worker implements Callable<double[]> {

    private double[] in = new double[N];

    public Worker(double[] in) {
        this.in = in;
    }

    @Override
    public double[] call() throws Exception {
        double out[] = new double[N];
        //Фильтрация входных данных
        for (int i = 0; i < in.length; i++) {
            out[i] = 0.;
            for (int j = 0; j < N - 1; j++)// та самая формула фильтра
                if (i - j >= 0)
                    out[i] += H[j] * in[i - j];
        }
        return out;
    }
}
