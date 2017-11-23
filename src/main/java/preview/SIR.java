package preview;


public class SIR {
	static double beta=1.4247;
	static double gama=0.14286;
	static double dt=0.001;
	
	private double x[] = {1,0,0};
	private double cur_time =0;
	
	public double [] get(){
		return x;
	}
	public void set(double x0, double x1, double x2){
		x[0]=x0; x[1]=x1; x[2]=x2;
	}
	
	public void update(){
		double dx[] = new double [3];
		dx[0]=-beta*x[0]*x[1];
		dx[1]=beta*x[0]*x[1]-gama*x[1];
		dx[2]=gama*x[1];
		x[0]+=dx[0]*dt;
		x[1]+=dx[1]*dt;
		x[2]+=dx[2]*dt;
		cur_time+=dt;
	}	
	public void show(){
		System.out.println("current time is: "+cur_time);
		System.out.println("S:"+x[0]+"  I:"+x[1]+"  R:"+x[2]);
	}
}

