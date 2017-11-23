package bigDataPsychologist;

/*
 * 原因：工作，情感，家庭，经济
 * 
 * 数据结构：HashMap<String, List<String>> trainData存储了训练样本数据
 * HashMap<String, List<Boolean>> TrainDataReason存储了每一组训练样本的原因，一个List<String>对应一个List<Boolean>，List<Boolean>中有4个值，和原因对应，值为true的是真正原因
 * List<String> testData存储了要测试的数据
 *  
 * 接口：setTrainData(HashMap<String, List<String>> trainData, HashMap<String, List<Boolean>> TrainDataReason) 设置训练数据   返回为空
 * setTestData(HashMap<String, List<String>> testData)设置测试数据     返回为空
 * getResult()得到结果，返回值为List<Double>
 * 
 * 
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



public class bayesianForReason {
	
	private HashMap<String, List<String>> trainData;
	private HashMap<String, List<Boolean>> trainDataReason;
	private List<String> keyWord;
	private List<Integer> numOfWordEachTrainData;
	private int[][] keyWordNum;
	private HashSet<String> trainDataSet; 
	private int sumOfAllData;
	
	
	public bayesianForReason(){
		trainData = new HashMap<String, List<String>>();
		numOfWordEachTrainData = new ArrayList<Integer>();
		trainDataReason = new HashMap<String, List<Boolean>>();
		keyWord = new ArrayList<String>();
		keyWordNum = new int[1000][1000];
		sumOfAllData = 1;
		trainDataSet = new HashSet<String>();
	}
	
	public void setTrainData(HashMap<String, List<String>> trainData, HashMap<String, List<Boolean>> trainDataReason){
		this.trainData = trainData;
		this.trainDataReason = trainDataReason;
	}
	
	public void setTestData(List<String> keyWord){
		this.keyWord = keyWord;
	}
	
	
	public List<Double> getResult(){
		return bayesianModel();
	}
	
	private void calculateData(){    //处理数据
		for(int i = 0; i < trainData.size(); i++){
			numOfWordEachTrainData.add(trainData.get(i+"").size());
			sumOfAllData += trainData.get((i + "")).size();
			for(int j = 0; j < trainData.get(i+"").size(); j++){
				trainDataSet.add(trainData.get(i+"").get(j));
			}
			for(int j = 0; j < keyWord.size(); j++){
				keyWordNum[i][j] = Collections.frequency(trainData.get(i+""), keyWord.get(j));
			}
		}
	}
	
	
	private int getMAX(List<Double> num){
		if(num.get(0) >= num.get(1) && num.get(0) >= num.get(2) && num.get(0) >= num.get(3)){
			return 0;
		}
		if(num.get(1) >= num.get(0) && num.get(1) >= num.get(2) && num.get(1) >= num.get(3)){
			return 1;
		}
		if(num.get(2) >= num.get(1) && num.get(2) >= num.get(0) && num.get(2) >= num.get(3)){
			return 2;
		}
		return 3;
	}
	
	private List<Double> bayesianModel(){   //生成模型，并返回结果
		//set();
		calculateData();
		List<Boolean> result = new ArrayList<Boolean>();
		List<Double> num = new ArrayList<Double>();
		int position;
		double probability_1, probability_2, probability_3, probability_4;
		double probability1_1, probability1_2, probability1_3, probability1_4;
		double probability2_1 = 1;
		double probability2_2 = 1;
		double probability2_3 = 1;
		double probability2_4 = 1;
		double down_1, down_2, down_3, down_4;
		double numOf_1 = 0.1;
		double numOf_2 = 0.1;
		double numOf_3 = 0.1;
		double numOf_4 = 0.1;

		for(int i = 0; i < trainDataReason.size(); i++){
			if(trainDataReason.get(i+"").get(0)){
				numOf_1 += numOfWordEachTrainData.get(i);
			}
			if(trainDataReason.get(i+"").get(1)){
				numOf_2 += numOfWordEachTrainData.get(i);
			}
			if(trainDataReason.get(i+"").get(2)){
				numOf_3 += numOfWordEachTrainData.get(i);
			}
			if(trainDataReason.get(i+"").get(3)){
				numOf_4 += numOfWordEachTrainData.get(i);
			}
		}
		down_1 = numOf_1 + trainDataSet.size();
		down_2 = numOf_2 + trainDataSet.size();
		down_3 = numOf_3 + trainDataSet.size();
		down_4 = numOf_4 + trainDataSet.size();
		probability1_1 = (double)(numOf_1 / sumOfAllData);
		probability1_2 = (double)(numOf_2 / sumOfAllData);
		probability1_3 = (double)(numOf_3 / sumOfAllData);
		probability1_4 = (double)(numOf_4 / sumOfAllData);
		for(int i = 0; i < keyWord.size(); i++){
			int sumOfKeyWord_1 = 1;
			int sumOfKeyWord_2 = 1;
			int sumOfKeyWord_3 = 1;
			int sumOfKeyWord_4 = 1;
//System.out.println(trainDataReason);
			for(int j = 0; j < trainDataReason.size(); j++){
//System.out.println(trainDataReason.get(j).get(0));
				if(trainDataReason.get(j+"").get(0)){
					sumOfKeyWord_1 += keyWordNum[j][i];
				}
				if(trainDataReason.get(j+"").get(1)){
					sumOfKeyWord_2 += keyWordNum[j][i];
				}
				if(trainDataReason.get(j+"").get(2)){
					sumOfKeyWord_3 += keyWordNum[j][i];
				}
				if(trainDataReason.get(j+"").get(3)){
					sumOfKeyWord_4 += keyWordNum[j][i];
				}
			}
			probability2_1 *= (sumOfKeyWord_1 / down_1);
			probability2_2 *= (sumOfKeyWord_2 / down_2);
			probability2_3 *= (sumOfKeyWord_3 / down_3);
			probability2_4 *= (sumOfKeyWord_4 / down_4);
			
			probability2_2 *= ( 1 / probability2_1);
			probability2_3 *= ( 1 / probability2_1);
			probability2_4 *= ( 1 / probability2_1);
			probability2_1 *= ( 1 / probability2_1);
			
//			System.out.println(probability2_1);
//			System.out.println(probability2_2);
//			System.out.println(probability2_3);
//			System.out.println(probability2_4);
			
		}
		
		probability_1 = probability1_1 * probability2_1;
		probability_2 = probability1_2 * probability2_2;
		probability_3 = probability1_3 * probability2_3;
		probability_4 = probability1_4 * probability2_4;
//		probability_1 *= ( 1 / probability_1 *1.2);
//		probability_2 *= ( 1 / probability_1 *1.2);
//		probability_3 *= ( 1 / probability_1 *1.2);
//		probability_4 *= ( 1 / probability_1 *1.2);
//System.out.println(probability_1 + " " + probability_2 + " " + probability_3 + " " + probability_4);
		num.add(probability_1);
		num.add(probability_2);
		num.add(probability_3);
		num.add(probability_4);
		System.out.println(num);
		return num;
//		position = getMAX(num);
//		switch(position){
//			case(0):{
//				System.out.println("0");
//				result.add(true);
//				result.add(false);
//				result.add(false);
//				result.add(false);
//			}
//			case(1):{
//				System.out.println("1");
//				result.add(false);
//				result.add(true);
//				result.add(false);
//				result.add(false);
//			}
//			case(2):{
//				System.out.println("2");
//				result.add(false);
//				result.add(false);
//				result.add(true);
//				result.add(false);
//			}
//			default:{
//				System.out.println("3");
//				result.add(false);
//				result.add(false);
//				result.add(false);
//				result.add(true);
//			}
//		}
//		return result;
	}
	
	
	public void tempSet(){
		int num = 0; 
		try {
			BufferedReader bReader = new BufferedReader(new FileReader(new File("d://ReasonBayesian.txt")));
			while (bReader.ready()){
				List<String> tempList = new ArrayList<String>();
				String st;
				String stList[];
				st = bReader.readLine();
				stList = st.split(" ");
				for(int i = 0; i < stList.length; i++)
					tempList.add(stList[i]);
//System.out.println(tempList);
				trainData.put(num + "", tempList);
				num++;
				//System.out.println(inputCheckString);
			}
			bReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<Boolean> tempBoo = new ArrayList<Boolean>();
		tempBoo.add(true);
		tempBoo.add(false);
		tempBoo.add(false);
		tempBoo.add(false);
		for(int i = 0; i < 3; i++){
			trainDataReason.put(i+"", tempBoo);
		}
		tempBoo = new ArrayList<Boolean>();
		tempBoo.add(false);
		tempBoo.add(true);
		tempBoo.add(false);
		tempBoo.add(false);
		for(int i = 3; i < 6; i++){
			trainDataReason.put(i+"", tempBoo);
		}
		tempBoo = new ArrayList<Boolean>();
		tempBoo.add(false);
		tempBoo.add(false);
		tempBoo.add(true);
		tempBoo.add(false);
		for(int i = 6; i < 9; i++){
			trainDataReason.put(i+"", tempBoo);
		}
		tempBoo = new ArrayList<Boolean>();
		tempBoo.add(false);
		tempBoo.add(false);
		tempBoo.add(false);
		tempBoo.add(true);
		for(int i = 9; i < 12; i++){
			trainDataReason.put(i+"", tempBoo);
		}
		
		
//		keyWord.add("工作");
//		keyWord.add("早醒");
//		keyWord.add("恶心");
//		keyWord.add("睡眠");
//		keyWord.add("睡眠");
//		keyWord.add("工作");
//		keyWord.add("疲惫");
//		keyWord.add("贪睡 ");
//		keyWord.add("说话");
//		keyWord.add("不想");
//		keyWord.add("工作");
//		keyWord.add("失眠");
//		keyWord.add("失眠");
//		keyWord.add("很累");
//		keyWord.add("工作 ");
		
		
//		keyWord.add("分手");
//		keyWord.add("可惜");
//		keyWord.add("最后");
//		keyWord.add("寂寞");
//		keyWord.add("雨中");
//		keyWord.add("丰收");
//		keyWord.add("眼泪");
//		keyWord.add("痛哭 ");
//		keyWord.add("眼泪");
//		keyWord.add("错过");
//		keyWord.add("伤心");
//		keyWord.add("生命");
//		keyWord.add("我");
//		keyWord.add("他");
//		keyWord.add("她 ");
//		
//		keyWord.add("我爸");
//		keyWord.add("脾气");
//		keyWord.add("唠叨");
//		keyWord.add("奶奶");
//		keyWord.add("我妈");
//		keyWord.add("维护");
//		keyWord.add("打架");
//		keyWord.add("和睦 ");
//		keyWord.add("不");
//		keyWord.add("超级");
//		keyWord.add("竟然");
//		keyWord.add("脾气");
//		keyWord.add("小时候");
//		keyWord.add("她");
//		keyWord.add("我爸 ");
//		
//		keyWord.add("债");
//		keyWord.add("工资");
//		keyWord.add("开销");
//		keyWord.add("工资");
//		keyWord.add("赚钱");
//		keyWord.add("剩余");
//		keyWord.add("房贷");
//		keyWord.add("钱 ");
//		keyWord.add("块");
//		keyWord.add("每月");
//		keyWord.add("工作");
//		keyWord.add("信用卡");
//		keyWord.add("开销");
//		keyWord.add("分手");
//		keyWord.add("脾气 ");
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		bayesianForReason bfr = new bayesianForReason();
		bfr.tempSet();
		bfr.getResult();

	}

}
