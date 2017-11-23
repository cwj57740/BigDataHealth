package bigDataPsychologist;

/*
 * 数据结构：HashMap<String,List<String>> trainData存储训练数据集，String存储序号，List<String>存储词
 * List<boolean> whetherIll对应HashMap存储每个训练数据的是否有病
 * List<String> keyWord存储了关键词
 * List<String>  testData存储了要测试的数据
 * 
 * 接口：run（）计算生成模型
 * getResult()返回结果，返回结果为boolean
 * setTrainData设置训练样本
 * setTestData设置检测样本
 * setKeyWord设置关键词
 * getResultNum()返回为真的概率
 */

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;  


public class logical {
	HashMap<String, List<String>> trainData;
	List<Boolean> whetherIll;
	List<String> testData;
	List<String> keyWord;
	double resultNum;
	int[][] x;
    int[] y;
    int[][] test;
    double[] thet;
	final static double STEP = 0.01;
	final static double MAX = 100;
	final static double LIMIT = 0.000001;
	
	
	public logical(){
		trainData = new HashMap<String, List<String>>();
		whetherIll = new ArrayList<Boolean>();
		testData = new ArrayList<String>();
		keyWord = new ArrayList<String>();
		x = new int[1000][100];
	    y = new int[1000];
	    thet = new double[100];
	    test = new int[1000][100];
	}
	
	
	private void processTrainData(){
		for(int i = 0; i < trainData.size(); i++){
			for(int j = 0; j < keyWord.size(); j++){
				x[i][j] = Collections.frequency(trainData.get(i+""), keyWord.get(j));
			}
			if(whetherIll.get(i))
				y[i] = 1;
			else
				y[i] = 0;
		}
		for(int i = 0; i < keyWord.size(); i++){
			if(i < keyWord.size() / 2)
				thet[i] = 1;
			else 
				thet[i] = -1;
		}
	}
	
	
	public void setKeyWord(List<String> keyWord){
		this.keyWord = keyWord;
	}
	
	public void setTrainData(HashMap<String, List<String>> trainData, List<Boolean> whetherIll){
		this.trainData = trainData;
		this.whetherIll = whetherIll;
	}
	
	public void setTestData(List<String> testData){
		this.testData = testData;
	}
	
	private double HThtX(double[] thet,int[] x, int len){
		double footer, hx;
	    double index = 0;
	    for(int i = 0; i < len; i++){
	        index += (thet[i] * x[i]);
	    }
	    index = 0 - index;
	    footer = 1 + Math.exp(index);
	    hx = 1 / footer;
	    return hx;
	}

	private void theta(double[] thet, int[][] x, int[] y, int len, int wid ){
		double hx, subtract, value, sum;
	    for(int j = 0; j < len; j++){
	        sum = 0;
	        for(int i = 0; i < wid; i++){
	            hx = HThtX(thet, x[i], len);
	            subtract = hx - y[i];
	            value = subtract * x[i][j];
	            sum += value;
	        }
	        sum = sum / len;
	        sum = sum + (thet[j] / len);
	        sum *= STEP;
	        thet[j] = thet[j] - sum;
	    }
	}
	
	private void thetaFirst(double[] thet, int[][] x, int[] y, int len, int wid ){
		double hx, subtract, value, sum;
	    for(int j = 0; j < len; j++){
	        sum = 0;
	        for(int i = 0; i < wid; i++){
	            hx = HThtX(thet, x[i], len);
	            subtract = hx - y[i];
	            value = subtract * x[j][i];
	            sum += value;
	        }
	        sum = sum / len;
	        sum *= STEP;
	        thet[j] = thet[j] - sum;
	    }
	}
	
	private double JThet(double[] thet, int[][] x, int[] y, int len, int wid){
		double j, mul1, mul2, sum, value;
	    sum = 0;
	    value = 0;
	    for(int i = 0; i < wid; i++){
	        mul1 = Math.log(HThtX(thet, x[i], len)) * y[i];
	        mul2 = Math.log(1 - HThtX(thet, x[i], len)) * (1 - y[i]);
	        sum += mul1;
	        sum += mul2;
	    }
	    sum /= wid;
	    for(int i = 0; i < wid; i++){
	        value += Math.pow(thet[i], 2);
	    }
	    value /=  2;
	    value /= wid;
	    j = value - sum;
	    return j;
	}
	

	public void run(){
		double jthet;
		double tempJthet;
		
		processTrainData();
		jthet = JThet(thet, x, y, keyWord.size(), trainData.size());
		theta(thet, x, y, keyWord.size(), trainData.size());
		tempJthet = jthet;
		jthet = JThet(thet, x, y, keyWord.size(), trainData.size());
		while(tempJthet - jthet > LIMIT){
	        theta(thet, x, y, keyWord.size(), trainData.size());
	        tempJthet = jthet;
	        jthet = JThet(thet, x, y, keyWord.size(), trainData.size());
	        //System.out.println(jthet);
	    }
	}
	
	
	public boolean getResult(){
		double result;
		int[] test = new int[100];
		for(int i = 0; i < keyWord.size(); i++){
			test[i] = Collections.frequency(testData, keyWord.get(i));
		}
		result = HThtX(thet, test, keyWord.size());
//System.out.println("true");
		resultNum = result;
		if (result > 0.5){
			//System.out.println("true");
			return true;	
		}
		else{
			//System.out.println("false");
			return false;
		}
	}
	
	
	public double getResultNum(){
		return resultNum;
	}
	
	public void run1(){
		int len;
	    int wid;
	    double jthet;
	    for(int i = 0; i < 7; i++){
	        for(int j = 0; j < MAX; j++)
	            x[i][j] = -1;
	        y[i] = -1;
	        thet[i] = 0.005;
	    }
	    for(int i = 7; i < MAX; i += 2){
	    	thet[i] = -0.005;
	    }
	    System.out.println("input the length and width");
	    Scanner in = new Scanner(System.in);
	    len = in.nextInt();
	    wid = in.nextInt();
	    System.out.println("x");
	    for(int i = 0; i < wid; i++){
	        System.out.println(i+1);
	        for(int j = 0; j < len; j++){
	        	x[i][j] = in.nextInt();
	        }
	        System.out.println();
	    }
	    System.out.println("y");
	    for(int i = 0; i < wid; i++){
	        y[i] = in.nextInt();
	    }
	    jthet = JThet(thet, x, y, len, wid);
	    theta(thet, x, y, len, wid);
	    //jthet = JThet(thet, x, y, len, wid);
	    while(jthet > LIMIT){
	        theta(thet, x, y, len, wid);
	        jthet = JThet(thet, x, y, len, wid);
	    }

	    for(int i = 0; i < wid; i++){
	        System.out.println(y[i] + "--");
	        System.out.println(HThtX(thet, x[i], len));
	        System.out.println();
	    }
	    int[][] test ={{0,  18,  10,   4,  13,   3,   1,24,  30,  26,  34,  39,  38,  24},
	    {14,  17,   9,  18,  17,   7,  16,38,  20,  25,  29,  38,  40,  32},
	    {7,10,10,3,0,10,8,31,  27,  37,  31,  33,  34,  20},
	    {7,  12,  180,  109,   9,  14,  19,49, 70,  85,  60,  91,  140,  42},
	    {58,  34,  50,  39,  77,  39,  37, 49,  70,  35,  60,  81,  40,  32},
	    {7,  1,  1,  1,   9,  4,  9,1,   6,  10,  6,   8,  10,  1}};
	    for(int i = 0; i < 6; i++){
	       System.out.println(HThtX(thet, test[i], len));
	    }
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
		for(int i =0 ; i < trainData.get(8+"").size(); i++)
			testData.add(trainData.get(8+"").get(i));
//		testData.add("兴奋");
//		testData.add("快乐");
//		testData.add("喜悦");
//		testData.add("愉快");
//		testData.add("悲伤");
//		testData.add("悲痛");
//		testData.add("悲惨 ");
//		testData.add("悲惨 ");
		keyWord.add("兴奋");
		keyWord.add("快乐");
		keyWord.add("喜悦");
		keyWord.add("愉快");
		keyWord.add("悲伤");
		keyWord.add("悲痛");
		keyWord.add("悲惨 ");
		keyWord.add("悲惨 ");
//		testData.add("由此");
//		testData.add("由此");
//		testData.add("由此");
//		testData.add("由此");
//		testData.add("由此");
		
//		keyWord.add("由此");
//		keyWord.add("由此");
//		keyWord.add("由此");
//		keyWord.add("由此");
//		keyWord.add("由此");
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		logical l = new logical();
		l.set();
		l.run();
		if(l.getResult()){
			System.out.println("True");
		}else{
			System.out.println("False");
		}
		
	}

}
