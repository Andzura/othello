package com.andzura.othello.main;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

import com.andzura.othello.main.Game;
import com.andzura.othello.statemanager.states.PlayState;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame jframe = new JFrame();
		PlayState state = new PlayState(null);
		jframe.add(state.getScreen());
		jframe.setSize(400, 400);
		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		state.init();
		jframe.pack();
		boolean running = true;
		long startTimer = System.currentTimeMillis();
		long endTimer;
		while(running){
			endTimer = System.currentTimeMillis();
			if(endTimer - startTimer > 100){
				state.update(0);
				startTimer = endTimer;
			}
		}
	}

}
