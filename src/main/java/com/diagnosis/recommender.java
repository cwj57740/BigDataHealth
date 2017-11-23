package com.diagnosis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class recommender {
	private static double CERTAINTY_THRESHOLD=0.5;
	private static int MAX_LEVEL=2;
	private static double DEFAULT_DISTANCE=4000.0;
	private static int TOP_K=20;
	private static double MAX_DISTANCE=40000000.0;
	private static double DEFAULT_NEAR=3000.0;
	//默认推荐的医院
	public Map<Integer,Map> initialRecommendHospital(Map diagnosis_info,Connection conn)
	{
		double certainty=(double)diagnosis_info.get("certainty");
		int disease_id=(int)diagnosis_info.get("disease_id");
		int patient_id=(int)diagnosis_info.get("patient_id");
		int level=0;
		level=computeDiseaseLevel(disease_id,certainty,conn);
		//System.out.println("level:"+level);
		//
		diagnosis_info.put("hospital_level", level);
		double range_distance=DEFAULT_DISTANCE;
		diagnosis_info.put("distance", range_distance);
		Map<Integer,Map> result_map=getHospitalData(diagnosis_info,conn);
		//

		comprehensiveScore(result_map,diagnosis_info,conn);
		//
		
		
		return result_map;
	}
	//扩大推荐的范围
	public Map<Integer,Map> extendRange(Map diagnosis_info,Connection conn)
	{
		double distance=(double)diagnosis_info.get("distance");
		distance*=1.5;
		Map<Integer,Map> result=new HashMap<Integer,Map>();
		diagnosis_info.put("distance", distance);
		result=getHospitalData(diagnosis_info,conn);
		comprehensiveScore(result,diagnosis_info,conn);
		return result;
	}
	//减小推荐的范围
	public Map<Integer,Map> reduceRange(Map diagnosis_info,Connection conn)
	{
		double distance=(double)diagnosis_info.get("distance");
		distance*=0.75;
		Map<Integer,Map> result=new HashMap<Integer,Map>();
		diagnosis_info.put("distance", distance);
		result=getHospitalData(diagnosis_info,conn);
		comprehensiveScore(result,diagnosis_info,conn);
		return result;
	}
	//不考虑距离因素，大范围的推荐
	public List<Integer> findTopHospital(int disease_id,Connection conn)
	{
		String sql;
		sql="select hospital_id from evaluate_global where disease_id='"+disease_id+"' order by score desc";
		mySql ml=new mySql();
		int counter=0;
		
		ResultSet rs=ml.selectData(conn, sql);
		
		List<Integer> result=new ArrayList<Integer>();
		
		try {
			while(rs.next()&&counter<TOP_K)
			{
				result.add(rs.getInt("hospital_id"));
				++counter;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	//计算不考虑距离因素推荐的医院距离患者的远近，返回医院以及到你的距离
	public Map<Integer,Double> computeTopHospitalDistance(List<Integer> top_hospital,Map diagnosis_info,Connection conn)
	{
		Map<Integer,Double> result_map=new HashMap<Integer,Double>();
		String sql;
		mySql ml=new mySql();
		ResultSet rs;
		int patient_id=(int)diagnosis_info.get("patient_id");
		sql="select longitude,latitude from patient_info where patiend_id='"+patient_id+"'";
		rs=ml.selectData(conn, sql);
		double longitude=0;
		double latitude=0;
		try {
			if(rs.next())
			{
				longitude=rs.getDouble("longitude");
				latitude=rs.getDouble("latitude");
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		for(int i=0;i<top_hospital.size();++i)
		{
			int hospital_id=top_hospital.get(i);
			sql="select longitude, latitude from hospital_info where hospital_id='"+hospital_id+"'";
			rs=ml.selectData(conn, sql);
			double distance=MAX_DISTANCE;
			try {
				if(rs.next())
				{
					MapDistance md=new MapDistance();
					distance=md.getDistance(latitude, longitude, rs.getDouble("latitude"), rs.getDouble("longitude"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			result_map.put(hospital_id, distance);
		}
		return result_map;
	}
	
	//找到附近患同种病的人
	public Map<Integer,Map<Integer,Integer>> findNearPatient(Map diagnosis_info,Connection conn)
	{
		int patient_id=(int)diagnosis_info.get("patient_id");
		int disease_id=(int)diagnosis_info.get("disease_id");
		String sql;
		sql="select longitude, latitude from patient_info where patient_id='"+patient_id+"'";
		mySql ml=new mySql();
		Map<Integer,Map<Integer,Integer>> result=new HashMap<Integer,Map<Integer,Integer>>();
		double longitude=0;
		double latitude=0;
		ResultSet rs=ml.selectData(conn, sql);
		try {
			if(rs.next())
			{
				longitude=rs.getDouble("longitude");
				latitude=rs.getDouble("latitude");
				Map<String,Double> range_map=new HashMap<String,Double>();
				MapDistance mapDis=new MapDistance();
				double range_distance=DEFAULT_NEAR;
				range_map=mapDis.getAround(latitude, longitude, range_distance);
				double minLng=(double)range_map.get("minLng");
				double maxLng=(double)range_map.get("maxLng");
				double minLat=(double)range_map.get("minLat");
				double maxLat=(double)range_map.get("maxLat");
				//
				
				sql="select m.patient_id as p_id, m.hospital_id as h_id, count(*) as sum_counter from patient_info p,medical_records m where p.patient_id=m.patient_id and "
						+"p.longitude>'"+minLng+"' and p.longitude<'"+maxLng+"'and p.latitude>'"+minLat+"' and p.latitude<'"+maxLat+"'"
						+"and m.disease_id='"+disease_id+"' group by m.patient_id, m.hospital_id";
			
				rs=ml.selectData(conn, sql);
				
				int last_patient_id=-1;
				Map<Integer,Integer> hospital=new HashMap<Integer,Integer>();
				while(rs.next())
				{
					//System.out.println("病人："+rs.getInt("p_id")+"医院："+rs.getInt("h_id")+"次数："+rs.getInt("sum_counter"));
					if(rs.getInt("p_id")==last_patient_id)
					{
						hospital.put(rs.getInt("h_id"), rs.getInt("sum_counter"));
						last_patient_id=rs.getInt("p_id");
					}
					else
					{
						if(!hospital.isEmpty())
						{
							Map<Integer,Integer> temp_hospital=new HashMap<Integer,Integer>();
							temp_hospital.putAll(hospital);
							result.put(last_patient_id, temp_hospital);
							hospital.clear();
						}
						hospital.put(rs.getInt("h_id"), rs.getInt("sum_counter"));
						last_patient_id=rs.getInt("p_id");
					}
				}
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	//根据附近患同种病的人的信息进行医院推荐
	public Map<Integer,Double> findWardmateHospital(Map<Integer,Map<Integer,Integer>> patient_info,Map diagnosis_info,Connection conn)
	{
		String sql;
		int patient_id=(int)diagnosis_info.get("patient_id");
		sql="select longitude, latitude from patient_info where patient_id='"+patient_id+"'";
		mySql ml=new mySql();
		double longitude=0;
		double latitude=0;
		Map<Integer,Double> hospital_score=new HashMap<Integer,Double>();
		ResultSet rs=ml.selectData(conn, sql);
		MapDistance md=new MapDistance();
		Map<Integer,Double> distance_map=new HashMap<Integer,Double>();
		try {
			if(rs.next())
			{
				longitude=rs.getDouble("longitude");
				latitude=rs.getDouble("latitude");
				for(Integer key : patient_info.keySet())
				{
					int temp_patient_id=key;
					sql="select longitude, latitude from patient_info where patient_id='"+temp_patient_id+"'";
					ResultSet temp_rs=ml.selectData(conn, sql);
					if(temp_rs.next())
					{
						double distance=md.getDistance(temp_rs.getDouble("latitude"), temp_rs.getDouble("longitude"), latitude, longitude);
						distance_map.put(temp_patient_id, distance);
					}
					
				}
				Map<Integer,Integer> temp_distance_map=constructMap(sortMapByValueDouble(distance_map));
				int patient_num=patient_info.size();
				
				for(Integer key : patient_info.keySet())
				{
					int temp_patient_id=key;
					double patient_weight=0;
					patient_weight=(double)(patient_num-temp_distance_map.get(key))/patient_num;
					
					
					Map<Integer,Integer> temp_hospital=patient_info.get(key);
					int sum_counter=0;
					for(Integer value : temp_hospital.values())
					{
						sum_counter+=value;
					}
					for(Map.Entry<Integer, Integer> entry : temp_hospital.entrySet())
					{
						double score=0;
						if(hospital_score.containsKey(entry.getKey()))
						{
							score=hospital_score.get(entry.getKey());
							score=score+((double)entry.getValue()/sum_counter)*patient_weight;
						}
						else
						{
							score=((double)entry.getValue()/sum_counter)*patient_weight;
						}
						hospital_score.put(entry.getKey(), score);
					}
				}
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hospital_score;
	}
	
	//提升医院的等级以找到更多的医院
	public Map<Integer,Map> findMoreHospital(Map diagnosis_info,Connection conn)
	{
		int level=(int)diagnosis_info.get("hospital_level");
		Map<Integer,Map> result=new HashMap<Integer,Map>();
		Map<Integer,Map> result_temp=new HashMap<Integer,Map>();
		if(2==level)
		{
			diagnosis_info.put("hospital_level", level-1);
			result_temp=getHospitalData(diagnosis_info,conn);
			//comprehensiveScore(result_temp);
		}
		else
		{
			diagnosis_info.put("hospital_level", level+1);
			result_temp=getHospitalData(diagnosis_info,conn);
			//comprehensiveScore(result_temp);
		}
		diagnosis_info.put("hospital_level", level);
		result=getHospitalData(diagnosis_info,conn);
		result.putAll(result_temp);
		comprehensiveScore(result,diagnosis_info,conn);
		return result;
	}
	//提升对应的医院等级,找到更好的医院
	public Map<Integer,Map> findBetterHospital(Map diagnosis_info,Connection conn)
	{
		Map<Integer,Map> result=new HashMap<Integer,Map>();
		int level=(int)diagnosis_info.get("hospital_level");
		if(level<2)
		{
			++level;
			diagnosis_info.put("hospital_level", level);
			result=getHospitalData(diagnosis_info,conn);
			comprehensiveScore(result,diagnosis_info,conn);
		}
		else
		{
			System.out.println("已经是该范围内最好的医院了，你可以试试扩大搜索范围！");
		}
		return result;
	}
	//计算疾病等级
	public int computeDiseaseLevel(int disease_id,double certainty,Connection conn)
	{
		String sql;
		mySql ml=new mySql();
		sql="select level from disease_info where disease_id='"+disease_id+"'";
		int level=MAX_LEVEL;
		ResultSet rs=ml.selectData(conn, sql);
		try {
			if(rs.next())
			{
				level=rs.getInt("level");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(certainty<CERTAINTY_THRESHOLD&& level<MAX_LEVEL)
		{
			
			++level;
		}
		return level;
	}
	//得到医院的信息
	public Map<Integer,Map> getHospitalData(Map diagnosis_info,Connection conn)
	{
		
		//
		//System.out.println("level"+level);
		int disease_id=(int)diagnosis_info.get("disease_id");
		int patient_id=(int)diagnosis_info.get("patient_id");
		int level=(int)diagnosis_info.get("hospital_level");
		double range_distance=(double)diagnosis_info.get("distance");
		String sql;
		mySql ml=new mySql();
		
		sql="select longitude,latitude from patient_info where patient_id='"+patient_id+"'";
		ResultSet rs=ml.selectData(conn, sql);
		double longitude=0.0;
		double latitude=0.0;
		try {
			if(rs.next())
			{
				longitude=rs.getDouble("longitude");
				latitude=rs.getDouble("latitude");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
		//System.out.println("lng"+longitude+"lat"+latitude);
		Map range_map=new HashMap();
		MapDistance mapDis=new MapDistance();
		range_map=mapDis.getAround(latitude, longitude, range_distance);
		double minLng=(double)range_map.get("minLng");
		double maxLng=(double)range_map.get("maxLng");
		double minLat=(double)range_map.get("minLat");
		double maxLat=(double)range_map.get("maxLat");
		
		sql="select * from hospital_info where longitude>'"+minLng+"' and longitude<'"+maxLng+"'"
				+"and latitude>'"+minLat+"' and latitude<'"+maxLat+"' and level='"+level+"'";
		rs=ml.selectData(conn, sql);
		//List<Integer> hospital_id_set=new ArrayList<Integer>();
		Map<Integer,Map> result_map=new HashMap<Integer,Map>();
		//result_map.put(hospital_id, value);
		try {
			while(rs.next())
			{
				int temp_hospital_id=rs.getInt("hospital_id");
				Map hospital_detail=new HashMap();
			//	hospital_id_set.add(temp_hospital_id);
				
				double distance=mapDis.getDistance(rs.getDouble("latitude"), rs.getDouble("longitude"), latitude, longitude);
				hospital_detail.put("distance", distance);
				//distance_map.put(temp_hospital_id, distance);
				String temp_sql;
				
				temp_sql="select * from evaluate_global where disease_id='"+disease_id+"' and hospital_id='"+temp_hospital_id+"'";
				ResultSet temp_rs=ml.selectData(conn, temp_sql);
				if(temp_rs.next())
				{
					hospital_detail.put("effect", temp_rs.getDouble("effect"));
					hospital_detail.put("attitude", temp_rs.getDouble("attitude"));
					hospital_detail.put("charge", temp_rs.getDouble("charge"));
					hospital_detail.put("patient_counter", temp_rs.getInt("patient_counter"));
				}
				else
				{
					hospital_detail.put("effect", 5.0);
					hospital_detail.put("attitude", 5.0);
					hospital_detail.put("charge", 5.0);
					hospital_detail.put("patient_counter", 1);
				}
				result_map.put(temp_hospital_id, hospital_detail);
				
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result_map;	
	}
	//将默认推荐的医院与根据周围相同患者推荐的医院进行综合
	public List<Integer> combineAllHospital(Map<Integer,Map> initialHospital,Map<Integer,Double> wardmateHospital,Map diagnosis_info,Connection conn)
	{
		List<Integer> wardmateHospitalRank=new ArrayList<Integer>();
		String sql;
		mySql ml=new mySql();
		ResultSet rs;
		int disease_id=(int)diagnosis_info.get("disease_id");
		int patient_id=(int)diagnosis_info.get("patient_id");
		MapDistance mapDis=new MapDistance();
		sql="select longitude, latitude from patient_info where patient_id='"+patient_id+"'";
		rs=ml.selectData(conn, sql);
		double longitude=0;
		double latitude=0;
		try {
			if(rs.next())
			{
				longitude=rs.getDouble("longitude");
				latitude=rs.getDouble("latitude");
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(Integer key : wardmateHospital.keySet())
		{
			if(!initialHospital.containsKey(key))
			{
				int temp_hospital_id=key;
				sql="select * from evaluate_global where hospital_id='"+temp_hospital_id+"'and disease_id='"+disease_id+"'";	
				Map hospital_detail=new HashMap();
				ResultSet temp_rs;
				try {
					temp_rs=ml.selectData(conn, sql);
					sql="select longitude, latitude from hospital_info where hospital_id='"+temp_hospital_id+"'";	
					rs=ml.selectData(conn, sql);
					double distance=0;
					if(rs.next())
					{
						distance=mapDis.getDistance(rs.getDouble("latitude"), rs.getDouble("longitude"), latitude, longitude);
					}
					hospital_detail.put("distance", distance);
					if(temp_rs.next())
					{
						hospital_detail.put("effect", temp_rs.getDouble("effect"));
						hospital_detail.put("attitude", temp_rs.getDouble("attitude"));
						hospital_detail.put("charge", temp_rs.getDouble("charge"));
						hospital_detail.put("patient_counter", temp_rs.getInt("patient_counter"));
					}
					else
					{
						hospital_detail.put("effect", 5.0);
						hospital_detail.put("attitude", 5.0);
						hospital_detail.put("charge", 5.0);
						hospital_detail.put("patient_counter", 1);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				initialHospital.put(temp_hospital_id, hospital_detail);
			}
		}
		wardmateHospitalRank=sortMapByValueDouble(wardmateHospital);
		return wardmateHospitalRank;
	}
	//推荐的结果根据属性值排序，属性为double类型
	public List<Integer> attributeOrderDouble(Map<Integer,Map> result_map,String attribute)
	{
		Map<Integer,Double> attribute_map=new HashMap<Integer,Double>();
		for(Map.Entry<Integer, Map> entry :result_map.entrySet())
		{
			attribute_map.put(entry.getKey(), (Double)(entry.getValue().get(attribute)));
		}
		List<Integer> temp_attribute=sortMapByValueDouble(attribute_map);
		return temp_attribute;
	}
	//推荐的结果根据属性值排序，属性为int类型
	public List<Integer> attributeOrderInt(Map<Integer,Map> result_map,String attribute)
	{
		Map<Integer,Integer> attribute_map=new HashMap<Integer,Integer>();
		for(Map.Entry<Integer, Map> entry :result_map.entrySet())
		{
			attribute_map.put(entry.getKey(), (Integer)(entry.getValue().get(attribute)));
		}
		List<Integer> temp_attribute=sortMapByValueInt(attribute_map);
		return temp_attribute;
	}
	//由排序结果构建map对象
	public Map<Integer,Integer> constructMap(List<Integer> hospital_order)
	{
		Map<Integer,Integer> result=new HashMap<Integer,Integer>();
		for(int i=0;i<hospital_order.size();++i)
		{
			result.put(hospital_order.get(i),i);
		}
		return result;
	}
	//计算综合得分
	public void comprehensiveScore(Map<Integer,Map> result_map,Map diagnosis_info,Connection conn)
	{
		
		Map<Integer,Map<Integer,Integer>> wardmateHospital=findNearPatient(diagnosis_info,conn);
		Map<Integer,Double> wardmateHospitalScore=findWardmateHospital(wardmateHospital,diagnosis_info,conn);
		List<Integer> wardmateHospitalRank=combineAllHospital(result_map,wardmateHospitalScore,diagnosis_info,conn);
		Map<Integer,Integer> temp_distance_map=constructMap(attributeOrderDouble(result_map,"distance"));
		
		
		Map<Integer,Integer> temp_effect_map=constructMap(attributeOrderDouble(result_map,"effect"));
		Map<Integer,Integer> temp_charge_map=constructMap(attributeOrderDouble(result_map,"charge"));
		
		Map<Integer,Integer> temp_attitude_map=constructMap(attributeOrderDouble(result_map,"attitude"));
		Map<Integer,Integer> temp_patient_map=constructMap(attributeOrderInt(result_map,"patient_counter"));
		
	//	Map<Integer,Double> hospital_score_map=new HashMap<Integer,Double>();
		//System.out.println(wardmateHospitalRank);
		int hospital_num=result_map.size()-1;
		for(Integer key : result_map.keySet())
		{
			Map<String,Integer> evaluate_map=new HashMap<String,Integer>();
			int temp_id=key;
			evaluate_map.put("distance", temp_distance_map.get(temp_id));
			evaluate_map.put("effect", temp_effect_map.get(temp_id));
			evaluate_map.put("attitude", temp_attitude_map.get(temp_id));
			evaluate_map.put("charge", temp_charge_map.get(temp_id));
			evaluate_map.put("patient", temp_patient_map.get(temp_id));
			//
			if(wardmateHospitalRank.contains(temp_id))
			{
				int num=wardmateHospitalRank.size();
				double temp_score=((1+(double)(wardmateHospitalRank.indexOf(temp_id)/(num*2))))*computeScore(evaluate_map,hospital_num);
				if(temp_score>10)
				{
					temp_score=10.0;
				}
				result_map.get(temp_id).put("score", temp_score);
			}
			else
			{
				result_map.get(temp_id).put("score", computeScore(evaluate_map,hospital_num));
			}
		
		}
	}
	
	//排序
	public List<Integer> sortMapByValueDouble(Map<Integer,Double> map)//升序
	{
		List<Map.Entry<Integer, Double>> infoIds =
			    new ArrayList<Map.Entry<Integer, Double>>(map.entrySet());

			//排序
			Collections.sort(infoIds, new Comparator<Map.Entry<Integer, Double>>() {   
			    public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {      
			        if(o1.getValue() >o2.getValue())
			        	{
			        		return 1; 
			        	}
			        else
			        {
			        	return -1;
			        }
			    }
			}); 

			//排序后
			List<Integer> result=new ArrayList<Integer>();
			for (int i = 0; i < infoIds.size(); i++) {
			    result.add(infoIds.get(i).getKey());
			}
			return result;
	}
	//排序
	public  List<Integer> sortMapByValueInt(Map<Integer,Integer> map)//升序
	{
		List<Map.Entry<Integer, Integer>> infoIds =
			    new ArrayList<Map.Entry<Integer, Integer>>(map.entrySet());

			//排序
			Collections.sort(infoIds, new Comparator<Map.Entry<Integer, Integer>>() {   
			    public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {      
			        if(o1.getValue() >o2.getValue())
			        	{
			        		return 1; 
			        	}
			        else
			        {
			        	return -1;
			        }
			    }
			}); 

			//排序后
			List<Integer> result=new ArrayList<Integer>();
			for (int i = 0; i < infoIds.size(); i++) {
			    result.add(infoIds.get(i).getKey());
			}
			return result;
	}
	public double computeScore(Map<String,Integer> evaluate_map,int hospital_num)//综合排序
	{
		double result=0.0;
		//
		
		result=(double)(3*evaluate_map.get("effect")+2*(hospital_num-evaluate_map.get("distance"))+2*evaluate_map.get("charge")+2*evaluate_map.get("patient")+evaluate_map.get("attitude"))/(double)hospital_num;//满分10分
		return result;
	}
}
