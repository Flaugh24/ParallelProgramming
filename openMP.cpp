#include <math.h>;
#include <omp.h>
#include <ctime>
#include <iostream>

using namespace std;

const double M_PI = 3.141592653589793238462643383279;

void main()
{
	const int sizeIn = 10000000;

	long double *in;
	long double *out;

	in = new long double[sizeIn];
	out = new long double[sizeIn];

	for (int i = 0; i < sizeIn; i++) {
		in[i] = rand() % 10;
	}


	const int N = 20; //Длина фильтра
	long double Fd = 2000; //Частота дискретизации входных данных
	long double Fs = 20; //Частота полосы пропускания
	long double Fx = 50; //Частота полосы затухания

	long double H[N] = { }; //Импульсная характеристика фильтра
	long double H_id[N] = { 0 }; //Идеальная импульсная характеристика
	long double W[N] = { 0 }; //Весовая функция

							  //Расчет импульсной характеристики фильтра
	double Fc = (Fs + Fx) / (2 * Fd);

	for (int i = 0; i<N; i++)
	{
		if (i == 0) H_id[i] = 2 * M_PI*Fc;
		else H_id[i] = sinl(2 * M_PI*Fc*i) / (M_PI*i);
		// весовая функция Блекмена
		W[i] = 0.42 - 0.5 * cosl((2 * M_PI*i) / (N - 1)) + 0.08 * cosl((4 * M_PI*i) / (N - 1));
		H[i] = H_id[i] * W[i];
	}

	//Нормировка импульсной характеристики
	double SUM = 0;
	for (int i = 0; i<N; i++) SUM += H[i];
	for (int i = 0; i<N; i++) H[i] /= SUM; //сумма коэффициентов равна 1 

										   //Фильтрация входных данных
	
	
	omp_set_num_threads(8);
	int step = sizeIn / 8;

	for (int k = 0; k < 10; k++) {
		unsigned int start_time = clock(); // начальное время
#pragma omp parallel for schedule(dynamic, step) private(j, i) 
		for (int i = 0; i < sizeIn; i++)
		{
			out[i] = 0.;
			for (int j = 0; j < N - 1; j++)// та самая формула фильтра
				if (i - j >= 0)
					out[i] += H[j] * in[i - j];
		}

		unsigned int end_time = clock(); // конечное время

		/*
		for (int i = 0; i < sizeIn; i++) {
			cout << out[i]<<"\n";
		}
		*/

		cout << "time " << end_time - start_time << "\n"; // искомое время
	}
	system("pause");

}