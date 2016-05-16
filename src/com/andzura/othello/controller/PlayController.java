package com.andzura.othello.controller;

import com.andzura.othello.ai.AI;
import com.andzura.othello.model.Board;

public class PlayController {
	public static final int EASY = 1;
	public static final int NORMAL = 2;
	public static final int HARD = 3;
	protected boolean ai;
	protected int playerTurn;
	protected Board board;
	protected int turnNumber;
	protected AI aiPlayer;
	
	public PlayController(Board board){
		this(false,board);
	}
	
	public PlayController(boolean ai, Board board){
		this(ai, board, HARD);
	}
	
	public PlayController(boolean ai, Board board, int IALevel){
		this.ai = ai;
		this.board = board;
		this.playerTurn = 2;
		this.turnNumber = 1;
		if(ai){
			if(IALevel == NORMAL)
				aiPlayer = new AI(5,100,1,0,0,0);
			else if(IALevel == EASY)
				aiPlayer = new AI(5,0,1,0,0,0);
			else
				aiPlayer = new AI(500);
		}
	}
	
	public void play(int x, int y){
		if(!ai || playerTurn == 2){
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
		if(ai && playerTurn == 1){
			int pos = aiPlayer.play(board, playerTurn,turnNumber);
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
