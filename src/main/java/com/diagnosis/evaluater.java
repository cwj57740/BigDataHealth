package com.diagnosis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;


import kdTree.EuclideanDistance;
import kdTree.KDTree;
import kdTree.KeyDuplicateException;
import kdTree.KeySizeException;

public class evaluater {
	//得到评价信息
	public Map getEvaluate(Map diagnosis_info,int hospital_id)
	{
		Map evaluate_turnover=new HashMap();
		
		System.out.println("请对本次诊疗打分（1-10之间）：");
		Scanner s=new Scanner(System.in);
		System.out.println("请输入诊疗效果分数：");
		int effect=s.nextInt();
		System.out.println("请输入收费高低分数：");
		int charge=s.nextInt();
		System.out.println("请输入服务态度分数：");
		int attitude=s.nextInt();
		evaluate_turnover.put("disease_id", (int)diagnosis_info.get("disease_id"));
		evaluate_turnover.put("patient_id", (int)diagnosis_info.get("patient_id"));
		evaluate_turnover.put("hospital_id",hospital_id);
		evaluate_turnover.put("effect", effect);
		evaluate_turnover.put("charge", charge);
		evaluate_turnover.put("attitude", attitude);
		Date d = new Date();  
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	    String evaluate_time = sdf.format(d); 
	    evaluate_turnover.put("evaluate_time", evaluate_time);
	    return evaluate_turnover;
	}
	//插入评价流水
	public int inputEvaluate_turnover(Map evaluate_turnover,Connection conn)
	{
		String sql;
		int disease_id=(int)evaluate_turnover.get("disease_id");
		int hospital_id=(int)evaluate_turnover.get("hospital_id");
		int effect=(int)evaluate_turnover.get("effect");
		int charge=(int)evaluate_turnover.get("charge");
		int attitude=(int)evaluate_turnover.get("attitude");
		String evaluate_time=(String)evaluate_turnover.get("evaluate_time");
		int patient_id=(int)evaluate_turnover.get("patient_id");
		sql="insert into evaluate_turnover(disease_id,hospital_id,effect,charge,attitude,evaluate_time,patient_id)"
				+"values('"+disease_id+"','"+hospital_id+"','"+effect+"','"+charge+"','"+attitude+"','"+evaluate_time+"','"+patient_id+"')";
		mySql ml=new mySql();
		int result=ml.modifyData(conn, sql);
		System.out.println("感谢您的评分！");
		return result;
	}
	//更新总的评价信息
	public int inputEvaluate_global(Map evaluate_global,Connection conn )
	{
		String sql;
		int disease_id=(int)evaluate_global.get("disease_id");
		int hospital_id=(int)evaluate_global.get("hospital_id");
		double effect=(double)evaluate_global.get("effect");
		double charge=(double)evaluate_global.get("charge");
		double attitude=(double)evaluate_global.get("attitude");
		int patient_counter=(int)evaluate_global.get("patient_counter");
		sql="select * from evaluate_global where disease_id='"+disease_id+"' and hospital_id='"+hospital_id+"'";
		mySql ml=new mySql();
		ResultSet rs=ml.selectData(conn, sql);
		int result=0;
		double score=0;
		try {
			int row_num=0;
			rs.last();
			row_num=rs.getRow();
			rs.beforeFirst();
			if(0==row_num)
			{
				if(1==patient_counter)
				{
					score=(3*effect+2*charge+attitude)*0.5;
				}
				else
				{
					score=(3*effect+2*charge+attitude)*Math.log(patient_counter);
				}
				sql="insert into evaluate_global(disease_id,hospital_id,effect,charge,attitude,patient_counter,score)"
						+"values('"+disease_id+"','"+hospital_id+"','"+effect+"','"+charge+"','"+attitude+"','"+patient_counter+"','"+score+"')";
				result=ml.modifyData(conn, sql);
			}
			else
			{
				if(rs.next()){
					double old_effect=rs.getDouble("effect");
					double old_charge=rs.getDouble("charge");
					double old_attitude=rs.getDouble("attitude");
					int old_counter=rs.getInt("patient_counter");
					effect=(old_effect*old_counter+effect*patient_counter)/(old_counter+patient_counter);
					charge=(old_charge*old_counter+charge*patient_counter)/(old_counter+patient_counter);
					attitude=(old_attitude*old_counter+attitude*patient_counter)/(old_counter+patient_counter);
					patient_counter+=old_counter;
					score=(3*effect+2*charge+attitude)*Math.log(patient_counter);
					sql="update  evaluate_global set effect='"+effect+"',charge='"+charge+"',attitude='"+attitude+"',patient_counter='"+patient_counter+"',score='"+score+"'"
							+"where disease_id='"+disease_id+"' and hospital_id='"+hospital_id+"'";
					result=ml.modifyData(conn, sql);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	//周期性更新总的评价信息
	public ResultSet collectEvaluate(String startDate,String endDate,Connection conn)
	{
		String sql;
		sql="select disease_id,hospital_id,avg(effect) as avg_e,avg(charge) as avg_c,avg(attitude) as avg_a,count(*) as sum_p from(select * from evaluate_turnover where evaluate_time>='"+startDate+"' and evaluate_time<='"+endDate+"')"
				+"as table1 group by disease_id ,hospital_id";
		
		mySql ml=new mySql();
		ResultSet rs=ml.selectData(conn, sql);
		
		try {
			while(rs.next())
			{
				Map map=new HashMap();
				map.put("disease_id", rs.getInt("disease_id"));
				map.put("hospital_id", rs.getInt("hospital_id"));
				map.put("effect", rs.getDouble("avg_e"));
				map.put("charge",rs.getDouble("avg_c"));
				map.put("attitude", rs.getDouble("avg_a"));
				map.put("patient_counter", rs.getInt("sum_p"));
				inputEvaluate_global(map,conn);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
				
	}
	//刚开始产生的用户评价信息中忘记产生用户id，用来增加id的
	public void generateUserEvaluatePID(Connection conn){
		String sql;
		sql="select turnoverID from evaluate_turnover";
		mySql ml= new mySql();
		ResultSet rs=ml.selectData(conn, sql);
		try {
			while(rs.next()){
				Random rand=new Random();
				int randNum=rand.nextInt(5000);
				int turnoverID=rs.getInt("turnoverID");
				sql="update evaluate_turnover set patient_id = '"+randNum+"' where turnoverID='"+turnoverID+"'";
				ml.modifyData(conn, sql);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void updateUserEaluate(Connection conn){
		String sql;
		mySql ml=new mySql();
		sql="select * from evaluate_turnover";
		ResultSet rs=ml.selectData(conn, sql);
		try {
			while(rs.next())
			{
				Map map=new HashMap();
				map.put("patient_id", rs.getInt("patient_id"));
				map.put("hospital_id", rs.getInt("hospital_id"));
				map.put("effect", rs.getInt("effect"));
				map.put("charge",rs.getInt("charge"));
				map.put("attitude", rs.getInt("attitude"));
				inputUserEvaluate(map,conn);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//更新用户对医院的评价信息表
	public void inputUserEvaluate(Map evaluate_turnover,Connection conn){
		String sql;
		ResultSet rs;
		int patient_id=(int)evaluate_turnover.get("patient_id");
		int hospital_id=(int)evaluate_turnover.get("hospital_id");
		int effect=(int)evaluate_turnover.get("effect");
		int charge=(int)evaluate_turnover.get("charge");
		int attitude=(int)evaluate_turnover.get("attitude");
		double score=(3*effect+2*charge+2*attitude)/10;
		int counter=1;
		
		mySql ml= new mySql();
		sql="select score,counter from user_evaluate_info where hospital_id='"+hospital_id+"'and patient_id='"+patient_id+"'";
		rs=ml.selectData(conn, sql);
		try {
			int row_num=0;
			rs.last();
			row_num=rs.getRow();
			rs.beforeFirst();
		
			if(0==row_num){
				sql="insert into user_evaluate_info(hospital_id,patient_id,score,counter) values('"+hospital_id+"','"+patient_id+"','"+score+"','"+counter+"')";
				ml.modifyData(conn, sql);
			}
			else{
				if(rs.next()){
					double oldScore=(double)rs.getDouble("score");
					int oldCounter=(int)rs.getInt("counter");
					score=((oldScore*oldCounter)+score)/(oldCounter+counter);
					++oldCounter;
					sql="update user_evaluate_info set score='"+score+"' ,counter='"+oldCounter+"' where hospital_id='"+hospital_id+"' and patient_id='"+patient_id+"'";
					ml.modifyData(conn, sql);
				}
			}
		} catch (SQLException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
	}
	/*public Map<Integer, Double> CFEvaluator(int patient_id,Set<Integer> target_hospital,double range,Connection conn) throws SQLException, KeySizeException, KeyDuplicateException{
		
		List<Integer> potentialCFUser=new ArrayList<Integer>(); 
		long startTime=System.currentTimeMillis();
		potentialCFUser=findPotentialCFUser(patient_id, range, conn);
		//也可以用kd树来组织的
		long endTime=System.currentTimeMillis();
		System.out.println("找到附近用户的时间："+(endTime-startTime));
		
		
		Map<Integer, Set<Integer>> CFUser=new HashMap<Integer,Set<Integer>>();
		startTime=System.currentTimeMillis();
		CFUser=findCFUser(target_hospital, patient_id, potentialCFUser, conn);
		endTime=System.currentTimeMillis();
		System.out.println("在附近的用户中找到协同用户的时间："+(endTime-startTime));
		
		
		Map<Integer, Double> CFScore=new HashMap<Integer,Double>();
		startTime=System.currentTimeMillis();
		CFScore=computeCFScore(patient_id, CFUser, target_hospital, conn);
		endTime=System.currentTimeMillis();
		System.out.println("计算协同过滤得分的时间："+(endTime-startTime));
		return CFScore;
	}*/
	public ArrayList<Map.Entry<Integer, Double>> score_sort(Map<Integer, Double> hospital_score){
		ArrayList<Map.Entry<Integer, Double>> score_list=new ArrayList<Map.Entry<Integer, Double>>(hospital_score.entrySet());
		
		Collections.sort(score_list, new Comparator<Map.Entry<Integer,Double>>(){
			public int compare(Map.Entry<Integer, Double> object1,Map.Entry<Integer,Double> object2){
				if(object1.getValue()<object2.getValue()){//降序
					return 1;
				}
				else{
					return -1;
				}
			}
		});
		
		return score_list;
	}
	//协同过滤器，其中range单位为千米,target_hospital由前面Skyline提供
	public ArrayList<Map.Entry<Integer, Double>> CFEvaluator(int patient_id,int disease_id,double range,Connection conn) throws SQLException, KeySizeException, KeyDuplicateException{
		patient_id=3000;
		disease_id=5;
		range=15;
		
		
		
		double [] startPoint=new double[2];
		startPoint[0]=45.4133072062;
		startPoint[1]=125.9483334462;
		MapDistance md=new MapDistance();
		Map resultMap=md.getAround(startPoint[0], startPoint[1], 10000);
		double [] maxPoint=new double[2];
		maxPoint[0]=(double)resultMap.get("maxLat");
		maxPoint[1]=(double)resultMap.get("maxLng");
		double testMaxDistance=new EuclideanDistance().distance(startPoint,maxPoint);
		KDTree mKdTree=generateKDTree(conn);
		List<Integer> near=mKdTree.nearestEuclidean(startPoint,testMaxDistance);
		Map<String,List<HospitalNode>> resultNode=computeSkylineNode(startPoint, disease_id, near, conn);
		Set<Integer> target_hospital=new HashSet<Integer>();
		for(HospitalNode n: resultNode.get("bestNode")){
			target_hospital.add(n.hospital_id);
		}
		for(HospitalNode n: resultNode.get("preparedNode")){
			target_hospital.add(n.hospital_id);
		}
		List<Integer> potentialCFUser=new ArrayList<Integer>(); 
		long startTime=System.currentTimeMillis();
		potentialCFUser=findPotentialCFUser(patient_id, range, conn);
		//也可以用kd树来组织的
		long endTime=System.currentTimeMillis();
		System.out.println("找到附近用户的时间："+(endTime-startTime));
		
		
		Map<Integer, Set<Integer>> CFUser=new HashMap<Integer,Set<Integer>>();
		startTime=System.currentTimeMillis();
		CFUser=findCFUser(target_hospital, patient_id, potentialCFUser, conn);
		endTime=System.currentTimeMillis();
		System.out.println("在附近的用户中找到协同用户的时间："+(endTime-startTime));
		
		
		ArrayList<Map.Entry<Integer, Double>> CFScoreResult=new ArrayList<>();
		startTime=System.currentTimeMillis();
		Map<Integer, Double> CFScore=computeCFScore(patient_id, CFUser, target_hospital, conn);
		CFScoreResult=score_sort(CFScore);
		endTime=System.currentTimeMillis();
		System.out.println("计算协同过滤得分的时间："+(endTime-startTime));
		return CFScoreResult;
	}
	//找到对目标用户评分过的医院也有评分的用户，作为可能的协同过滤用户,其中range的单位是千米
	public List<Integer> findPotentialCFUser(int patient_id,double range ,Connection conn){
		String sql;
		mySql ml=new mySql();
		sql="select latitude,longitude from patient_info where patient_id='"+patient_id+"'";
		double ownLatitude=0;
		double ownLongitude=0;
		ResultSet rs=ml.selectData(conn, sql);
		try {
			if(rs.next()){
				ownLatitude=rs.getDouble("latitude");
				ownLongitude=rs.getDouble("longitude");
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		List<Integer> ls=new ArrayList<Integer>();
		sql="select patient_id from user_evaluate_info where hospital_id in(select hospital_id from user_evaluate_info where patient_id='"+patient_id+"')";
		rs=ml.selectData(conn, sql);
		
		//
		int counter=0;
		try {
			while(rs.next()){
				
				int potential_id=rs.getInt("patient_id");
				sql="select latitude,longitude from patient_info where patient_id='"+potential_id+"'";
				ResultSet temp_rs=ml.selectData(conn, sql);
				if(temp_rs.next()){
					++counter;
					double p_latitude=temp_rs.getDouble("latitude");
					double p_longitude=temp_rs.getDouble("longitude");
					MapDistance md=new MapDistance();
					
					double dis=md.getDistance(ownLatitude, ownLongitude, p_latitude, p_longitude);
					
					if(dis<range){
						
						ls.add(potential_id);
					}
				}
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ls;
	}
	//计算出那些可能的协同过滤的用户评分过的医院集合
	public Map<Integer, Set<Integer>> findCFUser(Set<Integer> target_hospital,int patient_id,List<Integer> potential_id,Connection conn) throws SQLException{
		String sql;
		mySql ml=new mySql();
		//
		int count=0;
		Map<Integer, Set<Integer>> resultMap=new HashMap<Integer, Set<Integer>>();
		sql="select hospital_id,patient_id from user_evaluate_info where patient_id in(";
		for(int index=0;index<potential_id.size();++index){
			sql+=potential_id.get(index)+",";
		}
		sql=sql.substring(0,sql.length()-1)+")";
		ResultSet rs=ml.selectData(conn, sql);
		while(rs.next()){
			int temp_patient_id=rs.getInt("patient_id");
			int temp_hospital_id=rs.getInt("hospital_id");
			
			Set<Integer> temp_set=resultMap.get(temp_patient_id);
			if(null==temp_set){
				temp_set=new HashSet<>();
				temp_set.add(temp_hospital_id);
				resultMap.put(temp_patient_id, temp_set);
			}
			else{
				if(!temp_set.contains(temp_hospital_id)){
					resultMap.get(temp_patient_id).add(temp_hospital_id);
				}
			}
		}
		return resultMap;
	
	}
	//计算用户对医院评分的平均值
	public double computeAverageScore(Map<Integer, Double>scoreMap){
		double result_score=0;
		double total_score=0;
		int counter=scoreMap.size();
		for(Map.Entry<Integer, Double> entry : scoreMap.entrySet()){
			total_score+=entry.getValue();
		}
		if (0==counter) {
			return 0;
		}
		result_score=(double)total_score/(double)counter;
		return result_score;
	}

	
	public Map<Integer,Double> computeCFScore(int patient_id,Map<Integer, Set<Integer>> CF_user,Set<Integer> target_hospital,Connection conn) throws SQLException{
		Map<Integer,Double> result_map=new HashMap<Integer,Double>();
		Map<Integer, Map<String, Double>> pre_map=new HashMap<Integer, Map<String, Double>>();
		String sql;
		mySql ml=new mySql();
		long sTime=System.currentTimeMillis();
		
		Map<Integer, Double> ownScore_map=new HashMap<>();
		sql="select hospital_id,score from user_evaluate_info where patient_id='"+patient_id+"'";
		ResultSet rs=ml.selectData(conn, sql);
		while(rs.next()){
			ownScore_map.put(rs.getInt("hospital_id"), rs.getDouble("score"));
		}
	
		double own_average_score=computeAverageScore(ownScore_map);
		
		for(Integer i: target_hospital){
			result_map.put(i, own_average_score);
		}
		String user_idString="";
		String hospital_idString="";
		ArrayList<Integer> user_list=new ArrayList<>();
		Set<Integer> hospital_set=new HashSet<>();
		for(Map.Entry<Integer, Set<Integer>> entry:CF_user.entrySet()){
			user_list.add(entry.getKey());
			hospital_set.addAll(entry.getValue());
		}
		
		for(int index=0;index<user_list.size();++index){
			user_idString+=user_list.get(index)+",";
		}
		user_idString=user_idString.substring(0,user_idString.length()-1);
		
		for(Integer i:hospital_set){
			hospital_idString+=i+",";
		}
		hospital_idString=hospital_idString.substring(0,hospital_idString.length()-1);
		
		Map<Integer, Map<Integer, Double>> evaluateMap=new HashMap<>();
		sql="select patient_id, hospital_id, score from user_evaluate_info where patient_id in("+user_idString+") and hospital_id in("+hospital_idString+")";
		rs=ml.selectData(conn, sql);
		while(rs.next()){
			if(!evaluateMap.isEmpty()){
				if(null==evaluateMap.get(rs.getInt("patient_id"))){
					Map<Integer, Double> tempMap=new HashMap<>();
					tempMap.put(rs.getInt("hospital_id"), rs.getDouble("score"));
					evaluateMap.put(rs.getInt("patient_id"), tempMap);
				}
				else{
					evaluateMap.get(rs.getInt("patient_id")).put(rs.getInt("hospital_id"), rs.getDouble("score"));
				}
			}
			else {
				Map<Integer, Double> tempMap=new HashMap<>();
				tempMap.put(rs.getInt("hospital_id"), rs.getDouble("score"));
				evaluateMap.put(rs.getInt("patient_id"), tempMap);
			}
		}
		
		
		for(Map.Entry<Integer, Map<Integer, Double>> entry : evaluateMap.entrySet()){
			double average_score=computeAverageScore(entry.getValue());
		
			double diff_pro_sum=0;//差的乘积的和
			double own_diff_squ_sum=0;//自己差的平方和
			double smi_diff_squ_sum=0;//相似用户的差的平方和
			
			for(Map.Entry<Integer, Double> entry2 : entry.getValue().entrySet()){
				double own_diff=0;//自己的差
				double smi_diff=0;//相似用户的差
				if(!ownScore_map.isEmpty()){
					if(null!=ownScore_map.get(entry2.getKey())){
						own_diff=ownScore_map.get(entry2.getKey())-own_average_score;
					}
				}
				smi_diff=entry2.getValue()-average_score;
				diff_pro_sum+=own_diff*smi_diff;
				
				own_diff_squ_sum+=own_diff*own_diff;
				
				smi_diff_squ_sum+=smi_diff*smi_diff;
			}
			double similarity=diff_pro_sum/(Math.sqrt(own_diff_squ_sum)*Math.sqrt(smi_diff_squ_sum));
			
			Set<Integer> potential_hospital_set=new HashSet<>();
			potential_hospital_set.addAll(CF_user.get(entry.getKey()));
			if(similarity>0){
				computePre(pre_map,entry.getValue(), average_score, similarity, entry.getKey(), potential_hospital_set, target_hospital, conn);
			}
			for(Map.Entry<Integer, Map<String, Double>> e : pre_map.entrySet()){
				result_map.put(e.getKey(), own_average_score+(e.getValue().get("accumulate_score")/e.getValue().get("accumulate_similarity")));
				
			}
		}
		
		long endTime=System.currentTimeMillis();
	//	System.out.println("第二部分时间："+(endTime-startTime));
		//System.out.println("第二部分查询时间："+allTime);
		//System.out.println("计算预测值的时间："+preTime);
		return result_map;
	}
	//计算协同过滤中对目标集合中每个医院的预测评分,计算结果保存在pre_map中，该函数在computeCFScore中被调用
public void computePre(Map<Integer, Map<String, Double>> pre_map,Map<Integer, Double> scoreMap,double average_score,double similarity,int user_id,Set<Integer> hospital_set,Set<Integer> target_hospital,Connection conn) {
		
		Set<Integer> temp_set=new HashSet<Integer>();
		String sql;
		mySql ml = new mySql();
		temp_set.addAll(target_hospital);
		temp_set.retainAll(hospital_set);
		for(Integer id : temp_set){
			if(null!=scoreMap.get(id)){
				if(null!=pre_map.get(id)){
					double temp_score=pre_map.get(id).get("accumulate_score")+similarity*(scoreMap.get(id)-average_score);
					pre_map.get(id).put("accumulate_score", temp_score);
					
					double temp_similarity=pre_map.get(id).get("accumulate_similarity")+similarity;
					pre_map.get(id).put("accumulate_similarity", temp_similarity);
				}
				else{
					Map<String, Double> mp =new HashMap<String,Double>();
					mp.put("accumulate_score", similarity*(scoreMap.get(id)-average_score));
					mp.put("accumulate_similarity", similarity);
					pre_map.put(id, mp);
				}
			}
			
			
		}
	}
	/*public void computePre(Map<Integer, Map<String, Double>> pre_map,double average_score,double similarity,int user_id,Set<Integer> hospital_set,Set<Integer> target_hospital,Connection conn) {
		
		Set<Integer> temp_set=new HashSet<Integer>();
		String sql;
		mySql ml = new mySql();
		temp_set.addAll(target_hospital);
		temp_set.retainAll(hospital_set);
		for(Integer id : temp_set){
			sql="select score from user_evaluate_info where hospital_id='"+id+"' and patient_id='"+user_id+"' ";
			ResultSet rs =ml.selectData(conn, sql);
			try {
				if(rs.next()){
					if(null!=pre_map.get(id)){
						
						double temp_score=pre_map.get(id).get("accumulate_score")+similarity*(rs.getDouble("score")-average_score);
						pre_map.get(id).put("accumulate_score", temp_score);
						
						double temp_similarity=pre_map.get(id).get("accumulate_similarity")+similarity;
						pre_map.get(id).put("accumulate_similarity", temp_similarity);
						
						
					}
					else {
						Map<String, Double> mp =new HashMap<String,Double>();
						mp.put("accumulate_score", similarity*(rs.getDouble("score")-average_score));
						mp.put("accumulate_similarity", similarity);
						pre_map.put(id, mp);
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}*/
	//对医院生成kd树
	public KDTree generateKDTree(Connection conn) throws SQLException, KeySizeException, KeyDuplicateException{
		KDTree mKdTree=new KDTree(2);
		String sql;
		mySql ml=new mySql();
		sql="select hospital_id,latitude,longitude from hospital_info";
		ResultSet rs=ml.selectData(conn, sql);
		while(rs.next()){
			double [] position=new double[2];
			position[0]=rs.getDouble("latitude");
			position[1]=rs.getDouble("longitude");
			mKdTree.insert(position, rs.getInt("hospital_id"));
		}
		return mKdTree;
	}
	//定义优先级队列中的优先级顺序
	public static Comparator<HospitalNode> orderDominated = new Comparator<HospitalNode>() {
		public int compare(HospitalNode n1,HospitalNode n2){
			if(n2.isDominatedBy(n1)){
				return 1;
			}
			else {
				return -1;
			}
		}
	};
	//计算Skyline点以及预备队列中点的得分
	public Map<String,List<HospitalNode>> computeSkylineNode(double [] startPoint,int disease_id,List<Integer> hospital_id_list,Connection conn) throws SQLException{
		Map<String, List<HospitalNode>> resultMap=new HashMap<String, List<HospitalNode>>();
		List<HospitalNode> result_node=new ArrayList<HospitalNode>();
		SkylineNode sl=new SkylineNode();
		List<HospitalNode> suspectNode=sl.generateSuspectNode(disease_id,startPoint, hospital_id_list, conn);
		int k=10;
		Queue<HospitalNode> pQueue= new PriorityQueue<HospitalNode>(3,orderDominated);
		result_node=sl.BNL(suspectNode, startPoint,pQueue,k);
		List<HospitalNode> prepared_node=new ArrayList<HospitalNode>();
		while(!pQueue.isEmpty()){
			prepared_node.add(pQueue.poll());
		}
		resultMap.put("bestNode", result_node);
		resultMap.put("preparedNode", prepared_node);
		return resultMap;
	}
	public void test(Connection conn){
		String sql;
		//sql="insert into test (name,age,id1) values(8,8,5)";
		sql="select * from test";
		mySql ml=new mySql();
		ResultSet rs=ml.selectData(conn, sql);
		try {
			rs.next();
			int id=rs.getInt("id1");
			System.out.println(id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
