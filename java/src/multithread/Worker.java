package multithread;

import fir.FIR;

import java.util.concurrent.Callable;

import static fir.FIR.N;

public class Worker implements Callable<double[]> {

    private double[] in = new double[N];

    Worker(double[] in) {
        this.in = in;
    }

    @Override
    public double[] call() throws Exception {
        //Фильтрация входных данных
        return FIR.filtration(in);
    }
}
