package com.andzura.othello.statemanager.states;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JPanel;

import com.andzura.othello.graphics.Case;
import com.andzura.othello.main.Mouse;
import com.andzura.othello.statemanager.State;
import com.andzura.othello.statemanager.StateManager;
import com.andzura.othello.utils.Logger;


public class EndState extends State {
	private Font font;
	
	public EndState(StateManager manager, String winner) {
		super(manager);
		JPanel endscreen;
		font = null;
		endscreen = new JPanel(){
			@Override
			public void paint(Graphics g){
				Graphics2D g2;
				if (g instanceof Graphics2D) {
						g2 = (Graphics2D) g;
				}
				else {
						Logger.println("Error: g is not an instance of Graphics2D", Logger.ERROR);
						return;
				}
				g2.setColor(Color.BLACK);
				g2.fillRect(0, 0, this.getWidth(), this.getHeight());
				g2.setColor(Color.WHITE);
				g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30));
				FontMetrics fm = g2.getFontMetrics();
				g2.drawString( winner + " wins.",
						this.getWidth()/2 - fm.stringWidth("Player "+ winner + " wins.")/2, this.getHeight()/2);
				}
		};
		screen = endscreen;
		screen.addMouseListener(new Mouse());
		screen.setBackground(Color.BLACK);
			
		
	}

	@Override
	public void init() {
		// Nothing to do here;
	}

	@Override
	public void update(long elapsedTime) {
		if(Mouse.getClick()){
			manager.pop();
			manager.push(new MenuState(manager));
		}
	}

	@Override
	public void render() {
		screen.repaint();
	}

	@Override
	public void exit() {
		// Nothing to do here
	}

}
