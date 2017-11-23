package com.diagnosis;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import java.sql.Connection;

public class Segmentation {
	private static final int WIN_LENGTH=5;
//划分文本
public ArrayList<String> divide_text(String text){
	ArrayList<String> sentence_list=new ArrayList<String>();
	String sentence="";
	for(int index=0;index<text.length();++index){
		if((text.charAt(index)>=0x4E00) && (text.charAt(index)<=0x9Fa5)){
			//是汉字
			sentence+=text.substring(index,index+1);
		}
		else{
			//未识别的符号,即不是汉字
			if(!sentence.isEmpty()){
				sentence_list.add(sentence);
				sentence="";
			}
			}
		}
	if(!sentence.isEmpty()){
		sentence_list.add(sentence);
	}
	return sentence_list;	
	}

	public ArrayList<String> seg_sentence(String sentence,Connection conn) throws SQLException{
		ArrayList<String> word_list=new ArrayList<String>();
		int length=WIN_LENGTH;
		String windows="";
		
		while(sentence.length()>0){
			if(sentence.length()>=length){
				windows=sentence.substring(sentence.length()-length,sentence.length());
			}
			else {
				windows=sentence;
			}
		
			
			boolean flag=false;
			String sql="";
			
			switch (length) {
			case 5:
				sql="select code from word_five where word='"+windows+"'";
				break;
			case 4:
				sql="select code from word_four where word='"+windows+"'";
				break;
			case 3:
				sql="select code from word_three where word='"+windows+"'";
				break;
			case 2:
				sql="select code from word_two where word='"+windows+"'";
				break;
			case 1:
				sql="select code from word_one where word='"+windows+"'";
				break;
			default:
				break;
			}
			
			mySql ml=new mySql();
			ResultSet rs=ml.selectData(conn, sql);
			if(rs.next()){
				word_list.add(rs.getString("code"));
				flag=true;
			}
			if(flag){
				sentence=sentence.substring(0,sentence.length()-windows.length());
				length=WIN_LENGTH;
			}
			else {
				--length;
				if (length<=0) {
					sentence=sentence.substring(0,sentence.length()-1);
					length=WIN_LENGTH;
				}
			}
		}
		return word_list;
	}
}
