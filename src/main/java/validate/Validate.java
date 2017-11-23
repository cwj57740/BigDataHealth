package validate;

import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.io.*;
import dbaccess.*;

public class Validate extends HttpServlet {
	String uname = null;
	String upass = null;
	DBAccess dba;

	public void init() {
		dba = new DBAccess();
		try {
			dba.init();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// uname=request.getParameter("uname");
		// String tempass=request.getParameter("upass");
		// try{
		// upass=dba.query1(uname).trim();
		// }
		// catch(SQLException e){
		// System.out.println(e.getMessage());
		// }
		if (false) {
			response.sendRedirect("error.jsp");
		} else {
			response.sendRedirect("ok.jsp");
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
