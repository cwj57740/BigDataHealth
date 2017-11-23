package com.vista;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import database.DBHelper;

/**
 * 训练集管理器
 */

public class TrainingDataManager {
	private String[] traningFileClassifications;// 训练语料分类集合
	private File traningTextDir;// 训练语料存放目录
	private static String defaultPath = "D:\\disease";
	static DBHelper db1 = null;
	static ResultSet ret = null;
	static ArrayList disease_id = new ArrayList();;
	static ArrayList disease_Title = new ArrayList();;
	static ArrayList disease_Content = new ArrayList();;
	static String diseaseId = null;
	static String diseaseT = null;
	static String diseaseC = null;
	static boolean flag = true;

	public TrainingDataManager() {
		traningTextDir = new File(defaultPath);
		if (!traningTextDir.isDirectory()) {
			throw new IllegalArgumentException("训练语料库搜索失败！ [" + defaultPath + "]");
		}
		this.traningFileClassifications = traningTextDir.list();

		String collectDisease = "SELECT * FROM disease_info";
		if (flag) {
			selectDisease(collectDisease);
		}
		
		// for (int i = 0; i < traningTextDir.list().length; i++) {
		//
		// System.err.println("woro:"+traningFileClassifications[i]);
		// }
	}

	/**
	 * 返回训练文本类别，这个类别就是目录名
	 * 
	 * @return 训练文本类别
	 */
	public String[] getTraningClassifications() {
		return this.traningFileClassifications;
	}

	/**
	 * 根据训练文本类别返回这个类别下的所有训练文本路径（full path）
	 * 
	 * @param classification
	 *            给定的分类
	 * @return 给定分类下所有文件的路径（full path）
	 */
	public String getFilesPath(String classification) {
		File classDir = new File(traningTextDir.getAbsolutePath() + File.separator + classification);
		// System.out.println(classification);
		// String[] ret = classDir.list();
		// System.out.println(traningTextDir.getPath( ) +File.separator
		// +classification);
		// System.out.println("ret.length: "+classDir.list().length);
		String ret = traningTextDir.getAbsolutePath() + File.separator + classification;
		// for (int i = 0; i < ret.length; i++)
		// {
		// ret[i] = traningTextDir.getPath() +File.separator +ret[i];
		// }
		return ret;
	}

	/**
	 * 返回给定路径的文本文件内容
	 * 
	 * @param filePath
	 *            给定的文本文件路径
	 * @return 文本内容
	 * @throws java.io.FileNotFoundException
	 * @throws java.io.IOException
	 */
	public static String getText(String filePath) throws FileNotFoundException, IOException {

		InputStreamReader isReader = new InputStreamReader(new FileInputStream(filePath), "GBK");
		BufferedReader reader = new BufferedReader(isReader);
		String aline;
		StringBuilder sb = new StringBuilder();

		while ((aline = reader.readLine()) != null) {
			sb.append(aline + " ");
		}
		isReader.close();
		reader.close();
		return sb.toString();
	}

	/**
	 * 返回训练文本集中所有的文本数目
	 * 
	 * @return 训练文本集中所有的文本数目
	 */
	public int getTrainingFileCount() {
		int ret = 0;
		for (int i = 0; i < traningFileClassifications.length; i++) {
			ret += getTrainingFileCountOfClassification(traningFileClassifications[i]);
		}
		return ret;
	}

	/**
	 * 返回训练文本集中在给定分类下的训练文本数目
	 * 
	 * @param classification
	 *            给定的分类
	 * @return 训练文本集中在给定分类下的训练文本数目
	 */
	public int getTrainingFileCountOfClassification(String classification) {
		File classDir = new File(traningTextDir.getPath() + File.separator + classification);
		return 1;
	}

	/**
	 * 返回给定分类中包含关键字／词的训练文本的数目
	 * 
	 * @param classification
	 *            给定的分类
	 * @param key
	 *            给定的关键字／词
	 * @return 给定分类中包含关键字／词的训练文本的数目
	 */
	public int getCountContainKeyOfClassification(String classification, String key) {
		int ret = 0;
//		System.err.println("yyyy:" + classification);

		String DiseaseText = "";
		String DiseaseTit = "";
		int num = 0;
		for (int i = 1; i < disease_id.size(); i++) {
			if (classification.equals(disease_Title.get(i))) {
				num = i;
//				System.err.println("num:"+(num+1));
			}
		}
		
		DiseaseText = (String) disease_Content.get(num);
		if (DiseaseText.contains(key)) {
			ret++;
		}
		return ret;
	}

	public static void selectDisease(String sql) {
		// TODO Auto-generated method stub

		Long startTIme = System.currentTimeMillis();
		System.out.println(sql);
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
			System.out.println("number :" + number);
			ret.close();
			db1.close();// 关闭连接
			flag = false;
			Long endTime = System.currentTimeMillis();
			System.err.println(endTime - startTIme);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void setValue(String id, String a, String b, int num) {
		disease_id.add(id);
		disease_Title.add(a);
		disease_Content.add(b);

	}
}