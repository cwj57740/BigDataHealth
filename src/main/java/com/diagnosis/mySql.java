package com.diagnosis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class mySql {
	//得到连接
	public Connection getConnection()
	{
		Connection conn=null;
		String url="jdbc:mysql://localhost:3306/bigdatahealthdb?"
				+"user=root&password=root&useUnicode=true&characterEncoding=UTF8";
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn=DriverManager.getConnection(url);
		}catch(SQLException e){
			System.out.println("MySql操作错误");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Sorry,can't find the diriver!");
			e.printStackTrace();
		}
		return conn;
	}
	//关闭连接
	public void closeConnection(Connection conn)
	{
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//修改数据
	public int modifyData(Connection conn,String sql)
	{
		Statement stmt;
		int result=-1;
		try {
			stmt = conn.createStatement();
			result=stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	//选择数据
	public ResultSet selectData(Connection conn,String sql)
	{
		Statement stmt;
		ResultSet rs=null;
		try {
			stmt=conn.createStatement();
			
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return rs;
		 
	}
}
