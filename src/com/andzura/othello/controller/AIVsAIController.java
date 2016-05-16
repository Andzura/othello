package com.andzura.othello.controller;

import com.andzura.othello.ai.AI;
import com.andzura.othello.model.Board;

public class AIVsAIController extends PlayController{

	private AI aiPlayer2;

	public AIVsAIController(Board board, AI ai1, AI ai2) {
		super(board);
		aiPlayer = ai1;
		aiPlayer2 = ai2;
	}
	
	public boolean update() {
		if(!this.canPlay())
			playerTurn = playerTurn%2 + 1;
		int pos;
		if(playerTurn == 2)
			pos = aiPlayer.play(board, playerTurn,turnNumber);
		else
			pos = aiPlayer2.play(board, playerTurn,turnNumber);
		int x = pos%8;
		int y = pos/8;
		turnNumber++;
		if(board.play(x, y, playerTurn)){
			playerTurn = playerTurn%2 + 1;
			return true;
		}
		playerTurn = playerTurn%2 + 1;
		return false;
	}

}
