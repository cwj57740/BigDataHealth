package com.diagnosis;

//定义KD树中的医院节点
public class HospitalNode {
	public int hospital_id=0;
	public double [] position=new double[2];
	public double distance=0;
	public double score=0;
	public HospitalNode(){
		super();
	}
	public void insert(){
		;
	}
	public double [] getPosition(){
		return this.position;
	}
	public double getscore()
	{
		return this.score;
	}
	//计算距离的平方值
	public double sqrDistance(double [] startPoint){
		double dist=0;
		int numDimensionPoint=2;
		for(int i=0;i<numDimensionPoint;++i){
			double diff=this.position[i]-startPoint[i];
			dist+=diff*diff;
		}
		return dist;
	}
	//确定节点之间的控制关系
	public boolean isDominatedBy(HospitalNode p){
		if(p.distance<=this.distance && p.score>= this.score&& (p.distance<this.distance || p.score> this.score)){
			return true;
		}
		return false;
	}
	//打印节点信息
	public void display(){
		//MapDistance mdDistance=new MapDistance();
		//double dis=mdDistance.getDistance(this.position[0], this.position[1], 45.4133067062, 125.9483345462);
		System.out.println("Hospital_id: "+this.hospital_id+" ; Distance:"+this.distance+"  ; score: "+this.score+" ; Position: ["+this.position[0]+","+this.position[1]+"]");
	}
}

