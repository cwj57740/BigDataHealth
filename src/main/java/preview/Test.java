package preview;

public class Test {
	
	static double [][] a= {
			{0.9999846, 0.0000031, 0.0000002, 0.0000023, 0.0000098},
			{0.0000036, 0.9999789, 0.0000034, 0.0000075, 0.0000066},
			{0.0000077, 0.0000007, 0.9999829, 0.0000054, 0.0000033},
			{0.0000045, 0.0000092, 0.0000043, 0.9999795, 0.0000025},
			{0.0000098, 0.0000055, 0.0000012, 0.0000027, 0.9999808}
	};
	static double [] b={100000000,100000000,100000000,100000000,100000000};
	
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
		
		Network c=new Network(b, a, 5);
		c.output();
		c.UpdateFor(0.001);
		//���
		c.output();
		c.infect(0, 1e-6);
		for (int i=0;i<=20;i++){
			c.UpdateFor(1);
			c.output();
		}
	}
}

