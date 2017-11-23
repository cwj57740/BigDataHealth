package out;
import javax.servlet.*;
import javax.servlet.http.*;

import dbaccess.DBAccess;

import java.sql.*;
import java.io.*;
import preview.*;

public class Out extends HttpServlet{
	String ucity=null;
	public void init(){
		
	}
	public void doGet(HttpServletRequest request,HttpServletResponse response)
	throws ServletException,IOException{
		ucity=request.getParameter("city");
//		if(uname!=null){
//			response.sendRedirect("newusererror.jsp");
//			uname =null;
//		}
//		else{
//			try{
//				dba.insert(tempuname, upass);
//				response.sendRedirect("newuserok.jsp");
//			}
//			catch(SQLException e){
//				System.out.println(e.getMessage());
//			}
		System.out.println("1111");
		switch(ucity){
		case "zero":
			response.sendRedirect("zero.jsp");
			break;
		case "one":
			response.sendRedirect("one.jsp");
			break;
		case "two":
			response.sendRedirect("two.jsp");
			break;
		case "three":
			response.sendRedirect("three.jsp");
			break;
		case "four":
			response.sendRedirect("four.jsp");
			break;
		}
//		catch(SQLException e){
//			System.out.println(e.getMessage());
//		}
//	
		
	}
		public void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
			doGet(request,response);
		}
}
