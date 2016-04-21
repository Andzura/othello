package com.andzura.othello.controller;

import java.util.ArrayDeque;
import java.util.Deque;

public class AI {

	public static int[] play(int[] board, int playerTurn){
		Deque<Integer> playable = new ArrayDeque<>();
		long start = System.nanoTime();
		isPlayable(4+5*8, board, playerTurn, new ArrayDeque<Integer>(), playable);
		long end = System.nanoTime();
		float time = (end - start);
		System.out.println(playable.toString() + " computed in " + time + "ns.");
		return null;
	}
	
	private static int evaluate(int[] board, int player){
		return 0;
	}
	
	private static void isPlayable(int square,int[] board, int player, Deque<Integer> played, Deque<Integer> playable){
		if(played.contains(square)){
			return;
		}
		int x = square%8;
		int y = square/8;
		played.addFirst(square);
		if(board[square] != 0){
			if(x+1<8)
				isPlayable(y*8+(x+1),board,player,played,playable);
			if(x-1>=0)
				isPlayable(y*8+(x-1),board,player,played,playable);
			if(y+1<8)
				isPlayable((y+1)*8+x,board,player,played,playable);
			if(y-1>0)
				isPlayable((y-1)*8+x,board,player,played,playable);
			return;
		}
		
		{
			int width = 8;
			int height = 8;
			int curX, curY;
			for(int i = -1 ; i <= 1; i++){
				for(int j = -1; j<=1 ; j++){
					if(i != 0 || j != 0){
						curX = x+i;
						curY = y+j;
						if(curX > 0 && curX < width
								&& curY > 0 && curY < height){
							if(board[curX+curY*8] != 0 && board[curX+curY*8]  != player){
								do{
									curX += i;
									curY += j;
								}while(curX > 0 && curX < width
										&& curY > 0 && curY < height
										&& board[curX+curY*8]!= 0 
										&& board[curX+curY*8]!= player);
								if(board[curX+curY*8] == player){
									playable.addFirst(square);
									break;
								}
							}
						}
					}
				}
			}
		}
		
			if((x+1 < 8) && board[(x+1)+y*8] != 0
					||(x-1 >= 0) && board[(x-1)+y*8] != 0){
				if(y+1<8)
					isPlayable((y+1)*8+x,board,player,played,playable);
				if(y-1>0)
					isPlayable((y-1)*8+x,board,player,played,playable);
				return;
			}
			if((y+1 < 8) && board[x+(y+1)*8] != 0
					||(y-1 >= 0) && board[x+(y-1)*8] != 0){
				if(x+1<8)
					isPlayable(y*8+x+1,board,player,played,playable);
				if(x-1>0)
					isPlayable(y*8+x-1,board,player,played,playable);
				return;
			}
			if(y+1 < 8 && x+1 < 8 && board[x+1+(y+1)*8] != 0){
				isPlayable(y*8+(x+1),board,player,played,playable);
				isPlayable((y+1)*8+x,board,player,played,playable);
				return;
			}
			if(y-1 > 0 && x-1 > 0 && board[x-1+(y-1)*8] != 0){
				isPlayable(y*8+(x-1),board,player,played,playable);
				isPlayable((y-1)*8+x,board,player,played,playable);
				return;
			}
			if(y+1 < 8 && x-1 > 0 && board[x-1+(y+1)*8] != 0){
				isPlayable(y*8+(x-1),board,player,played,playable);
				isPlayable((y+1)*8+x,board,player,played,playable);
				return;
			}
			if(y-1 > 0 && x+1 < 8 && board[x+1+(y-1)*8] != 0){
				isPlayable(y*8+(x+1),board,player,played,playable);
				isPlayable((y-1)*8+x,board,player,played,playable);
				return;
			}
		
	}
	
}
