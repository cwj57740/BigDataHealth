package com.ClinicalGuidingGUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.event.MouseInputAdapter;

public class MyMessageDialog extends JFrame{
	private TransparentButton confirmButton;
	private MyMessageBackGround contentPane;
	private JLabel label;
	
	
	final class ButtonActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(e.getSource()==confirmButton){
				dispose();
			}
		}
	}
	
	final class ButtonMouseActionListener extends MouseInputAdapter{
		public void mouseEntered(MouseEvent e){
			if(e.getSource()==confirmButton){
				((TransparentButton) e.getSource()).setForeground(new Color(0, 60, 200));
			}
		}
		public void mouseExited(MouseEvent e){
			if(e.getSource()==confirmButton){
				((TransparentButton) e.getSource()).setForeground(Color.BLACK);
			}
		}
	}
	
	private void initComponent(Map<String,String> textIcon){
		confirmButton=new TransparentButton(textIcon.get("buttonText"),new ImageIcon(textIcon.get("buttonIcon")));
		label=new JLabel(textIcon.get("labelText"),new ImageIcon(textIcon.get("labelIcon")),SwingConstants.LEFT);
		label.setFont(new Font("»ªÎÄÐÂÎº", Font.PLAIN, 16));
		label.setForeground(new Color(0, 60, 150));
		contentPane=new MyMessageBackGround();
		
		this.setTitle(textIcon.get("titleText"));
		//this.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
	}
	
	private void addComponent(){
		TransparentPane labelPane=new TransparentPane();
		labelPane.add(label);
		
		TransparentPane buttonPane=new TransparentPane();
		buttonPane.add(confirmButton);
		
		contentPane.setLayout(new GridLayout(2,1));
		contentPane.add(labelPane);
		contentPane.add(buttonPane);
		
		this.setContentPane(contentPane);
		pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	public void addListener(){
		ButtonActionListener bal=new ButtonActionListener();
		confirmButton.addActionListener(bal);
		ButtonMouseActionListener bmal=new ButtonMouseActionListener();
		confirmButton.addMouseListener(bmal);
	}
	public MyMessageDialog(Map<String,String> textIcon){
		initComponent(textIcon);
		addComponent();
		addListener();
	}
}
