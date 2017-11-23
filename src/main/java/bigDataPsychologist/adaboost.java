package bigDataPsychologist;

/*
 * 
 * alpha[0]bayesian   alpha[1]logical
 * 
 * 接口：setTrainData(HashMap<String, List<String>> trainData, List<Boolean> trainWhether) 设置数据   返回为空
 * calculate(logical l,bayesian b)进行adaboost的运算，返回值为double[],其中存储了两个分类器的权值。
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Collections;  
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class adaboost {

	/**
	 * @param args
	 */
	
//	private HashMap<String, List<String>> trainData;
//	private List<Boolean> whetherIll;
//	private List<String> keyWord;
	private double[] alpha;
	private List<Double> w;
	private HashMap<String, List<String>> trainData;
	private List<Boolean> trainWhether;
	
	public adaboost(){
//		trainData = new HashMap<String, List<String>>();
//		whetherIll = new ArrayList<Boolean>();
//		keyWord = new ArrayList<String>();
		alpha = new double[2];
		w = new ArrayList<Double>();
		trainData = new HashMap<String, List<String>>();
		trainWhether = new ArrayList<Boolean>();
	}
	
	
	private void getData(){
//		try {
//			BufferedReader bReader = new BufferedReader(new FileReader(new File("d://trainData.txt")));
//			int time = 0;
//			int key = 0;
//			List<String> data = new ArrayList<String>();
//			while (bReader.ready()){
//				if((time % 3 == 0 && time != 0) || time == 29){
//					trainData.put(key + "", data);
//					key ++;
//					data = new ArrayList<String>();				
//				}
//				String line = bReader.readLine();
//				String[] terms = line.split(" ");
//				for(int i = 0; i < terms.length; i++){
//					data.add(terms[i]);
//				}
//				time ++;
//			}
//			bReader.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		try {
			BufferedReader bReader = new BufferedReader(new FileReader(new File("d://testData.txt")));
			int time = 0;
			int key = 0;
			List<String> data = new ArrayList<String>();
			while (bReader.ready()){
				if((time % 3 == 0 && time != 0) || time == 29){
					trainData.put(key + "", data);
					key ++;
					data = new ArrayList<String>();				
				}
				String line = bReader.readLine();
				String[] terms = line.split(" ");
				for(int i = 0; i < terms.length; i++){
					data.add(terms[i]);
				}
				time ++;
			}
			bReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		try {
//			BufferedReader bReader = new BufferedReader(new FileReader(new File("d://keyWord.txt")));
//			while(bReader.ready()){
//				String line = bReader.readLine();
//				String terms[] = line.split(" ");
//				for(int i = 0; i < terms.length; i++)
//					keyWord.add(terms[i]);
//			}
//			bReader.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		try {
//			BufferedReader bReader = new BufferedReader(new FileReader(new File("d://whether.txt")));
//			while(bReader.ready()){
//				String line = bReader.readLine();
//				String terms[] = line.split(" ");
//				for(int i = 0; i < terms.length; i++){
//					if(terms[i] == "1")
//						whetherIll.add(true);
//					else
//						whetherIll.add(false);
//				}
//			}
//			bReader.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		try {
			BufferedReader bReader = new BufferedReader(new FileReader(new File("d://testWhether.txt")));
			while(bReader.ready()){
				String line = bReader.readLine();
				String terms[] = line.split(" ");
				for(int i = 0; i < terms.length; i++){
					if(terms[i] == "1")
						trainWhether.add(true);
					else
						trainWhether.add(false);
				}
			}
			bReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(int i = 0; i < trainData.size(); i ++)
			w.add(1.0/trainData.size());
		
	}

	
	public double[] calculate(bayesian b, logical l){
		double sigma = 1;
		double z;
		double tempw;
		int boolean1, boolean2;
		
		for(int i = 0; i < trainData.size(); i ++)
			w.add(1.0/trainData.size());
		//System.out.println("!!!!!!!!!!!!!!!!!w.size" + w.size());
		while(sigma > 0.002){
			sigma = 0.001;
			z = 0;
//System.out.println(w);
			for(int i = 0; i < trainData.size(); i++){
				b.setTestData(trainData.get(i+""));
				
				if(trainWhether.get(i) != (b.getResult()))
					sigma += w.get(i);
			}
			alpha[0] = Math.log((1 - sigma)/sigma);
			//////////////w的迭代函数
			for(int i = 0; i < w.size(); i++){
				//System.out.println("!!!!!!!!!!!");
				b.setTestData(trainData.get(i+""));
				//System.out.println(trainData.get(i+""));
				if(b.getResult())
					boolean1 = 1;
				else
					boolean1 = 0;
				if(trainWhether.get(i))
					boolean2 = 1;
				else
					boolean2 = 0;
				z += w.get(i) * (Math.exp(-alpha[0] * Math.abs(boolean1 - boolean2)));
			}
			
			for(int i = 0; i < w.size(); i++){
				b.setTestData(trainData.get(i+""));
				//System.out.println("###" + w.size() + "###");
				//System.out.println("!!!!");
				if(b.getResult())
					boolean1 = 1;
				else
					boolean1 = 0;
				if(trainWhether.get(i))
					boolean2 = 1;
				else
					boolean2 = 0;
				tempw = w.get(i) * (Math.exp(-alpha[0] * Math.abs(boolean1 - boolean2)));
				tempw /= z;
				w.set(i, tempw);
				
			}
//System.out.println(sigma);
		}
		sigma = 1;
		w = new ArrayList<Double>();
		for(int i = 0; i < trainData.size(); i ++)
			w.add(1.0/trainData.size());
//System.out.println("!!!!");
		while(sigma > 0.002){
			sigma = 0.001;
			z = 0;
//System.out.println(w);
			for(int i = 0; i < trainData.size(); i++){
				l.setTestData(trainData.get(i+""));
				if(trainWhether.get(i) != (l.getResult()))
					sigma += w.get(i);
			}
//System.out.println(sigma);
			alpha[1] = Math.log((1 - sigma)/sigma);
			for(int i = 0; i < w.size(); i++){
				double tempI;
				l.setTestData(trainData.get(i+""));
				if(l.getResult())
					boolean1 = 1;
				else 
					boolean1 = 0;
				if (trainWhether.get(i))
					boolean2 = 1;
				else 
					boolean2 = 0;
				if(boolean1 == boolean2)
					tempI = 1.0;
				else
					tempI = 0.0;
				z += w.get(i) * (Math.exp(-alpha[1] * tempI));
			}
			for(int i = 0; i < w.size(); i++){
				l.setTestData(trainData.get(i+""));
				double tempI;
				if(l.getResult())
					boolean1 = 1;
				else 
					boolean1 = 0;
				if (trainWhether.get(i))
					boolean2 = 1;
				else 
					boolean2 = 0;
				if(boolean1 == boolean2)
					tempI = 1.0;
				else
					tempI = 0.0;
				tempw = w.get(i) * (Math.exp(-alpha[1] *tempI));
				tempw /= z;
				w.set(i, tempw);
			}
//System.out.println(sigma);
		}
		
		return alpha;
		
	}
	
	
	public void setTestData(HashMap<String, List<String>> trainData, List<Boolean> trainWhether){
		this.trainData = trainData;
		this.trainWhether = trainWhether;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
