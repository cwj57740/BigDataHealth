package com.ClinicalGuidingGUI;

import java.sql.ResultSet;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.diagnosis.mySql;
import com.mysql.jdbc.Connection;

public class TestMain {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UserLogin login=new UserLogin();
		
	}

}
