package com.andzura.othello.statemanager.states;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

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
			boolean canPlay = false;
			updateNeeded = false;
			for(int i = 0; i < 8; i++){
				for(int j = 0; j < 8; j++){
					if(board.isPlayable(i, j, playerTurn)){
						cases[i+j*8].select();
						canPlay = true;
					}else{
						cases[i+j*8].unselect();
					}
				}
			}
			if(!canPlay){
				updateNeeded = true;
				playerTurn = playerTurn%2 + 1;
			}
			this.render();
		}
		if(Mouse.getClick()){
			updateNeeded = true;
			if(board.play(Mouse.getX()/(screen.getWidth()/8), Mouse.getY()/(screen.getHeight()/8), playerTurn)){
				playerTurn = playerTurn%2 + 1;
			}
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
