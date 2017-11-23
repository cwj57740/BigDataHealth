package com.ClinicalGuidingGUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.MouseInputAdapter;
import javax.swing.text.html.Option;

import org.omg.CORBA.PUBLIC_MEMBER;

import com.diagnosis.mySql;


public class UserLogin extends JFrame{
	//BufferedImage backGroundImage=null;
	private MyContentPane contentPane;
	private JPanel panel;
	private JTextField userName;
	private JPasswordField userPassWord;
	private TransparentButton loginButton;
	private TransparentButton registerButton;
	public static final int DEFAULT_WIDTH=700;
	public static final int DEFAULT_HEIGHT=500;
	
	private boolean passwordValidate() throws SQLException{
		mySql ml=new mySql();
		Connection conn=ml.getConnection();
		ResultSet rs;
		String userNameString=userName.getText();
		if(userNameString.isEmpty())
		{
			return false;
		}
		int patient_id=Integer.parseInt(userNameString);
		String sql;
		sql="select password from patient_info where patient_id='"+patient_id+"'";
		rs=ml.selectData(conn, sql);
		if(rs.next()){
			String password=rs.getString("password");
			
			if(password.equals(String.valueOf(userPassWord.getPassword())))
			{
				conn.close();
				return true;
			}
			else {
				conn.close();
				return false;
			}
		}
		conn.close();
		return false;
	}
	
	final class ButtonActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(e.getSource()==registerButton){
				UserRegister register=new UserRegister();
			}
			else if(e.getSource()==loginButton){
				boolean flag=false;
				try {
					flag = passwordValidate();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(flag)
				{
					DiagnosisGUI diagnosis=new DiagnosisGUI(Integer.parseInt(userName.getText()));
					dispose();
				}
				else {
					Map<String, String> textIcon=new HashMap<String,String>();
					textIcon.put("buttonText", "确认");
					textIcon.put("buttonIcon", "messageButton.png");
					textIcon.put("labelText", "您输入的用户名或密码错误，请重新输入！");
					textIcon.put("labelIcon", "messageFailLabel.png");
					textIcon.put("titleText", "登录失败");
					MyMessageDialog messageDialog=new MyMessageDialog(textIcon);
					userPassWord.setText("");
				}
			}
		}
	}
	
	final class ButtonMouseActionListener extends MouseInputAdapter{
		public void mouseEntered(MouseEvent e){
			if(e.getSource()==loginButton||e.getSource()==registerButton){
				((TransparentButton) e.getSource()).setForeground(new Color(0, 60, 150));
			}
		}
		public void mouseExited(MouseEvent e){
			if(e.getSource()==loginButton||e.getSource()==registerButton){
				((TransparentButton) e.getSource()).setForeground(Color.BLACK);
			}
		}
	}
	
	private void initComponent() throws Exception{
		this.setTitle("智能导诊--用户登录");
		this.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		
		//backGroundImage=ImageIO.read(new File("backGround.jpg"));
		
		contentPane=new MyContentPane();
		panel=new JPanel();
		panel.setLayout(new GridLayout(3,1));
		userName=new JTextField(20);
		userPassWord=new JPasswordField(20);
		loginButton=new TransparentButton("登录",new ImageIcon("loginButton.png"));
		
		//loginButton.setForeground(Color.RED);
		//loginButton.setBackground(Color.blue);
		//loginButton.setOpaque(false);
		loginButton.setContentAreaFilled(false);
		loginButton.setBorderPainted(false);
		registerButton=new TransparentButton("注册",new ImageIcon("registerButton.png"));
		registerButton.setContentAreaFilled(false);
		registerButton.setBorderPainted(false);
	}
	
	private void addComponent(){
		JPanel user=new JPanel();
		user.setOpaque(false);
		user.add(new JLabel("用户名："));
		user.add(userName);
		JPanel pwd=new JPanel();
		pwd.add(new JLabel("密  码："));
		pwd.add(userPassWord);
		pwd.setOpaque(false);
		JPanel opt=new JPanel();
		opt.setOpaque(false);
		opt.add(new TransparentPane());
		opt.add(new TransparentPane());
		opt.add(new TransparentPane());
		opt.add(new TransparentPane());
		opt.add(loginButton);
		opt.add(new TransparentPane());
		opt.add(registerButton);
		//opt.setBackground(Color.green);
		
		
		panel.add(user);
		panel.add(pwd);
		panel.add(opt);
		panel.setOpaque(false);
		
		
		contentPane.setLayout(new GridBagLayout());
		contentPane.add(new TransparentPane(), new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		contentPane.add(new TransparentPane(), new GridBagConstraints(0, 1, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		contentPane.add(panel, new GridBagConstraints(1, 1, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		contentPane.add(new TransparentPane(), new GridBagConstraints(2, 1, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		contentPane.add(new TransparentPane(), new GridBagConstraints(0, 2, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		
		this.setContentPane(contentPane);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
	
	public void addListener(){
		ButtonActionListener bal=new ButtonActionListener();
		registerButton.addActionListener(bal);
		loginButton.addActionListener(bal);
		ButtonMouseActionListener bmal=new ButtonMouseActionListener();
		registerButton.addMouseListener(bmal);
		loginButton.addMouseListener(bmal);
	}
	
	public UserLogin() throws Exception{
		initComponent();
		addComponent();
		addListener();
	}
}
