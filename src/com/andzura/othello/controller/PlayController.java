package com.andzura.othello.controller;

import com.andzura.othello.model.Board;

public class PlayController {
	
	private boolean ai;
	private int playerTurn;
	private Board board;
	
	public PlayController(Board board){
		this(false,board);
	}
	
	public PlayController(boolean ai, Board board){
		this.ai = ai;
		this.board = board;
		this.playerTurn = 2;
	}
	
	public void play(int x, int y){
		if(!ai || playerTurn == 2){
			if(board.play(x, y, playerTurn)){
				playerTurn = playerTurn%2 + 1;
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
		if(ai && playerTurn == 1){
			int pos = AI.play(board.getBoard(), playerTurn);
			int x = pos%8;
			int y = pos/8;
			if(board.play(x, y, playerTurn)){
				playerTurn = playerTurn%2 + 1;
				return true;
			}else{
				playerTurn = playerTurn%2 + 1;
			}
		}
		return false;
	}
}
