package preview;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Network {
	private int num;
	private double cur_time;

	public double getCur_time() {
		return cur_time;
	}

	public void setCur_time(double cur_time) {
		this.cur_time = cur_time;
	}

	static double dt=0.001;
	private City[] city;
	private double[][] tran;
	public Network(double []a, double [][] b, int n){
		num=n;
		cur_time=0;
		
		city=new City[num];
		tran=new double [num][num];
		for (int i=0;i<num;i++){
			city[i]=new City(a[i]);			
		}
		for (int i=0;i<n;i++)
			for (int j=0;j<n;j++)
				tran[i][j]=b[i][j];
	}
	private void transform(){
		for (int i=0;i<num;i++){
			double[] x={0,0,0};
			for (int j=0;j<num;j++){
				double [] y=city[j].get();
				x[0]+=y[0]*tran[j][i];
				x[1]+=y[1]*tran[j][i];
				x[2]+=y[2]*tran[j][i];
			}
			city[i].change(x[0], x[1], x[2]);				
		}
	}
	private void update(){
		for (int i=0;i<num;i++)
			city[i].update();
	}
	public void UpdateFor(double t){
		for (double i=0;i<t;i+=dt){
			transform();
			update();
			cur_time+=dt;
		}
	}
	public void infect(int id, double rate){
		city[id].set(1-rate, rate, 0);
	}
	public void output(){
		System.out.printf("current time is : %.0f\n",cur_time);
		for (int i=0;i<num;i++){
			System.out.print("City "+i+": ");
			System.out.printf("S: %.0f I: %.0f R: %.0f\n",city[i].get()[0],city[i].get()[1],city[i].get()[2]);
		}
	}
	public Map<String,Map> getResult(){
		Map<String,Map> resultMap = new HashMap<>();
		for (int i=0;i<num;i++){
			Map<String,String> map = new HashMap<>();
			map.put("S",Double.toString(city[i].get()[0]));
			map.put("I",Double.toString(city[i].get()[1]));
			map.put("R",Double.toString(city[i].get()[2]));
			resultMap.put(Integer.toString(i),map);
		}
		return resultMap;
	}
}

