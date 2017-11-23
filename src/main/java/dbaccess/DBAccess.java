package dbaccess;
import java.sql.*;
public class DBAccess {
	String driver="com.mysql.jdbc.Driver";
	String url="jdbc:mysql://localhost:3306/";
	String user="root";
	String password="123";
	Connection conn=null;
	Statement stmt=null;
	public void init() throws SQLException
	{
		try{
			System.out.println("llllll");
			Class.forName(driver);
			System.out.println("llllll");
			conn=DriverManager.getConnection(url,user,password);
			System.out.println("llllll");
			stmt=conn.createStatement();
			System.out.println("llllll");
		}
		catch(ClassNotFoundException e){
			System.out.println("找不到驱动程序");
			e.printStackTrace();
		}
	}
	public void insert(String uname,String upass)throws SQLException{
		String str="insert into users values('"+uname+"','"+upass+"')";
		stmt.execute(str);
	}
	public void update(String uname,String upass)throws SQLException{
		String str="update into users values('"+uname+"','"+upass+"')";
		stmt.execute(str);
	}
	public String query1(String uname)throws SQLException{
		String str="select upass from users where uname='"+uname+"'";
		ResultSet rs=stmt.executeQuery(str);
		rs.next();
		String result=rs.getString("upass");
		return result;
	}
	public String query2(String uname)throws SQLException{
		String str="select uname from users where uname='"+uname+"'";
		ResultSet rs=stmt.executeQuery(str);
		rs.next();
		String result=rs.getString("uname");
		return result;
	}
	public void submit()throws SQLException{
		stmt.close();
		conn.close();
	}
}
