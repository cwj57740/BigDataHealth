package com.ClinicalGuidingGUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RecommenderGUI extends JFrame{
	public static final int DEFAULT_WIDTH=700;
	public static final int DEFAULT_HEIGHT=500;
	private MyContentPane contentPane;
	private JTextField hospital1;
	private JTextField hospital2;
	private JTextField hospital3;
	private JTextField hospital4;
	private JTextField hospital5;
	private JTextField hospital6;
	private JTextField hospital7;
	private JTextField hospital8;
	private JTextField hospital9;
	private JTextField hospital10;
	
	private JTextField distance1;
	private JTextField distance2;
	private JTextField distance3;
	private JTextField distance4;
	private JTextField distance5;
	private JTextField distance6;
	private JTextField distance7;
	private JTextField distance8;
	private JTextField distance9;
	private JTextField distance10;
	
	private JTextField multipleScore1;
	private JTextField multipleScore2;
	private JTextField multipleScore3;
	private JTextField multipleScore4;
	private JTextField multipleScore5;
	private JTextField multipleScore6;
	private JTextField multipleScore7;
	private JTextField multipleScore8;
	private JTextField multipleScore9;
	private JTextField multipleScore10;
	
	private void initComponent(){
		contentPane=new MyContentPane();
		hospital1=new JTextField(30);
		hospital2=new JTextField(30);
		hospital3=new JTextField(30);
		hospital4=new JTextField(30);
		hospital5=new JTextField(30);
		hospital6=new JTextField(30);
		hospital7=new JTextField(30);
		hospital8=new JTextField(30);
		hospital9=new JTextField(30);
		hospital10=new JTextField(30);
		
		distance1=new JTextField(10);
		distance2=new JTextField(10);
		distance3=new JTextField(10);
		distance4=new JTextField(10);
		distance5=new JTextField(10);
		distance6=new JTextField(10);
		distance7=new JTextField(10);
		distance8=new JTextField(10);
		distance9=new JTextField(10);
		distance10=new JTextField(10);
		
		multipleScore1=new JTextField(5);
		multipleScore2=new JTextField(5);
		multipleScore3=new JTextField(5);
		multipleScore4=new JTextField(5);
		multipleScore5=new JTextField(5);
		multipleScore6=new JTextField(5);
		multipleScore7=new JTextField(5);
		multipleScore8=new JTextField(5);
		multipleScore9=new JTextField(5);
		multipleScore10=new JTextField(5);
		
	}
	
	private void addComponent(){
		this.setTitle("智能导诊--医院推荐");
		this.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
		this.setLocationRelativeTo(null);
		contentPane.setLayout(new GridBagLayout());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		
		JPanel panel1=new JPanel();
		panel1.setOpaque(false);
		panel1.add(new JLabel("医院名称："));
		panel1.add(hospital1);
		panel1.add(new JLabel("距离："));
		panel1.add(distance1);
		panel1.add(new JLabel("综合评分："));
		panel1.add(multipleScore1);
		contentPane.add(panel1, new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		JPanel panel2=new JPanel();
		panel2.setOpaque(false);
		panel2.add(new JLabel("医院名称："));
		panel2.add(hospital2);
		panel2.add(new JLabel("距离："));
		panel2.add(distance2);
		panel2.add(new JLabel("综合评分："));
		panel2.add(multipleScore2);
		contentPane.add(panel2, new GridBagConstraints(0, 1, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		JPanel panel3=new JPanel();
		panel3.setOpaque(false);
		panel3.add(new JLabel("医院名称："));
		panel3.add(hospital3);
		panel3.add(new JLabel("距离："));
		panel3.add(distance3);
		panel3.add(new JLabel("综合评分："));
		panel3.add(multipleScore3);
		contentPane.add(panel3, new GridBagConstraints(0, 2, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		JPanel panel4=new JPanel();
		panel4.setOpaque(false);
		panel4.add(new JLabel("医院名称："));
		panel4.add(hospital4);
		panel4.add(new JLabel("距离："));
		panel4.add(distance4);
		panel4.add(new JLabel("综合评分："));
		panel4.add(multipleScore4);
		contentPane.add(panel4, new GridBagConstraints(0, 3, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		JPanel panel5=new JPanel();
		panel5.setOpaque(false);
		panel5.add(new JLabel("医院名称："));
		panel5.add(hospital5);
		panel5.add(new JLabel("距离："));
		panel5.add(distance5);
		panel5.add(new JLabel("综合评分："));
		panel5.add(multipleScore5);
		contentPane.add(panel5, new GridBagConstraints(0, 4, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		JPanel panel6=new JPanel();
		panel6.setOpaque(false);
		panel6.add(new JLabel("医院名称："));
		panel6.add(hospital6);
		panel6.add(new JLabel("距离："));
		panel6.add(distance6);
		panel6.add(new JLabel("综合评分："));
		panel6.add(multipleScore6);
		contentPane.add(panel6, new GridBagConstraints(0, 5, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		JPanel panel7=new JPanel();
		panel7.setOpaque(false);
		panel7.add(new JLabel("医院名称："));
		panel7.add(hospital7);
		panel7.add(new JLabel("距离："));
		panel7.add(distance7);
		panel7.add(new JLabel("综合评分："));
		panel7.add(multipleScore7);
		contentPane.add(panel7, new GridBagConstraints(0, 6, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		JPanel panel8=new JPanel();
		panel8.setOpaque(false);
		panel8.add(new JLabel("医院名称："));
		panel8.add(hospital8);
		panel8.add(new JLabel("距离："));
		panel8.add(distance8);
		panel8.add(new JLabel("综合评分："));
		panel8.add(multipleScore8);
		panel8.setVisible(false);
		contentPane.add(panel8, new GridBagConstraints(0, 7, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		JPanel panel9=new JPanel();
		panel9.setOpaque(false);
		panel9.add(new JLabel("医院名称："));
		panel9.add(hospital9);
		panel9.add(new JLabel("距离："));
		panel9.add(distance9);
		panel9.add(new JLabel("综合评分："));
		panel9.add(multipleScore9);
		panel9.setVisible(false);
		contentPane.add(panel9, new GridBagConstraints(0, 8, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		JPanel panel10=new JPanel();
		panel10.setOpaque(false);
		panel10.add(new JLabel("医院名称："));
		panel10.add(hospital10);
		panel10.add(new JLabel("距离："));
		panel10.add(distance10);
		panel10.add(new JLabel("综合评分："));
		panel10.add(multipleScore10);
		panel10.setVisible(false);
		contentPane.add(panel10, new GridBagConstraints(0, 9, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		this.setContentPane(contentPane);
		this.setVisible(true);
	}
	
	public RecommenderGUI(ArrayList<Map.Entry<Integer, Double>> CFScore){
		initComponent();
		addComponent();
	}
	}
