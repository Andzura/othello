package com.andzura.othello.controller;

import com.andzura.othello.model.Board;

public class PlayController {
	
	private boolean ai;
	private int playerTurn;
	private Board board;
	private int turnNumber;
	private AI theAI;
	private AI theAI2;
	
	public PlayController(Board board){
		this(false,board);
	}
	
	public PlayController(boolean ai, Board board){
		this.ai = ai;
		this.board = board;
		this.playerTurn = 2;
		this.turnNumber = 1;
		theAI = new AI(1000);
		theAI2 = new AI(1000);
	}
	
	public void play(int x, int y){
		if(!ai || playerTurn == 1){
			if(board.play(x, y, playerTurn)){
				playerTurn = playerTurn%2 + 1;
				turnNumber++;
			}
		}
		
	}

	public boolean isPlayable(int i, int j) {
		return board.isPlayable(i, j, playerTurn);
	}

	public void changePlayer() {
		playerTurn = playerTurn%2 + 1;
	}
	
	public boolean update() {
		if(!this.canPlay())
			playerTurn = playerTurn%2 + 1;
		if(ai){
			int pos;
			if(playerTurn == 2)
				pos = theAI.play(board, playerTurn,turnNumber);
			else
				pos = theAI2.play(board, playerTurn,turnNumber);
			int x = pos%8;
			int y = pos/8;
			turnNumber++;
			if(board.play(x, y, playerTurn)){
				playerTurn = playerTurn%2 + 1;
				return true;
			}
			playerTurn = playerTurn%2 + 1;
		}
		return false;
	}
	
	
	public boolean canPlay(){
		for(int i = 0; i < board.getHeight() * board.getWidth(); i++){
			if((board.isPlayable(i%8, i/8, playerTurn)))
					return true;
		}
		return false;
	}

}
