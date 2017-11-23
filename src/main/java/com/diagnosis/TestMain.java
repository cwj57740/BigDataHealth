package com.diagnosis;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import kdTree.EuclideanDistance;
import kdTree.KDTree;
import kdTree.KeyDuplicateException;
import kdTree.KeySizeException;

import com.vista.BayesClassifier;
import com.vista.ClassifyResult;



public class TestMain {

	/**
	 * @param args
	 * @throws SQLException 
	 * @throws KeyDuplicateException 
	 * @throws KeySizeException 
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws KeySizeException, KeyDuplicateException, SQLException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		mySql ml=new mySql();
		Connection conn=ml.getConnection();
		
		//用户注册
		patient pt=new patient();
		System.out.println("注册新用户：");
		pt.inputPatientInfo(pt.getPatientInfo(), conn);
		//getPatientInfo()函数是的作用是读取用户的注册信息，
		//返回值是Map，存储的是用户的某个属性与属性值这样的键值对，比如<"name",张三>
		//inputPatientInfo函数的作用是将注册信息存到数据库中
		
		
		//医院注册
		hospital h=new hospital();
		System.out.println("注册医院信息：");
		h.inputInfo(h.getInfo(), conn);
		//getInfo()函数的作用是读取医院的注册信息，返回值也是Map，存的也是某个属性与属性值这样的键值对
		//inputInfo函数的作用是将注册信息存到数据库中
		
		
		//疾病诊断
		System.out.println("疾病诊断：");
		Diagnosis diagnosis=new Diagnosis();
		System.out.println("请输入您的症状信息：");
		Scanner s=new Scanner(System.in);
		String symptom=s.next();
		ArrayList<Map.Entry<String, Double>>diagnosis_result=diagnosis.diagnosis(symptom, conn);
		//diagnosis函数输入参数是病人的症状描述symptom，数据库的接入参数conn；其中symptom是在诊断界面中由用户输入的
		//返回值是诊断结果，也就是可能的疾病与其相对相似度，类型是ArrayList<Map.Entry<String, Double>>，比如<"感冒的ICD编号"，89.1>
		
		
		
		//优化诊断
		System.out.println("疾病诊断：");
		diagnosis.optimizeDiagnosis(diagnosis_result, symptom, conn);
		//optimizeDiagnosis函数的作用是根据用户对前面诊断结果的反馈，进行二次诊断
		//参数是diagnosis_result-前面诊断结果，也就是diagnosis的返回值，symptom-输入的症状信息，conn-数据库接入参数
		//在optimizeDiagnosis中调用了mark_related_disease，用来获得用户的反馈信息，也就是得到可能的疾病的序号
		//返回值是优化诊断结果，也就是可能的疾病与其相对相似度，类型是ArrayList<Map.Entry<String, Double>>，比如<"感冒的ICD编号"，89.1>
		
		
		
		//医院推荐
		System.out.println("医院推荐：");
		int patient_id=3000;
		int disease_id=5;
		double range=15;
		evaluater ev=new evaluater();
		ArrayList<Map.Entry<Integer, Double>>  CFScoreMap=new ArrayList<>();
		CFScoreMap=ev.CFEvaluator(patient_id,disease_id, range, conn);
		
		for(int index=0;index<CFScoreMap.size();++index){
			System.out.println("医院编号:"+CFScoreMap.get(index).getKey()+";评分:"+CFScoreMap.get(index).getValue());
		}
		//CFEvaluator函数的作用是根据前面的诊断结果为用户推荐一些医院
		//输入参数patient_id表示用户的id，该参数可以在登录界面获取
		//输入参数disease_id表示用户在诊断结果中最终选择的疾病编号，该参数可以在诊断结果界面获取
		//输入参数range表示能推荐的医院距离用户的最大距离，该参数目前默认15
		//输入参数conn表示数据库接入参数
		//返回值为ArrayList<Map.Entry<Integer, Double>> 类型的，表示推荐的医院及其综合评分，比如<"哈尔滨工业大学校医院"，4.3>
		
		
		//更新诊疗记录
		System.out.println("更新诊疗记录：");
		Map<Integer,Map> m=new HashMap<Integer,Map>();
		Map diagnosis_info=new HashMap();
		diagnosis_info.put("disease_id", disease_id);
		diagnosis_info.put("patient_id", patient_id);
		int hospital_id=17;
		//hospital_id可以从推荐界面中获取
		pt.inputMedicalRecords(pt.getMedicalRecords(diagnosis_info, hospital_id), conn);
		//inputMedicalRecords将相关的诊断记录存入数据库中
		
		
		//评价医院
		System.out.println("评价医院：");
		ev.inputEvaluate_turnover(ev.getEvaluate(diagnosis_info, hospital_id), conn);
		//inputEvaluate_turnover将用户的评价信息存入数据库
		//getEvaluate函数作用是获取用户的评价信息
	}

}
