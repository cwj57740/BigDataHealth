package preview;

import java.util.Scanner;
public class Test {

	static double [][] a;
	static double [] b;
	static double [] s;

	public static void main(String[] args) {


		BP u= new BP(32, 15, 1);
		double std_input[]=new double[32];
		double std_output[]=new double[1];
		u.train(std_input, std_output);
		double input[]=new double[32];
		double[] result=u.test(input);

	/*SIR s=new SIR();
	//	s.init(1-result[0], result[0], 0);
		s.show();
	//	s.UpdateToTime(13);
		s.show();*/

		Scanner cin=new Scanner(System.in);
		System.out.println("input city number:");

		int city_number = cin.nextInt();
		a = new double[city_number][city_number];
		b = new double[city_number];
		s = new double[city_number];

		System.out.println("input the matrix of population transportation:");
		for (int i=0;i<city_number;i++){
			for (int j=0;j<city_number;j++){
				a[i][j] = cin.nextDouble();
			}
		}

		System.out.println("input the initial population of each city:");
		for (int i=0;i<city_number;i++){
			b[i] = cin.nextDouble();
		}

		System.out.println("input the initial prevalence of each city:");

		for (int i=0;i<city_number;i++) {
			s[i] = cin.nextDouble();
		}

		System.out.println("input the time interval:");

		int t = cin.nextInt();

		Network c=new Network(b, a, city_number);
		c.output();
		for (int i=0;i<city_number;i++)
			c.infect(i, s[i]);

		for (int i=0;i<t;i++){
			c.UpdateFor(1);
			c.output();
		}
	}
}

