package com.diagnosis;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;




public class SkylineNode {
	private List<HospitalNode> sList;
	public SkylineNode(){
		super();
		this.sList=new LinkedList<HospitalNode>();
	}
	public List<HospitalNode> getsList(){
		return this.sList;
	}
	//根据输入的医院id集合，生成医院节点，以便后面用来计算Skyline点
	public List<HospitalNode> generateSuspectNode(int disease_id,double [] startPoint,List<Integer> hospital_idList,Connection conn) throws SQLException{
		String sql;
		mySql ml=new mySql();
		List<HospitalNode> resultNode =new ArrayList<HospitalNode>();
		for(int i=0;i<hospital_idList.size();++i){
			HospitalNode temp_node=new HospitalNode();
			int hospital_id=hospital_idList.get(i);
			sql="select latitude,longitude from hospital_info where hospital_id='"+hospital_id+"'";
			ResultSet rs=ml.selectData(conn, sql);
			if(rs.next()){
				temp_node.position[0]=rs.getDouble("latitude");
				temp_node.position[1]=rs.getDouble("longitude");
				temp_node.hospital_id=hospital_id;
				temp_node.distance=temp_node.sqrDistance(startPoint);
			}
			sql="select score from evaluate_global where disease_id='"+disease_id+"' and hospital_id='"+hospital_id+"'";
			rs=ml.selectData(conn, sql);
			if(rs.next()){
				temp_node.score=rs.getDouble("score");
			}
			resultNode.add(temp_node);
		}
	/*	for(HospitalNode n: resultNode){
			n.display();
		}
		System.out.println("测试生成点");*/
		return resultNode;
	}
	//将Skyline中淘汰的点插入优先级队列
	public void putIntoQueue(HospitalNode pNode,Queue<HospitalNode> prepareQueue,int k){
		if(prepareQueue.size()<k){
			prepareQueue.offer(pNode);
			
		}
		else{
			if(prepareQueue.peek().isDominatedBy(pNode)){
				prepareQueue.poll();
				prepareQueue.offer(pNode);
			}
			else{
				
			}
		}
		return ;
	}
	//使用循环嵌套算法计算Skyline点以及预备队列中的点
	public List<HospitalNode> BNL(List<HospitalNode> suspectnode,double [] startPoint,Queue<HospitalNode> prepareQueue,int k){
		if(suspectnode!=null && !suspectnode.isEmpty()){
			sList.add(suspectnode.get(0));
			for(int i=1;i<suspectnode.size();++i){
				boolean dominated=false;
				for(int j=0;j<sList.size();++j){
					if(suspectnode.get(i).isDominatedBy(sList.get(j))){
						putIntoQueue(suspectnode.get(i), prepareQueue, k);
						dominated=true;
						break;
					}
					if(sList.get(j).isDominatedBy(suspectnode.get(i))){
						putIntoQueue(sList.get(j), prepareQueue, k);
						sList.remove(j);
						--j;
					}
				}
				if(!dominated){
					sList.add(suspectnode.get(i));
				}
			}
		}
		return sList;
	}
	
}