package com.andzura.othello.statemanager.states;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayDeque;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.andzura.othello.controller.PlayController;
import com.andzura.othello.graphics.Case;
import com.andzura.othello.main.Mouse;
import com.andzura.othello.model.Board;
import com.andzura.othello.statemanager.State;
import com.andzura.othello.statemanager.StateManager;

public class PlayState extends State{
	private Case[] cases;
	private Board board;
	private JPanel gameBoard;
	private boolean updateNeeded = true;
	private int playerTurn = 2;
	private PlayController controller;
	
	public PlayState(StateManager manager) {
		super(manager);
		gameBoard = new JPanel(){
					@Override
				    public Dimension getPreferredSize() {
				        Dimension d = this.getParent().getSize();
				        int size = d.width > d.height ? d.height : d.width;
				        size = size == 0 ? 100 : size;
				        return new Dimension(size, size);
				    }
				};
		gameBoard.addMouseListener(new Mouse());
		board = new Board(8,8);
		controller = new PlayController(true, board);
		cases = new Case[8*8];
	}
	
	@Override
	public void init() {
		GridLayout layout = new GridLayout(8,8);
		layout.setHgap(2);
		layout.setVgap(2);
		screen.setLayout(new FlowLayout());
		screen.add(gameBoard);
		gameBoard.setLayout(layout);
		for(int i = 0; i < 8*8; i++){
			cases[i] = new Case();
			gameBoard.add(cases[i]);
		}
	}

	@Override
	public void update(long elapsedTime) {
		// TODO Auto-generated method stub
		if(updateNeeded){
			updateNeeded = controller.update();
			for(int i = 0; i < 8; i++){
				for(int j = 0; j < 8; j++){
					if(controller.isPlayable(i, j)){
						cases[i+j*8].select();
					}else{
						cases[i+j*8].unselect();
					}
				}
			}
			this.render();
			
		}
		if(Mouse.getClick()){
			updateNeeded = true;
			int x = Mouse.getX()/(gameBoard.getWidth()/8);
			int y = Mouse.getY()/(gameBoard.getHeight()/8);
			controller.play(x, y);

		}
		
	}

	
	@Override
	public void render() {
		for(int i = 0; i < 8*8; i++){
			cases[i].setContent(board.getSquareContent(i));
		}
		this.screen.repaint();
	}

	@Override
	public void exit() {
		// TODO Auto-generated method stub
		
	}

}
