package com.vista;

import com.vista.ChineseSpliter;
import com.vista.ClassConditionalProbability;
import com.vista.PriorProbability;
import com.vista.TrainingDataManager;

import database.DBHelper;
import database.DataBaseConnection;

import com.vista.StopWordsHandler;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * 朴素贝叶斯分类器
 */
public class BayesClassifier {
	private TrainingDataManager tdm;// 训练集管理器
	private String trainnigDataPath;// 训练集路径
	private static double zoomFactor = 10.0f;
	static DBHelper db1 = null;
	static ResultSet ret = null;
	static String backline;
	static String jaja;

	static String ufname = null;
	static String ulname = null;
	static String udate = null;

	static String diseaseId = null;
	static String diseaseT = null;
	static String diseaseC = null;
	static String responseId = null;

	static ArrayList disease_id = new ArrayList();;
	static ArrayList disease_Title = new ArrayList();;
	static ArrayList disease_Content = new ArrayList();;

	// static String[] diseaseTitle = null;
	// static String[] diseaseContent = null;

	/**
	 * 默认的构造器，初始化训练集
	 */
	public BayesClassifier() {
		tdm = new TrainingDataManager();
	}

	/**
	 * 计算给定的文本属性向量X在给定的分类Cj中的类条件概率 <code>ClassConditionalProbability</code>连乘值
	 * 
	 * @param X
	 *            给定的文本属性向量
	 * @param Cj
	 *            给定的类别
	 * @return 分类条件概率连乘值，即<br>
	 */
	double calcProd(String[] X, String Cj) {
		double ret = 1.0d;
		// 类条件概率连乘
		for (int i = 0; i < X.length; i++) {
			String Xi = X[i];
			// 因为结果过小，因此在连乘之前放大10倍，这对最终结果并无影响，因为我们只是比较概率大小而已
			ret *= ClassConditionalProbability.calculatePxc(Xi, Cj) * zoomFactor;
		}
		// 再乘以先验概率
		ret *= PriorProbability.calculatePc(Cj);
		return ret;
	}

	/**
	 * 去掉停用词
	 * 
	 * @param text
	 *            给定的文本
	 * @return 去停用词后结果
	 */
	public String[] DropStopWords(String[] oldWords) {
		Vector<String> v1 = new Vector<String>();
		for (int i = 0; i < oldWords.length; ++i) {
			if (StopWordsHandler.IsStopWord(oldWords[i]) == false) {// 不是停用词
				v1.add(oldWords[i]);
			}
		}
		String[] newWords = new String[v1.size()];
		v1.toArray(newWords);
		return newWords;
	}

	/**
	 * 对给定的文本进行分类
	 * 
	 * @param text
	 *            给定的文本
	 * @return 分类结果
	 */
	@SuppressWarnings("unchecked")
	// responseIn 用户反馈的关键词
	public List<ClassifyResult> classify(String text) {
		String[] terms = null;
		// terms 原病例的关键词集合
		terms = ChineseSpliter.split(text, " ").split(" ");// 中文分词处理(分词后结果可能还包含有停用词）
		terms = DropStopWords(terms);// 去掉停用词，以免影响分类
	/*	for (int i = 0; i < terms.length; i++) {

			System.out.println(terms[i]);
		}*/

		String[] Classes = new String[disease_id.size()];
		for (int i = 0; i < disease_id.size(); i++) {
			Classes[i] = (String) disease_Title.get(i);

		}

		double probility = 0.0d;
		List<ClassifyResult> crs = new ArrayList<ClassifyResult>();// 分类结果
		//System.out.println("Classes.length: "+Classes.length);
		for (int i = 0; i < Classes.length; i++) {
			String Ci = Classes[i];// 第i个分类
			probility = calcProd(terms, Ci);// 计算给定的文本属性向量terms在给定的分类Ci中的分类条件概率
			// 保存分类结果
			ClassifyResult cr = new ClassifyResult();
			cr.classification = Ci;// 分类
			cr.probility = probility;
			// System.out.println("In process...."+Ci);
			// System.out.println(Ci + "：" + cr.probility);
			crs.add(cr);
		}
		// 对最后概率结果进行排序
		java.util.Collections.sort(crs, new Comparator() {
			public int compare(final Object o1, final Object o2) {
				final ClassifyResult m1 = (ClassifyResult) o1;
				final ClassifyResult m2 = (ClassifyResult) o2;
				if (m1.probility < m2.probility) {
					return 1;

				} else if (m1.probility == m2.probility) {
					return 0;
				} else {
					return -1;
				}
			}
		});
		
		List<ClassifyResult> returnResult=new ArrayList<ClassifyResult>();
		for(int i=0;i<crs.size()&&i<5;++i)
		{
			returnResult.add(crs.get(i));
		}
		return returnResult;
		// return "dasdas";
	}

	

	

	@SuppressWarnings("null")
	public static void selectDisease(String sql) {
		// TODO Auto-generated method stub

//		Long startTIme = System.currentTimeMillis();
//		System.out.println(sql);
		int number = 0;
		db1 = new DBHelper(sql);// 创建DBHelper对象
		try {
			ret = db1.pst.executeQuery();// 执行语句，得到结果集
			while (ret.next()) {
				diseaseId = ret.getString("disease_id");
				diseaseT = ret.getString("title");
				diseaseC = ret.getString("content");
				// System.out.println(diseaseId + "\t" + diseaseT + "\t" +
				// diseaseC);
				number++;
				setValue(diseaseId, diseaseT, diseaseC, number);
			} // 显示数据

			ret.close();
			db1.close();// 关闭连接
//			Long endTime = System.currentTimeMillis();
//			System.err.println(endTime - startTIme);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void setValue(String id, String a, String b, int num) {
		disease_id.add(id);
		disease_Title.add(a);
		disease_Content.add(b);

	}
	public int getDiseaseId(String title){
		String[] DiseaseText = new String[disease_id.size()];
		int Did = 0;
		for (int i = 0; i < disease_id.size(); i++) {
			DiseaseText[i] = (String) disease_Title.get(i);

			if (title.equals(DiseaseText[i])) {
				Did = i;
			}
		}
		
		
		return Did;
	}
	
	public String getDiseaseText(String title){
		String DiseaseText = "";
		for (int i = 0; i < disease_id.size(); i++) {
			DiseaseText = DiseaseText+(String) disease_Content.get(i);

		}
//		for (int t = 0; t < DiseaseText.length; t++) {
//			String string = DiseaseText[t];
//			System.out.println("getDiseaseText:" + string);
//
//		}
		
		return DiseaseText;
	}
	@SuppressWarnings("null")
	//public static void main(String[] args){ 
	public  List<ClassifyResult> diagnosis() {

		String collectDisease = "SELECT * FROM disease_info";
		selectDisease(collectDisease);

		
		String disease_describe;	
		Scanner s=new Scanner(System.in);
		System.out.println("请输入症状描述：");
		disease_describe=s.next();
		DataBaseConnection dataBaseConnection = null;
		
		BayesClassifier classifier = new BayesClassifier();// 构造Bayes分类器
		//System.out.println(disease_describe);
		List<ClassifyResult> result=new ArrayList<ClassifyResult>();
		result = classifier.classify(disease_describe);// 进行分类
		System.out.println("诊断结果：");
		for(int i=0;i<result.size();++i)
		{
			System.out.println(result.get(i).classification);
		}
		return result;

	}

}