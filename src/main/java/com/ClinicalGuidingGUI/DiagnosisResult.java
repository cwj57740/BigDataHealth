package com.ClinicalGuidingGUI;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.MouseInputAdapter;

import kdTree.KeyDuplicateException;
import kdTree.KeySizeException;

import com.diagnosis.Diagnosis;
import com.diagnosis.evaluater;
import com.diagnosis.mySql;

public class DiagnosisResult extends JFrame{
	public static final int DEFAULT_WIDTH=750;
	public static final int DEFAULT_HEIGHT=550;
	
	private ArrayList<Map.Entry<String, Double>> diagResult;
	private String old_symptom;
	private int patient_id;
	private MyContentPane contentPane;
	private TransparentCheckBox jcb1;
	private TransparentCheckBox jcb2;
	private TransparentCheckBox jcb3;
	private TransparentCheckBox jcb4;
	private TransparentCheckBox jcb5;
	private TransparentCheckBox jcb6;
	private TransparentCheckBox jcb7;
	private TransparentCheckBox jcb8;
	private TransparentCheckBox jcb9;
	private TransparentCheckBox jcb10;
	
	private JTextField disease1;
	private JTextField disease2;
	private JTextField disease3;
	private JTextField disease4;
	private JTextField disease5;
	private JTextField disease6;
	private JTextField disease7;
	private JTextField disease8;
	private JTextField disease9;
	private JTextField disease10;
	
	private TransparentButton button1;
	//private JButton button1;
	private TransparentButton button2;
	private TransparentButton button3;
	private TransparentButton button4;
	private TransparentButton button5;
	private TransparentButton button6;
	private TransparentButton button7;
	private TransparentButton button8;
	private TransparentButton button9;
	private TransparentButton button10;
	
	private TransparentButton optimizeButton;
	

	final class ButtonMouseActionListener extends MouseInputAdapter{
		public void mouseEntered(MouseEvent e){
			if(e.getSource()== optimizeButton){
				((TransparentButton) e.getSource()).setForeground(new Color(0, 60, 150));
			}
		}
		public void mouseExited(MouseEvent e){
			if(e.getSource()== optimizeButton){
				((TransparentButton) e.getSource()).setForeground(Color.BLACK);
			}
		}
	}
	final class ButtonActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(e.getSource()==optimizeButton){
				ArrayList<String> relatedDisease=markRelatedDisease();
				if(relatedDisease.isEmpty()){
					Map<String, String> textIcon=new HashMap<String,String>();
					textIcon.put("buttonText", "确认");
					textIcon.put("buttonIcon", "messageButton.png");
					textIcon.put("labelText", "请先勾选您认为可能性大的疾病，再点击优化诊断！");
					textIcon.put("labelIcon", "messageFailLabel.png");
					textIcon.put("titleText", "诊断失败");
					MyMessageDialog messageDialog=new MyMessageDialog(textIcon);
				}
				else{
					Diagnosis diagnosis=new Diagnosis();
					mySql ml=new mySql();
					Connection conn=ml.getConnection();
					Map<String, Integer> word_count;
					try {
						word_count = diagnosis.query_sentence(old_symptom, conn);
						setDiagnosisResult(diagnosis.feedback_query(relatedDisease, word_count, conn));
						displayResult();
						setCheckBoxNoSelect();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
			}
			if(e.getSource()==button1 || e.getSource()==button2 || e.getSource()==button3 || e.getSource()==button4 || e.getSource()==button5
					|| e.getSource()==button6 || e.getSource()==button7 || e.getSource()==button8 || e.getSource()==button9 || e.getSource()==button10 ){
				mySql ml=new mySql();
				Connection conn=ml.getConnection();
				int patient_id=getPatient_id();
				int disease_id=0;
				double range=0;//备用
				if(e.getSource()==button1){
				//String disease_id=getDiagnosisResult().get(0).getKey();
				}
				else if(e.getSource()==button2){
					//String disease_id=getDiagnosisResult().get(1).getKey();
					}
				else if(e.getSource()==button3){
					//String disease_id=getDiagnosisResult().get(2).getKey();
					}
				else if(e.getSource()==button4){
					//String disease_id=getDiagnosisResult().get(3).getKey();
					}
				else if(e.getSource()==button5){
					//String disease_id=getDiagnosisResult().get(4).getKey();
					}
				else if(e.getSource()==button6){
					//String disease_id=getDiagnosisResult().get(5).getKey();
					}
				else if(e.getSource()==button7){
					//String disease_id=getDiagnosisResult().get(6).getKey();
					}
				else if(e.getSource()==button8){
					//String disease_id=getDiagnosisResult().get(7).getKey();
					}
				else if(e.getSource()==button9){
					//String disease_id=getDiagnosisResult().get(8).getKey();
					}
				else if(e.getSource()==button10){
					//String disease_id=getDiagnosisResult().get(9).getKey();
					}
				evaluater ev=new evaluater();
				ArrayList<Map.Entry<Integer, Double>> CFScore=new ArrayList<>();
				try {
					CFScore=ev.CFEvaluator(patient_id,disease_id, range, conn);
					
				} catch (KeySizeException | KeyDuplicateException
						| SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				RecommenderGUI recommender=new RecommenderGUI(CFScore);
			}
			
		}
	}
	public ArrayList<Map.Entry<String, Double>> getDiagnosisResult(){
		return this.diagResult;
	}
	public void setCheckBoxNoSelect(){
		jcb1.setSelected(false);
		jcb2.setSelected(false);
		jcb3.setSelected(false);
		jcb4.setSelected(false);
		jcb5.setSelected(false);
		jcb6.setSelected(false);
		jcb7.setSelected(false);
		jcb8.setSelected(false);
		jcb9.setSelected(false);
		jcb10.setSelected(false);
	}
	public void setDiagnosisResult(ArrayList<Map.Entry<String, Double>> diagnosisResult){
		this.diagResult=diagnosisResult;
	}
	public ArrayList<String> markRelatedDisease(){
		ArrayList<String> relatedDisease=new ArrayList<>();
		if(jcb1.isSelected()){
			relatedDisease.add(this.diagResult.get(0).getKey());
		}
		if(jcb2.isSelected()){
			relatedDisease.add(this.diagResult.get(1).getKey());
		}
		if(jcb3.isSelected()){
			relatedDisease.add(this.diagResult.get(2).getKey());
		}
		if(jcb4.isSelected()){
			relatedDisease.add(this.diagResult.get(3).getKey());
		}
		if(jcb5.isSelected()){
			relatedDisease.add(this.diagResult.get(4).getKey());
		}
		if(jcb6.isSelected()){
			relatedDisease.add(this.diagResult.get(5).getKey());
		}
		if(jcb7.isSelected()){
			relatedDisease.add(this.diagResult.get(6).getKey());
		}
		if(jcb8.isSelected()){
			relatedDisease.add(this.diagResult.get(7).getKey());
		}
		if(jcb9.isSelected()){
			relatedDisease.add(this.diagResult.get(8).getKey());
		}
		if(jcb10.isSelected()){
			relatedDisease.add(this.diagResult.get(9).getKey());
		}
		
		return relatedDisease;
	}
	public int getPatient_id(){
		return this.patient_id;
	}
	private void initComponent(ArrayList<Map.Entry<String, Double>> diagnosisResult,String oldSymptom,int pt_id){
		this.patient_id=pt_id;
		diagResult=diagnosisResult;
		old_symptom=oldSymptom;
		contentPane=new MyContentPane();
		jcb1=new TransparentCheckBox();
		jcb2=new TransparentCheckBox();
		jcb3=new TransparentCheckBox();
		jcb4=new TransparentCheckBox();
		jcb5=new TransparentCheckBox();
		jcb6=new TransparentCheckBox();
		jcb7=new TransparentCheckBox();
		jcb8=new TransparentCheckBox();
		jcb9=new TransparentCheckBox();
		jcb10=new TransparentCheckBox();
	
		
		disease1=new JTextField(30);
		disease2=new JTextField(30);
		disease3=new JTextField(30);
		disease4=new JTextField(30);
		disease5=new JTextField(30);
		disease6=new JTextField(30);
		disease7=new JTextField(30);
		disease8=new JTextField(30);
		disease9=new JTextField(30);
		disease10=new JTextField(30);
		//disease1.setText("马钰");
		
		//button1=new TransparentButton("确认疾病");
		//button2=new TransparentButton("确认疾病");
		button1=new TransparentButton(new ImageIcon("confirmButton.png"));
		button2=new TransparentButton(new ImageIcon("confirmButton.png"));
		button3=new TransparentButton(new ImageIcon("confirmButton.png"));
		button4=new TransparentButton(new ImageIcon("confirmButton.png"));
		button5=new TransparentButton(new ImageIcon("confirmButton.png"));
		button6=new TransparentButton(new ImageIcon("confirmButton.png"));
		button7=new TransparentButton(new ImageIcon("confirmButton.png"));
		button8=new TransparentButton(new ImageIcon("confirmButton.png"));
		button9=new TransparentButton(new ImageIcon("confirmButton.png"));
		button10=new TransparentButton(new ImageIcon("confirmButton.png"));
		
		optimizeButton=new TransparentButton("优化诊断",new ImageIcon("optimizeButton.png"));
	}
	
	private void addComponent(){
		this.setTitle("智能导诊--诊断结果");
		this.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		
		contentPane.setLayout(new GridBagLayout());
		TransparentPane panel1=new TransparentPane();
		panel1.add(jcb1);
		panel1.add(disease1);
		panel1.add(button1);
		contentPane.add(panel1, new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		TransparentPane panel2=new TransparentPane();
		panel2.add(jcb2);
		panel2.add(disease2);
		panel2.add(button2);
		contentPane.add(panel2, new GridBagConstraints(0, 1, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		TransparentPane panel3=new TransparentPane();
		panel3.add(jcb3);
		panel3.add(disease3);
		panel3.add(button3);
		contentPane.add(panel3, new GridBagConstraints(0, 2, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		TransparentPane panel4=new TransparentPane();
		panel4.add(jcb4);
		panel4.add(disease4);
		panel4.add(button4);
		contentPane.add(panel4, new GridBagConstraints(0, 3, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		TransparentPane panel5=new TransparentPane();
		panel5.add(jcb5);
		panel5.add(disease5);
		panel5.add(button5);
		contentPane.add(panel5, new GridBagConstraints(0, 4, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		TransparentPane panel6=new TransparentPane();
		panel6.add(jcb6);
		panel6.add(disease6);
		panel6.add(button6);
		contentPane.add(panel6, new GridBagConstraints(0, 5, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		TransparentPane panel7=new TransparentPane();
		panel7.add(jcb7);
		panel7.add(disease7);
		panel7.add(button7);
		contentPane.add(panel7, new GridBagConstraints(0, 6, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		TransparentPane panel8=new TransparentPane();
		panel8.add(jcb8);
		panel8.add(disease8);
		panel8.add(button8);
		contentPane.add(panel8, new GridBagConstraints(0, 7, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		TransparentPane panel9=new TransparentPane();
		panel9.add(jcb9);
		panel9.add(disease9);
		panel9.add(button9);
		contentPane.add(panel9, new GridBagConstraints(0, 8, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		TransparentPane panel10=new TransparentPane();
		panel10.add(jcb10);
		panel10.add(disease10);
		panel10.add(button10);
		contentPane.add(panel10, new GridBagConstraints(0, 9, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		
		TransparentPane panel11=new TransparentPane();
		panel11.add(optimizeButton);
		contentPane.add(panel11, new GridBagConstraints(0, 12, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		this.setContentPane(contentPane);
		this.setVisible(true);
		
	
	}
	public void addListener(){
		ButtonMouseActionListener bmal=new ButtonMouseActionListener();
		optimizeButton.addMouseListener(bmal);
		ButtonActionListener bal=new ButtonActionListener();
		optimizeButton.addActionListener(bal);
		button1.addActionListener(bal);
		button2.addActionListener(bal);
		button3.addActionListener(bal);
		button4.addActionListener(bal);
		button5.addActionListener(bal);
		button6.addActionListener(bal);
		button7.addActionListener(bal);
		button8.addActionListener(bal);
		button9.addActionListener(bal);
		button10.addActionListener(bal);
	}
	public void displayResult() throws SQLException{
		ArrayList<String> diseaseName=new ArrayList<>();
		mySql ml=new mySql();
		Connection conn=ml.getConnection();
		String sql;
		for (int index = 0; index < this.diagResult.size()&& index<10; index++) {
			sql="select name from complete_disease where ICD='"+this.diagResult.get(index).getKey()+"'";
			ResultSet rs=ml.selectData(conn, sql);
			if(rs.next()){
				diseaseName.add(rs.getString("name"));
			}
			
		}
		int index=0;
		disease1.setText(diseaseName.get(index++));
		disease2.setText(diseaseName.get(index++));
		disease3.setText(diseaseName.get(index++));
		disease4.setText(diseaseName.get(index++));
		disease5.setText(diseaseName.get(index++));
		disease6.setText(diseaseName.get(index++));
		disease7.setText(diseaseName.get(index++));
		disease8.setText(diseaseName.get(index++));
		disease9.setText(diseaseName.get(index++));
		disease10.setText(diseaseName.get(index++));
	}
	public  DiagnosisResult(ArrayList<Map.Entry<String, Double>> diagnosisResult,String oldSymptom,int patient_id) throws SQLException{
		initComponent(diagnosisResult,oldSymptom,patient_id);
		addComponent();
		addListener();
		displayResult();
	}
}
