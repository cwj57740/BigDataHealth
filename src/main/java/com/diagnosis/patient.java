package com.diagnosis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class patient {
	//得到病人信息
	public Map getPatientInfo(){
		Map patientInfo=new HashMap();
		Scanner s=new Scanner(System.in);
		System.out.println("请输入您的姓名：");
		String name=s.next();
		System.out.println("请输入您的地址：");
		String address=s.next();
		System.out.println("请输入您的出生年月：");
		String birth=s.next();
		System.out.println("请输入您的登录密码：");
		String password=s.next();
		MapDistance mapDis=new MapDistance();
		//将地址转换为经纬度
		Map<String,Double> addressMap=mapDis.getLngAndLat(address);
		patientInfo.put("name", name);
		patientInfo.put("longitude", addressMap.get("lng"));
		patientInfo.put("latitude", addressMap.get("lat"));
		return patientInfo;
	}
	//插入病人信息
	public int inputPatientInfo(Map patientInfo,Connection conn)
	{
		String sql;
		int patient_id=-1;
		//int patient_id=(int)patientInfo.get("patient_id");
		String name=(String)patientInfo.get("name");
		double longitude=(double)patientInfo.get("longitude");
		double latitude=(double)patientInfo.get("latitude");
		String password=(String)patientInfo.get("password");
		sql="insert into patient_info(name,longitude,latitude,password)"
				+"values('"+name+"','"+longitude+"','"+latitude+"','"+password+"')";
		mySql ml=new mySql();
		ml.modifyData(conn, sql);
		sql="select * from patient_info where name='"+name+"' and longitude='"+longitude+"' and latitude='"+latitude+"'";
		ResultSet rs=ml.selectData(conn, sql);
		try {
			if(rs.next())
			{
				patient_id=rs.getInt("patient_id");
				String p_name=rs.getString("name");
				double p_lng=rs.getDouble("longitude");
				double	p_lat=rs.getDouble("latitude");
				System.out.println("ID："+patient_id);
				System.out.println("姓名："+p_name);
				System.out.println("经度："+p_lng);
				System.out.println("纬度："+p_lat);
				System.out.println("请牢记您的ID，下次登录时使用：");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return patient_id;
	}
	//返回就诊信息
	public Map getMedicalRecords(Map diagnosis_info,int hospital_id)
	{
		Map medical_records=new HashMap();
		
		Date d = new Date();  
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	    String medical_time = sdf.format(d); 
		medical_records.put("patient_id", (int)diagnosis_info.get("patient_id"));
		medical_records.put("disease_id", (int)diagnosis_info.get("disease_id"));
		medical_records.put("hospital_id", hospital_id);
		medical_records.put("medical_time", medical_time);
		return medical_records;
		
	}
	//插入就诊信息
	public int inputMedicalRecords(Map medical_records,Connection conn)
	{
		String sql;
		int patient_id=(int)medical_records.get("patient_id");
		int disease_id=(int)medical_records.get("disease_id");
		int hospital_id=(int)medical_records.get("hospital_id");
		String medical_time=(String)medical_records.get("medical_time");
		sql="insert into medical_records (patient_id,disease_id,hospital_id,medical_time)"
				+"values('"+patient_id+"','"+disease_id+"','"+hospital_id+"','"+medical_time+"')";
		mySql ml=new mySql();
		int result=ml.modifyData(conn, sql);
		System.out.println("医疗档案信息更新完毕！");
		return result;
	}
	/*//得到评价信息
	public Map getEvaluateInfo(Map medical_records){
		Map evaluateInfoMap=new HashMap<>();
		evaluateInfoMap.putAll(medical_records);
		
		Scanner s=new Scanner(System.in);
		System.out.println("请输入疗效水平得分（1-10）：");
		int effect=s.nextInt();
		System.out.println("请输入收费水平得分（1-10）：");
		int charge=s.nextInt();
		System.out.println("请输入服务态度水平等分（1-10）：");
		int attitude=s.nextInt();
		evaluateInfoMap.put("effect", effect);
		evaluateInfoMap.put("charge", charge);
		evaluateInfoMap.put("attitude", attitude);
		
		return evaluateInfoMap;
	}
	public int insertEvaluateInfo(Map evaluateInfoMap,Connection conn){
		String sql;
		int effect=(int)evaluateInfoMap.get("effect");
		int charge=(int)evaluateInfoMap.get("charge");
		int attitude=(int)evaluateInfoMap.get("attitude");
		int patient_id=(int)evaluateInfoMap.get("patient_id");
		int disease_id=(int)evaluateInfoMap.get("disease_id");
		int hospital_id=(int)evaluateInfoMap.get("hospital_id");
		String evaluate_time=(String)evaluateInfoMap.get("medical_time");
		sql="insert into medical_records (disease_id,hospital_id,effect,charge,attitude,evaluate_time,patient_id)"
				+"values('"+disease_id+"','"+hospital_id+"',,'"+effect+"','"+charge+"','"+attitude+"','"+evaluate_time+"','"+patient_id+"')";
		mySql ml=new mySql();
		int result=ml.modifyData(conn, sql);
		System.out.println("感谢您的评价！");
		return result;
	}*/
}
