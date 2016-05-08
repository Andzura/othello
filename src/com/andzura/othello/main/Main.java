package com.andzura.othello.main;

import javax.swing.JFrame;

import com.andzura.othello.statemanager.StateManager;
import com.andzura.othello.statemanager.states.PlayState;

public class Main {

	public static void main(String[] args) {
		JFrame jframe = new JFrame();
		StateManager manager = new StateManager();
		PlayState state = new PlayState(manager);
		manager.addState(state, "play");
		manager.push("play");
		jframe.add(manager.getScreen());
		jframe.setSize(400, 400);
		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		boolean running = true;
		long startTimer = System.currentTimeMillis();
		long endTimer;
		jframe.add(manager.getScreen());
		jframe.pack();
		while(running){
			endTimer = System.currentTimeMillis();
			if(endTimer - startTimer > 100){
				jframe.add(manager.getScreen());
				jframe.validate();
				manager.update(endTimer -startTimer);
				manager.render();
				startTimer = endTimer;
			}
		}
	}

}
