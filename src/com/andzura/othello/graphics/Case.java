package com.andzura.othello.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Case extends JPanel{
	private int content;
	
	public Case(){
		this(0);
	}
	public Case(int content){
		this.content = content;
		this.setBackground(Color.decode("0x006400"));
	}
	
	@Override 
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if(content == 1){
			g.setColor(Color.WHITE);
			g.fillOval((this.getHeight())/8,(this.getHeight())/8, 3*(this.getHeight())/4, 3*(this.getHeight())/4);
		}
		else if(content == 2){
			g.setColor(Color.BLACK);
			g.fillOval((this.getHeight())/8,(this.getHeight())/8, 3*(this.getHeight())/4, 3*(this.getHeight())/4);
		}
	}
	
	public void setContent(int content) {
		this.content = content;
	}
	
	public int getContent() {
		return content;
	}
	
	public void select(){
		this.setBackground(Color.RED);
	}
	
	public void unselect(){
		this.setBackground(Color.decode("0x006400"));
	}
}
