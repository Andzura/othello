package com.andzura.othello.main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseListener, MouseMotionListener{
	private static boolean mouseBL = false;
	private static int mouseX = 0;
	private static int mouseY = 0;
	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(arg0.getButton() == MouseEvent.BUTTON1){
			mouseBL = true;
			this.mouseX = arg0.getX();
			this.mouseY = arg0.getY();
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
	}
	
	public static int getX(){
		return mouseX;
	}
	
	public static int getY(){
		return mouseY;
	}
	
	public static boolean getClick(){
		if(mouseBL){
			mouseBL = false;
			return true;
		}
		return false;
	}
}
