package preview;


public class City {
	
	private SIR sir=new SIR();
	private double num;
	public City(double x){
		num=x;
		sir.set(1, 0, 0);
	}
	public double[] get(){
		double [] x=sir.get();
		//System.out.println(num+","+x[0]+","+x[1]+","+x[2]);
		double [] y={num*x[0], num*x[1], num*x[2]};
		return y;
	}
	public void set(double x0, double x1, double x2){
		sir.set(x0, x1, x2);
	}
	public void change(double ss, double ii, double rr){
		num=ss+ii+rr;
		sir.set(ss/num, ii/num, rr/num);
	}
	public void update(){
		sir.update();
	}

}
