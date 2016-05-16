package com.andzura.othello.statemanager.states;

import java.awt.Button;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.andzura.othello.controller.PlayController;
import com.andzura.othello.statemanager.State;
import com.andzura.othello.statemanager.StateManager;

public class MenuState extends State {

	public MenuState(StateManager manager) {
		super(manager);
	}

	@Override
	public void init() {
		screen = new JPanel();
		screen.setLayout(new GridLayout(5,1));
		Button pvpButton = new Button("Joueur Contre Joueur");
		screen.add(pvpButton);
		Button easyButton = new Button("IA Facile");
		screen.add(easyButton);
		Button normalButton = new Button("IA Normal");
		screen.add(normalButton);
		Button hardButton = new Button("IA Difficile");
		screen.add(hardButton);
		Button exitButton = new Button("Quitter");
		screen.add(exitButton);
		exitButton.addActionListener((new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
			
		}));
		
		pvpButton.addActionListener((new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				manager.pop();
				manager.push(new PlayState(manager,false));
			}
			
		}));
		
		easyButton.addActionListener((new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				manager.pop();
				manager.push(new PlayState(manager,PlayController.EASY));
			}
			
		}));
		
		normalButton.addActionListener((new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				manager.pop();
				manager.push(new PlayState(manager,PlayController.NORMAL));
			}
			
		}));
		
		hardButton.addActionListener((new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				manager.pop();
				manager.push(new PlayState(manager,PlayController.HARD));
			}
			
		}));
	}

	@Override
	public void update(long elapsedTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {
		// TODO Auto-generated method stub

	}

	@Override
	public void exit() {
		// TODO Auto-generated method stub

	}

}
