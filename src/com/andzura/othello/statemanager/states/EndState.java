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

import me.grea.antoine.utils.Log;

import com.andzura.othello.graphics.Case;
import com.andzura.othello.statemanager.State;
import com.andzura.othello.statemanager.StateManager;


public class EndState extends State {
	private Font font;
	
	public EndState(StateManager manager, String winner) {
		super(manager);
		JPanel endscreen;
		font = null;
		InputStream is = EndState.class.getResourceAsStream("/font/cp437-8x8.ttf");
		if(is != null){
			try {
				font = Font.createFont(Font.TRUETYPE_FONT, is);
			} catch (FontFormatException | IOException e) {
				Log.f(e.getMessage());
			}
		}else
			Log.f("Font not found");
		try {
			is.close();
		} catch (IOException e) {
			Log.f(e.getMessage());
		}
		font = font.deriveFont(Font.BOLD, 15);
		endscreen = new JPanel(){
			@Override
			public void paint(Graphics g){
				Graphics2D g2;
				if (g instanceof Graphics2D) {
						g2 = (Graphics2D) g;
				}
				else {
						Log.e(" g isn't an instance of Graphics2D.");
						return;
				}
				g2.setColor(Color.BLACK);
				g2.fillRect(0, 0, this.getWidth(), this.getHeight());
				g2.setColor(Color.WHITE);
				g2.setFont(font);
				FontMetrics fm = g2.getFontMetrics();
				g2.drawString( winner + " wins.",
						this.getWidth()/2 - fm.stringWidth("Player "+ winner + " wins.")/2, this.getHeight()/2);
				}
		};
		screen = endscreen;
		screen.setBackground(Color.BLACK);
			
		
	}

	@Override
	public void init() {
		// Nothing to do here;
	}

	@Override
	public void update(long elapsedTime) {
		// Nothing to do here;
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
