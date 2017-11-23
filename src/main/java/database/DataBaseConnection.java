package database;


import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBaseConnection {

//	static String sql = null;
	static DBHelper db1 = null;
	static ResultSet ret = null;
	static String backline = null;



	 public String getBack() {
		    return backline;
		 }
		 public static void setBack(String password) {
		   backline = password;
		 }
}
