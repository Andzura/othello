package com.andzura.othello.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.concurrent.CountDownLatch;

import javax.swing.JFrame;

import com.andzura.othello.ai.AI;
import com.andzura.othello.controller.AIVsAIController;
import com.andzura.othello.model.Board;
import com.andzura.othello.statemanager.StateManager;
import com.andzura.othello.statemanager.states.PlayState;
import com.andzura.othello.utils.Logger;


public class AITest{
	
	public static void main(String[] args) {
		try {
			Logger.setOut(new PrintStream(new File("logAI.txt")));
		} catch (FileNotFoundException e) {
			Logger.println("error while setting log file ! " + e.getMessage(), Logger.ERROR);
		}
		//Logger.setLevel(Logger.DEBUG);
		long start = System.currentTimeMillis();
		AI ai1 = new AI(500,80,0,800,0,0);
		AI ai2 = new AI(500);
		Board board = new Board();
		AIVsAIController controller = new AIVsAIController(board, ai1, ai2);
		boolean running = true;
		while(running){
			controller.update();
			if(board.checkEndGame())
				running = false;
		}
		Logger.println("WINNER " + board.winner() + " computed in "+ (System.currentTimeMillis() - start)+ " ms", Logger.INFO);
		
	}
	

}
