package com.ClinicalGuidingGUI;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class MyMessageBackGround extends JPanel{
	Image bg;
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(bg, 0, 0, this.getWidth(), this.getHeight(), this);
	}
	public MyMessageBackGround(){
		bg=Toolkit.getDefaultToolkit().getImage("messageBackGround.jpg");
		this.setOpaque(false);
		
	}
}
