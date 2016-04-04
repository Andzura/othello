package com.andzura.othello.statemanager;


import javax.swing.JPanel;

import com.andzura.othello.main.Game;

public abstract class State {
	
	protected StateManager manager;
	protected JPanel screen;
	
	public State(StateManager manager){
		this.manager = manager;	
		this.screen = new JPanel();
	}
	public JPanel getScreen(){
		return screen;
	}
	
	public abstract void init();
	
	public abstract void update(long elapsedTime);
	
	public abstract void render();
	
	public abstract void exit();

}
