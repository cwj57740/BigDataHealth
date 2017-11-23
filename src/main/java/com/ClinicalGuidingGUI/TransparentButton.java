package com.ClinicalGuidingGUI;

import javax.swing.Icon;
import javax.swing.JButton;

public class TransparentButton extends JButton{
	
	public TransparentButton(String text){
		super(text);
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
	}
	public TransparentButton(Icon icon){
		super(icon);
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
	}
	public TransparentButton(String text,Icon icon){
		super(text,icon);
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
	}
}
