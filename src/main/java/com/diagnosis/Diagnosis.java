package com.diagnosis;

import java.awt.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Diagnosis {
	//将查询症状做分词处理
	public Map<String,Integer> query_sentence(String symptom_text,Connection conn) throws SQLException{
		Segmentation segmentation=new Segmentation();
		ArrayList<String> sentence_list=segmentation.divide_text(symptom_text);
		String sentence="";
		Map<String,Integer> word_count=new HashMap<String,Integer>();
		for(int index=0;index<sentence_list.size();++index){
			sentence=sentence_list.get(index);
			ArrayList<String> word_list=segmentation.seg_sentence(sentence, conn);
			String word="";
			for(int i=0;i<word_list.size();++i){
				word=word_list.get(i);
				if(null==word_count.get(word)){
					word_count.put(word, 2);
				}
				else{
					int old_count=word_count.get(word);
					old_count+=2;//提高输入症状的权重
					word_count.put(word, old_count);
				}
			}
		}
		return word_count;
	}
	
	//计算相似度
	public Map<String, Double> compute_similarity(Map<String, Integer> word_count,Connection conn) throws SQLException{
		int num_disease=0;
		int symptom_num=0;
		Map<String, Integer> potential_diseaseMap=new HashMap<String,Integer>();
		String disease_ICD="";
		String disease_ICD_set="";
		Map<String, Double> disease_similarity=new HashMap<String,Double>();
		int row_count=0;
		String sql;
		mySql ml=new mySql();
		
		for(Map.Entry<String, Integer> entry : word_count.entrySet()){
			sql="select name from symptom_index where code='"+entry.getKey()+"'";
			ResultSet rs=ml.selectData(conn, sql);
			if(rs.next()){
				disease_ICD_set=rs.getString("name");
				boolean flag=false;//某种疾病是否处理完成
				for(int index=0;index<disease_ICD_set.length();++index){
					if(disease_ICD_set.charAt(index)!=' '){
						if(!flag){
							disease_ICD+=disease_ICD_set.charAt(index);
						}
					}
					else if(!flag){
						flag=true;
						sql="select symptom_num from complete_disease where ICD='"+disease_ICD+"'";
						ResultSet resultSet=ml.selectData(conn, sql);
						if(resultSet.next()){
							symptom_num=resultSet.getInt("symptom_num");
							potential_diseaseMap.put(disease_ICD, symptom_num);
						}
						disease_ICD="";
					}
					else{
						flag=false;
					}
				}
			}
		}
		num_disease=potential_diseaseMap.size();
		for(Map.Entry<String, Integer> entry :potential_diseaseMap.entrySet()){
			disease_ICD=entry.getKey();
			double similarity=0;
			for(Map.Entry<String, Integer> wordEntry : word_count.entrySet()){
				int query_word_weight=wordEntry.getValue();
				int word_in_disease=0;
				String str_word_in_disease="";
				int disease_type=0;
				sql="select name,type from symptom_index where code='"+wordEntry.getKey()+"'";
				ResultSet rs=ml.selectData(conn, sql);
				if(rs.next()){
					disease_ICD_set=rs.getString("name");
					disease_type=rs.getInt("type");
					int pos=disease_ICD_set.indexOf(disease_ICD);
					if(-1!=pos&&disease_ICD_set.charAt(pos+disease_ICD.length())==' '){
						pos=pos+disease_ICD.length()+1;
						while(pos<disease_ICD_set.length()&& disease_ICD_set.charAt(pos)!=' '){
							str_word_in_disease+=disease_ICD_set.charAt(pos);
							++pos;
						}
						word_in_disease=Integer.parseInt(str_word_in_disease);
						double idf_divisor=(double)num_disease/(double)disease_type;
						double idf=0;
						if(Math.abs(idf_divisor-1)<0.000000001){
							idf=1;
						}
						else{
							idf=Math.log10(idf_divisor)/Math.log10(2.0);
						}
						similarity=similarity+query_word_weight*word_in_disease*idf;
					}
				}
			}
			symptom_num=potential_diseaseMap.get(disease_ICD);
			disease_similarity.put(disease_ICD, similarity/(double)symptom_num);
			
		}
		return disease_similarity;
	} 
	
	//相似度排序
	public ArrayList<Map.Entry<String, Double>> similarity_sort(Map<String, Double> disease_similarity){
		ArrayList<Map.Entry<String, Double>> similarity_list=new ArrayList<Map.Entry<String, Double>>(disease_similarity.entrySet());
		
		Collections.sort(similarity_list, new Comparator<Map.Entry<String,Double>>(){
			public int compare(Map.Entry<String, Double> object1,Map.Entry<String,Double> object2){
				if(object1.getValue()<object2.getValue()){//降序
					return 1;
				}
				else{
					return -1;
				}
			}
		});
		
		return similarity_list;
	}
	
	//归一化相似度
	public void normalize_similarity(ArrayList<Map.Entry<String, Double>> similarity_list){
		if(similarity_list.size()<2){
			if(!similarity_list.isEmpty()){
				similarity_list.get(0).setValue(0.999999);
			}
		}
		else{
			double max=similarity_list.get(0).getValue();
			double min=similarity_list.get(similarity_list.size()-1).getValue();
			double denominator=max-min;
			
			for(int index=0;index<similarity_list.size();++index){
				double temp=similarity_list.get(index).getValue();
				temp=(temp-min)/denominator;
				temp=(temp+0.01)*0.99;
				temp=Math.floor(temp*10000+0.5)/100.0;
				similarity_list.get(index).setValue(temp);
			}
		}
		}
	
	//诊断
	public ArrayList<Map<String,String>> fullDiagnosis(String symptom,Connection conn) throws SQLException{
		
		Map<String, Integer> word_count=query_sentence(symptom, conn);
		Map<String, Double> disease_similarity=compute_similarity(word_count, conn);
		ArrayList<Map.Entry<String, Double>> similarity_list=similarity_sort(disease_similarity);
		normalize_similarity(similarity_list);
		ArrayList<Map<String,String>> list = display_result(similarity_list, conn);
		/*System.out.println("是否进行反馈查询");
		ArrayList<String> related_disease=mark_related_disease(similarity_list);
		similarity_list=feedback_query(related_disease, word_count, conn);
		display_result(similarity_list, conn);*/
		return list;
	}

	//完整诊断
	public ArrayList<Map.Entry<String, Double>> diagnosis(String symptom,Connection conn) throws SQLException{

		Map<String, Integer> word_count=query_sentence(symptom, conn);
		Map<String, Double> disease_similarity=compute_similarity(word_count, conn);
		ArrayList<Map.Entry<String, Double>> similarity_list=similarity_sort(disease_similarity);
		normalize_similarity(similarity_list);
		display_result(similarity_list, conn);
		/*System.out.println("是否进行反馈查询");
		ArrayList<String> related_disease=mark_related_disease(similarity_list);
		similarity_list=feedback_query(related_disease, word_count, conn);
		display_result(similarity_list, conn);*/
		return similarity_list;
	}


	//优化诊断
	public  ArrayList<Map.Entry<String, Double>> optimizeDiagnosis(ArrayList<Map.Entry<String, Double>> diagnosis_result,String symptom,Connection conn) throws SQLException{
		ArrayList<String> related_disease=mark_related_disease(diagnosis_result);
		Map<String, Integer> word_count=query_sentence(symptom, conn);
		ArrayList<Map.Entry<String, Double>> similarity_list=feedback_query(related_disease, word_count, conn);
		display_result(similarity_list, conn);
		return similarity_list;
	}
	//输出结果
	public ArrayList<Map<String,String>> display_result(ArrayList<Map.Entry<String, Double>> similarity_list,Connection conn) throws SQLException{
		mySql ml=new mySql();
		String sql;
		ArrayList<Map<String,String>> list = new ArrayList<Map<String,String>>();
		for (int index = 0; index < similarity_list.size()&&index<10; index++) {
			sql="select name from complete_disease where ICD='"+similarity_list.get(index).getKey()+"'";
			ResultSet rs=ml.selectData(conn, sql);
			if(rs.next()){
				Map<String,String> map = new HashMap<String,String>();
				map.put("index",Integer.toString(index));
				map.put("name",rs.getString("name"));
				map.put("no",similarity_list.get(index).getKey());
				map.put("similarity",similarity_list.get(index).getValue().toString());
				list.add(map);
				System.out.println("序号："+index+"疾病名称："+rs.getString("name")+"      疾病编号"+similarity_list.get(index).getKey()+"      相似度："+similarity_list.get(index).getValue());
			}
		}
		return list;
	}
	//获得反馈信息
	public ArrayList<String> mark_related_disease(ArrayList<Map.Entry<String, Double>> similarity_list){
		ArrayList<Integer> related_disease_numArrayList=new ArrayList<>();
		System.out.println("请输入可能相关的疾病序号（-1表示输入结束”）:");
		Scanner scanner=new Scanner(System.in);
		int num;
		ArrayList<String> related_disease_ICDArrayList=new ArrayList<>();
		while(scanner.hasNext()){
			num=scanner.nextInt();
			if(-1==num){
				break;
			}
			related_disease_numArrayList.add(num);
		}
		
		for(int index=0;index<similarity_list.size();++index){
			if(related_disease_numArrayList.contains(index)){
				related_disease_ICDArrayList.add(similarity_list.get(index).getKey());
				
			}
		}
		return related_disease_ICDArrayList;
	}
	
	//反馈查询
	public ArrayList<Map.Entry<String, Double>> feedback_query(ArrayList<String> related_disease,Map<String, Integer> word_count,Connection conn) throws SQLException{
		String sql;
		mySql ml=new mySql();
		for(int index=0;index<related_disease.size();++index){
			Map<String, Integer> temp_word_count=new HashMap<>();
			sql="select symptom from complete_disease where ICD= '"+related_disease.get(index)+"'";
			ResultSet rs=ml.selectData(conn, sql);
			if(rs.next()){
				temp_word_count=query_sentence(rs.getString("symptom"), conn);
			}
			for(Map.Entry<String, Integer> entry : temp_word_count.entrySet()){
				if(null!=word_count.get(entry.getKey())){
					int temp=word_count.get(entry.getKey());
					temp=temp+entry.getValue()/2;
					word_count.put(entry.getKey(), temp);
				}
				else{
					word_count.put(entry.getKey(), entry.getValue()/2);
				}
			}
		}
		Map<String, Double> disease_similarity=compute_similarity(word_count, conn);
		ArrayList<Map.Entry<String, Double>> similarity_list=similarity_sort(disease_similarity);
		normalize_similarity(similarity_list);
		return similarity_list;
		//display_result(similarity_list, conn);
	}
}

