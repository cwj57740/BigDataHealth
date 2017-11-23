package bigDataPsychologist;
/*
 * 数据结构：HashMap<String,List<String>> trainData存储训练数据集，String存储序号，List<String>存储词
 * List<boolean> whetherIll对应HashMap存储每个训练数据的是否有病
 * List<String> keyWord存储了要检测的数据的词
 * 
 * 接口：setTrainData设置trainData和whetherIll，返回为空
 * setTestData设置keyWord，返回为空
 * getResult得到结果，返回为true或false
 * getResultNum返回为真的概率
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


public class bayesian {

	/**
	 * @param args
	 */
	
	private HashMap<String, List<String>> trainData;
	private List<Integer> numOfWordEachTrainData;
	private List<Boolean> whetherIll;
	private List<String> keyWord;
	private int[][] keyWordNum;
	private HashSet<String> trainDataSet; 
	private int sumOfAllData;
	private double resultNum;
	
	
	public bayesian(){
		trainData = new HashMap<String, List<String>>();
		numOfWordEachTrainData = new ArrayList<Integer>();
		whetherIll = new ArrayList<Boolean>();
		keyWord = new ArrayList<String>();
		keyWordNum = new int[1000][1000];
		sumOfAllData = 1;
		trainDataSet = new HashSet<String>();
	}
	
	
	private void calculateData(){    //处理数据
		numOfWordEachTrainData = new ArrayList<Integer>();
		keyWordNum = new int[1000][1000];
		sumOfAllData = 1;
		trainDataSet = new HashSet<String>();
		for(int i = 0; i < trainData.size(); i++){
			numOfWordEachTrainData.add(trainData.get(i+"").size());
			sumOfAllData += trainData.get((i + "")).size();
			for(int j = 0; j < trainData.get(i+"").size(); j++){
				trainDataSet.add(trainData.get(i+"").get(j));
			}
			//System.out.println("@@@@@@"+ keyWord.size());
			for(int j = 0; j < keyWord.size(); j++){
				keyWordNum[i][j] = Collections.frequency(trainData.get(i+""), keyWord.get(j));
			}
		}
		//System.out.println("!!!"+trainData);
		//System.out.println("@@@"+keyWord);
	}
	
	private boolean bayesianModel(){   //生成模型，并返回结果
		//set();
		calculateData();
		double probabilityTrue, probabilityFalse;
		double probability1True, probability1False;
		double probability2True = 1;
		double probability2False = 1;
		double downTrue, downFalse;
		double numOfTrue = 1;
		double numOfFalse = 1;
		for(int i = 0; i < whetherIll.size(); i++){
			if(whetherIll.get(i)){
				numOfTrue += numOfWordEachTrainData.get(i);
			}
			if(!whetherIll.get(i)){
				numOfFalse += numOfWordEachTrainData.get(i);
			}
		}
		downTrue = numOfTrue + trainDataSet.size();
		downFalse = numOfFalse + trainDataSet.size();
		probability1True = (double)(numOfTrue / sumOfAllData);
		probability1False = numOfFalse / sumOfAllData;
		for(int i = 0; i < keyWord.size(); i++){
			int sumOfKeyWordTrue = 1;
			int sumOfKeyWordFalse = 1;
			for(int j = 0; j < whetherIll.size(); j++){
				if(whetherIll.get(j)){
					sumOfKeyWordTrue += keyWordNum[j][i];
				}
				if(!whetherIll.get(j)){
					sumOfKeyWordFalse += keyWordNum[j][i];
				}
			}
			probability2True *= (sumOfKeyWordTrue / downTrue);
			probability2False *= (sumOfKeyWordFalse / downFalse);
			probability2False *= (1 / probability2True);
			probability2True *= (1 / probability2True);
		}
		probabilityTrue = probability1True * probability2True;
		probabilityFalse = probability1False * probability2False;
		
		
System.out.println("bayesian" + probabilityTrue + " " + probabilityFalse);
		keyWord = new ArrayList<String>();
		resultNum = probabilityTrue / (probabilityFalse + probabilityTrue);
//System.out.println(resultNum);
		if (probabilityTrue > probabilityFalse){
//System.out.println("True");
			return true;
		}
		else{
//System.out.println("False");
			return false;
		}
	}
	
	
	public double getResultNum(){
//System.out.println(resultNum[0] + " " + resultNum[1]);
		return resultNum;
	}
	
	
	public void setTrainData(HashMap<String, List<String>> trainData, List<Boolean> whetherIll){
		this.trainData = trainData;
		this.whetherIll = whetherIll;
	}
	
	public void setTestData(List<String> testData){
		this.keyWord = testData;
	}
	
	public boolean getResult(){
		return bayesianModel();
	}
	
	
	private void set(){
		try {
			BufferedReader bReader = new BufferedReader(new FileReader(new File("d://data.txt")));
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
		for(int i = 0; i < 5; i++)
			whetherIll.add(true);
		for(int i = 5; i < 10; i++)
			whetherIll.add(false);
//		System.out.println(trainData.get(3+""));
//		for(int i = 0; i < trainData.get(3+"").size(); i++)
//			keyWord.add(trainData.get(3+"").get(i));
		keyWord.add("兴奋");
		keyWord.add("快乐");
		keyWord.add("喜悦");
		keyWord.add("愉快");
		keyWord.add("悲伤");
		keyWord.add("悲痛");
		keyWord.add("悲惨 ");
		keyWord.add("悲惨 ");
		keyWord.add("兴奋");
		keyWord.add("快乐");
		keyWord.add("喜悦");
		keyWord.add("愉快");
		keyWord.add("悲伤");
		keyWord.add("悲痛");
		keyWord.add("悲惨 ");
		keyWord.add("悲惨 ");
		keyWord.add("由此");
		keyWord.add("由此");
		keyWord.add("由此");
		keyWord.add("由此");
		keyWord.add("由此");
		
//		try {
//			BufferedReader bReader = new BufferedReader(new FileReader(new File("d://data.txt")));
//			while (bReader.ready()){
//				String line = bReader.readLine();
//				String[] terms = line.split(" ");
//				for(int i = 0; i < terms.length; i++){
//					keyWord.add(terms[i]);
//				}
//			}
//			bReader.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		//System.out.println(keyWord.size());
	
	}
	
	public static void main(String[] args) {
		bayesian b = new bayesian();
		b.getResult();
		
	}

}

