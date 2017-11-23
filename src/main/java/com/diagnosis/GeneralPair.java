package com.diagnosis;
//可以不用你了
public class GeneralPair<E extends Object,F extends Object> {
	private E first;
	private F second;
	
	public GeneralPair(E first,F second){
		this.first=first;
		this.second=second;
	}
	
	public E getFirst(){
		return first;
	}
	
	public F getSecond(){
		return second;
	}
	
	public void setFirst(E first){
		this.first=first;
	}
	
	public void setSecond(F second){
		this.second=second;
	}
}
