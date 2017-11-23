package com.diagnosis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class disease {
	//输入疾病信息
	public int inputDiseaseInfo(Map diseaseInfo,Connection conn)
	{
		String sql;
		int disease_id=(int)diseaseInfo.get("disease_id");
		String disease_name=(String)diseaseInfo.get("disease_name");
		int level=(int)diseaseInfo.get("level");
		sql="insert into disease_info(disease_id,disease_name,level)"
				+"values('"+disease_id+"','"+disease_name+"','"+level+"')";
		mySql ml=new mySql();
		int result=ml.modifyData(conn, sql);
		return result;
	}
	//周期性更新索引表
	public void rebuild_symptom_index(Connection conn) throws SQLException{
		mySql ml=new mySql();
		String sql;
		String symptom;
		String disease_ICD;
		sql="select symptom,ICD from complete_disease where insert_flag=false limit 100,100";
		int counter=0;
		ResultSet rs=ml.selectData(conn, sql);
		while(rs.next()){
			System.out.println(++counter);
			symptom=rs.getString("symptom");
			disease_ICD=rs.getString("ICD");
			build_symptom_index(symptom, disease_ICD, conn);
			sql="update complete_disease set insert_flag=true where ICD='"+disease_ICD+"'";
			ml.modifyData(conn, sql);
		}
		
		//sql="update complete_disease set insert_flag=true where insert_flag=false";
		//ml.modifyData(conn, sql);
	}
	
	//症状索引表
	public int insert_index(String word,String name,Connection conn,int symptom_num) throws SQLException{
		int count=1;
		String str_count="";
		String new_name=name+" "+count;
		int type=1;
		
		mySql ml=new mySql();
		String sql="select name, type from symptom_index where code='"+word+"'";
		ResultSet rs=ml.selectData(conn, sql);
		if(rs.next()){
			String old_name=rs.getString("name");
			type=rs.getInt("type");
			int pos=old_name.indexOf(name);
			if(-1!=pos && old_name.charAt(pos+name.length())==' '){
				String front=old_name.substring(0,pos);
				String behind="";
				int index=pos+name.length()+1;
				str_count="";
				while(index<old_name.length() && old_name.charAt(index)!=' '){
					str_count+=old_name.charAt(index);
					++index;
				}
				if(index<old_name.length()){
					behind=old_name.substring(index);
				}
				
				count=Integer.parseInt(str_count);
				++count;
				old_name=front+name+" "+count+behind;
			}
			else{
				count=1;
				++type;
				old_name=old_name+" "+name+" "+count;
				++symptom_num;
			}
			sql="update symptom_index set name='"+old_name+"',type='"+type+"' where code='"+word+"'";
			ml.modifyData(conn, sql);
		}
		else{
			sql="insert into symptom_index(code,name,type) values('"+word+"','"+new_name+"','"+type+"')";
			ml.modifyData(conn, sql);
			++symptom_num;
		}
		return symptom_num;
	}
	//反馈缓存表
	public void insert_cache(String ICD,String word,Connection conn) throws SQLException{
		int type;
		String sql="select type from feedback_cache where ICD='"+ICD+"' and code='"+word+"'";
		mySql ml=new mySql();
		ResultSet rs=ml.selectData(conn, sql);
		if(!rs.next()){
			sql="insert into feedback_cache(ICD,code,type) values('"+ICD+"','"+word+"',1)";
			ml.modifyData(conn, sql);
		}
		else {
			type=rs.getInt("type");
			++type;
			sql="update feedback_cache set type='"+type+"' where ICD='"+ICD+"' and code='"+word+"'";
			ml.modifyData(conn, sql);
		}
	}
	//建立症状索引表
	public void build_symptom_index(String symptom,String disease_ICD,Connection conn) throws SQLException{
		String sql="";
		mySql ml=new mySql();
		
		Segmentation segmentation=new Segmentation();
		ArrayList<String> sentence_list=segmentation.divide_text(symptom);
		int symptom_num=0;
		
		for(int index=0;index<sentence_list.size();++index){
			String sentence=sentence_list.get(index);
			ArrayList<String> word_list=new ArrayList<String>();
			word_list=segmentation.seg_sentence(sentence, conn);
			String word="";
			//对所有的句子分完词，统计词频之后再一起插入是不是更合理
			for(int word_index=0;word_index<word_list.size();++word_index){
				word=word_list.get(word_index);
				symptom_num=insert_index(word,disease_ICD,conn,symptom_num);
				insert_cache(disease_ICD,word,conn);
			}
		}
		sql="update complete_disease set symptom_num='"+symptom_num+"' where ICD='"+disease_ICD+"'";
		ml.modifyData(conn, sql);
	}
}
