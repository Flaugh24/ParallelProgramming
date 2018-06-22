package multithread;

import util.FIR;

import java.util.concurrent.Callable;

import static util.FIR.N;

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
