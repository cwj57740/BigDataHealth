package newuser;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.io.*;
import dbaccess.*;
public class Newuser extends HttpServlet{
String uname =null;
String upass=null;
DBAccess dba;
public void init(){
	dba=new DBAccess();
	try {
		dba.init();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		System.out.println(e.getMessage());
	}
}
public void doGet(HttpServletRequest request,HttpServletResponse response)
throws ServletException,IOException{
	String tempuname=request.getParameter("uname");
	upass=request.getParameter("upass");
	try{
		uname=dba.query2(tempuname).trim();
	}
	catch(SQLException e){
		System.out.println(e.getMessage());
	}
	if(uname!=null){
		response.sendRedirect("newusererror.jsp");
		uname =null;
	}
	else{
		try{
			dba.insert(tempuname, upass);
			response.sendRedirect("newuserok.jsp");
		}
		catch(SQLException e){
			System.out.println(e.getMessage());
		}
		
	}
}
	public void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
		doGet(request,response);
	}
}

