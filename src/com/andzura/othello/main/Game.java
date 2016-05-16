package com.andzura.othello.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import javax.swing.JFrame;

import com.andzura.othello.statemanager.StateManager;
import com.andzura.othello.statemanager.states.MenuState;
import com.andzura.othello.statemanager.states.PlayState;
import com.andzura.othello.utils.Logger;

public class Game{
	public static void main(String[] args) {
		try {
			Logger.setOut(new PrintStream(new File("log.txt")));
		} catch (FileNotFoundException e) {
			Logger.println("error while setting log file ! " + e.getMessage(), Logger.ERROR);
		}
		// Uncomment this line to get full log.
		// Logger.setLevel(Logger.DEBUG);
		JFrame jframe;
		StateManager manager = new StateManager();
		MenuState state = new MenuState(manager);;
		manager.push(state);
		jframe = manager.getScreen();
		jframe.setSize(400, 400);
		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		boolean running = true;
		long startTimer = System.currentTimeMillis();
		long endTimer;
		while(running){
			endTimer = System.currentTimeMillis();
			if(endTimer - startTimer > 100){
				jframe.validate();
				manager.update(endTimer -startTimer);
				manager.render();
				startTimer = endTimer;
			}
		}
	}
}
