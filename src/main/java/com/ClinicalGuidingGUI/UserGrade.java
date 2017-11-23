package com.ClinicalGuidingGUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class UserGrade extends JFrame{
	public static final int DEFAULT_WIDTH=700;
	public static final int DEFAULT_HEIGHT=500;
	private JComboBox<Integer> effect;
	private JComboBox<Integer> charge;
	private JComboBox<Integer> attitude;
	private JButton confirmButton;
	
	
	
	public void initComponent(){
		effect=new JComboBox<Integer>();
		charge=new JComboBox<Integer>();
		attitude=new JComboBox<Integer>();
		
		for(int i=0;i<10;++i){
			effect.addItem(i+1);
			charge.addItem(i+1);
			attitude.addItem(i+1);
		}
		confirmButton=new JButton("提交");
		confirmButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(null,"感谢您的评价！","评价完成",JOptionPane.PLAIN_MESSAGE);
			}
		});
	}
	private void addComponent(){
		this.setTitle("智能导诊--用户评价");
		this.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
		this.setLocationRelativeTo(null);
		this.setLayout(new GridBagLayout());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		
		JPanel titlePanel=new JPanel();
		JLabel titleLabel=new JLabel("评价医院");
		titleLabel.setFont(new Font("宋体", Font.PLAIN, 25));
		titlePanel.add(titleLabel);
		this.add(titlePanel, new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		this.add(new JPanel(), new GridBagConstraints(0, 1, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		this.add(new JPanel(), new GridBagConstraints(0, 2, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		this.add(new JPanel(), new GridBagConstraints(0, 3, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0),
						0, 0));
		this.add(new JPanel(), new GridBagConstraints(0, 4, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		JPanel scorePanel=new JPanel();
		scorePanel.add(new JLabel("治疗效果"));
		scorePanel.add(effect);
		scorePanel.add(new JPanel());
		scorePanel.add(new JPanel());
		scorePanel.add(new JLabel("收费水平"));
		scorePanel.add(charge);
		scorePanel.add(new JPanel());
		scorePanel.add(new JPanel());
		scorePanel.add(new JLabel("服务态度"));
		scorePanel.add(attitude);
		this.add(scorePanel, new GridBagConstraints(0, 5, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		this.add(new JPanel(), new GridBagConstraints(0, 6, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		this.add(new JPanel(), new GridBagConstraints(0, 7, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		this.add(new JPanel(), new GridBagConstraints(0, 8, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		JPanel confirmPanel=new JPanel();
		confirmPanel.add(confirmButton);
		this.add(confirmPanel, new GridBagConstraints(0, 10, 1, 1, 0, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0));
		
		this.setVisible(true);
	}
	
	public UserGrade(){
		initComponent();
		addComponent();
	}
}
