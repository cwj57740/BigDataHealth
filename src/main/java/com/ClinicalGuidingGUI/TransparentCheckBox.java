package com.ClinicalGuidingGUI;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;

public class TransparentCheckBox extends JCheckBox{
	public TransparentCheckBox(){
		this.setContentAreaFilled(false);
		this.setBorderPaintedFlat(false);
	}
}
