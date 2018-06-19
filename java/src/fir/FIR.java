package fir;

import static java.lang.Math.PI;
import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.sin;

public class FIR {

    public final static int N = 25; //Длина фильтра
    private final static double Fd = 2000; //Частота дискретизации входных данных
    private final static double Fs = 20; //Частота полосы пропускания
    private final static double Fx = 50; //Частота полосы затухания

    private static double H[] = new double[N]; //Импульсная характеристика фильтра
    private static double H_id[] = new double[N]; //Идеальная импульсная характеристика
    private static double W[] = new double[N]; //Весовая функция

    static {
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

    }

    public static double[] filtration(double[] in){
        double[] out = new double[in.length];
        for (int i = 0; i < in.length; i++) {
            out[i] = 0.;
            for (int j = 0; j < N - 1; j++)// та самая формула фильтра
                if (i - j >= 0)
                    out[i] += H[j] * in[i - j];
        }
        return out;
    }
}
