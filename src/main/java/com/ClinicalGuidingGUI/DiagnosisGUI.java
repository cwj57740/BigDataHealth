package com.ClinicalGuidingGUI;

import java.awt.BorderLayout;
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
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.MouseInputAdapter;

import org.apache.commons.collections.functors.IfClosure;

import com.diagnosis.Diagnosis;
import com.diagnosis.mySql;

public class DiagnosisGUI extends JFrame{
	private int patient_id;
	private MyContentPane contentPane;
	private JTextArea symptom;
	private TransparentButton diagButton;
	private TransparentButton clearButton;
	public static final int DEFAULT_WIDTH=700;
	public static final int DEFAULT_HEIGHT=500;
	
	final class ButtonMouseActionListener extends MouseInputAdapter{
		public void mouseEntered(MouseEvent e){
			if(e.getSource()==diagButton||e.getSource()==clearButton){
				((TransparentButton) e.getSource()).setForeground(new Color(0, 60, 150));
			}
		}
		public void mouseExited(MouseEvent e){
			if(e.getSource()==diagButton||e.getSource()==clearButton){
				((TransparentButton) e.getSource()).setForeground(Color.BLACK);
			}
		}
	}
	final class ButtonActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(e.getSource()==diagButton){
				String symptomString=symptom.getText();
				if(!symptomString.isEmpty()){
					Diagnosis diagnosis=new Diagnosis();
					mySql ml=new mySql();
					Connection conn=ml.getConnection();
					
					try {
						ArrayList<Map.Entry<String, Double>> diagnosisResult=diagnosis.diagnosis(symptomString, conn);
						System.out.println(diagnosisResult);
						if(diagnosisResult.size()<10){
							Map<String, String> textIcon=new HashMap<String,String>();
							textIcon.put("buttonText", "确认");
							textIcon.put("buttonIcon", "messageButton.png");
							textIcon.put("labelText", "您输入的症状信息太少，请多输入一些吧！");
							textIcon.put("labelIcon", "messageFailLabel.png");
							textIcon.put("titleText", "诊断失败");
							MyMessageDialog messageDialog=new MyMessageDialog(textIcon);
						}
						else{
							DiagnosisResult diagResult=new DiagnosisResult(diagnosisResult,symptomString,getPatient_id());
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else{
					Map<String, String> textIcon=new HashMap<String,String>();
					textIcon.put("buttonText", "确认");
					textIcon.put("buttonIcon", "messageButton.png");
					textIcon.put("labelText", "症状信息为空，请输入症状后再进行诊断！");
					textIcon.put("labelIcon", "messageFailLabel.png");
					textIcon.put("titleText", "诊断失败");
					MyMessageDialog messageDialog=new MyMessageDialog(textIcon);
				}
			}
			if(e.getSource()==clearButton){
				symptom.setText("");
			}
		}
		
	}
	public int getPatient_id(){
		return this.patient_id;
	}
	private void initComponet(int pt_id){
		patient_id=pt_id;
		contentPane=new MyContentPane();
		symptom=new JTextArea(6, 60);
		diagButton=new TransparentButton("诊断",new ImageIcon("diagnosisButton.png"));
		clearButton=new TransparentButton("清空",new ImageIcon("clearButton.png"));
	}
	
	private void addComponent(){
		
		this.setTitle("智能导诊--疾病诊断");
		this.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		
		contentPane.setLayout(new GridBagLayout());
		contentPane.add(new TransparentPane(), new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		contentPane.add(new TransparentPane(), new GridBagConstraints(0, 1, 1, 4, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		TransparentPane symptomPanel=new TransparentPane();
		symptomPanel.add(symptom);
		contentPane.add(symptomPanel, new GridBagConstraints(1, 1, 12, 4, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		contentPane.add(new TransparentPane(), new GridBagConstraints(13, 1, 1, 4, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		contentPane.add(new TransparentPane(), new GridBagConstraints(0, 7, 1, 4, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		
		TransparentPane buttonPanel=new TransparentPane();
		buttonPanel.add(diagButton);
		buttonPanel.add(new TransparentPane());
		buttonPanel.add(new TransparentPane());
		buttonPanel.add(clearButton);
		contentPane.add(buttonPanel, new GridBagConstraints(12, 12, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
	
		this.setContentPane(contentPane);
		this.setVisible(true);
	}
	public void addListener(){
		ButtonMouseActionListener bmal=new ButtonMouseActionListener();
		diagButton.addMouseListener(bmal);
		clearButton.addMouseListener(bmal);
		ButtonActionListener bal=new ButtonActionListener();
		diagButton.addActionListener(bal);
		clearButton.addActionListener(bal);
		
	}
	public DiagnosisGUI(int pt_id){
		initComponet(pt_id);
		addComponent();
		addListener();
	}
}
