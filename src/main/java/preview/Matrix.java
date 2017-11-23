package preview;

import java.util.Arrays;

public class Matrix {
	private int maxn = 50;
	private double[][] e=new double[maxn][maxn];
	public Matrix(){
		//Arrays.fill(e, 0.0);
	}
	public Matrix(int x){
		//Arrays.fill(e, 0);
		for (int i=0;i<maxn;i++){
			e[i][i]=x;
		}
	}
	public void set_size(int n){
		maxn=n;
	}
	public void set(int i, int j, double x){
		e[i][j]=x;
	}
	public double get(int i, int j){
		return e[i][j];
	}
	public void clear(){
		Arrays.fill(e, 0);
	}
	public Matrix mul(Matrix b){
		Matrix c = new Matrix();
		for (int k=0;k<maxn;k++)
			for (int i=0;i<maxn;i++)
				for (int j=0;j<maxn;j++)
					c.e[i][j]+=this.e[i][k]*b.e[k][j];
		return c;
	}
}
