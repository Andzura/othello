package com.andzura.othello.controller;

import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import me.grea.antoine.utils.Dice;

public class AI {
        private static int WIDTH = 8;
        private static int  HEIGHT = 8;
	private static int MINUSINFINITE = Integer.MIN_VALUE;
	private static int PLUSINFINITE = Integer.MAX_VALUE;
	private static HashMap<BitSet, Integer[]> lookupTable = new HashMap<>();
	
	public static int play(byte[] board, int player){
		long start = System.currentTimeMillis();
		System.out.println("START");
		Deque<Byte> playable = playableSquare(board, player);
		orderMove(board, playable, player);
		int[][] scores = new int[playable.size()][2];
		//compute score of every possible move.
		{
			int alpha = MINUSINFINITE;
			int beta = PLUSINFINITE;
			int size = playable.size();
			for(int i = 0; i < size; i++){
				scores[i][0] = playable.pop();
				board[scores[i][0]] = (byte)player;
				scores[i][1] = -alphaBeta(8, board,-beta, -alpha, player%2+1);
				if(scores[i][1] > alpha){
					alpha = scores[i][1];
				}
				board[scores[i][0]] = 0;
			}
		}
		playable = null;
		//find the best move
		int best = 0;
		int bestScore = MINUSINFINITE;
		for(int i = 0; i < scores.length; i++){
			System.out.println("move " + scores[i][0] + " has a score of "+ scores[i][1]);
			if(scores[i][1] >= bestScore){
				if(scores[i][1] > bestScore || Dice.roll(6)%2 == 0){
					bestScore = scores[i][1];
					best = scores[i][0];
				}
			}
		}
		long end = System.currentTimeMillis();
		float time = (end - start);
		System.out.println("best move is: "+ best + " computed in " + time + "ms.");
		Iterator it = lookupTable.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        if(((Integer[])(pair.getValue()))[1] == 5){
	        	lookupTable.remove((BitSet)pair.getKey());	        	
	        }
	        else{
	        	((Integer[])(pair.getValue()))[1]++;
	        	lookupTable.put((BitSet)pair.getKey(), (Integer[])pair.getValue());
	        }
	        it.remove();
	    }
		return best;
	}
	
	private static int alphaBeta(int depth, byte[] board, int alpha, int beta, int player) {
		int score;
                if((score = hasAlreadyBeenTested(board)) != MINUSINFINITE)
			return score;
                BitSet hash = new BitSet(board.length * 2);
                for(int i = 0; i<board.length; i++){
			if(board[i] == 1)
				hash.set(i*2);
			else if(board[i] == 2)
				hash.set(i*2+1);
		}
		Deque<Byte> playable = playableSquare(board, player);
		if(depth == 0 || playable.size() == 0){
			return evaluate(board, player);
		}
		orderMove(board, playable, player);
		int best = MINUSINFINITE;
		int move;
		int size = playable.size();
		for(int i = 0; i < size; i++){
			move = playable.pop();
			board[move] = (byte)player;
			if(i == 0){
				score = -alphaBeta(depth - 1,board, -beta, -alpha, player%2+1);
			}else{
				score = -alphaBeta(depth - 1,board, -alpha-1, -alpha, player%2+1);
				if(score > alpha && score<beta)
					score = -alphaBeta(depth - 1,board, -beta, -score, player%2+1);
			}
			board[move] = 0;
			if(score > best){
				best = score;
				if(best > alpha){
					alpha = best;
						if(alpha > beta){
							lookupTable.put(hash, new Integer[]{alpha,0});
							return alpha;
						}
				}
			}
		}
		lookupTable.put(hash, new Integer[]{best,0});
		return best;
	}

	
	private static void orderMove(byte[] board, Deque<Byte> playable, int player){
		byte[] move = new byte[playable.size()];
		byte tempMove, tempMoveNumber;
		for(int i = 0; i < move.length; i++){
			tempMove = playable.pop();
			board[tempMove] = (byte)player;
			tempMoveNumber = (byte)playableSquare(board, player%2+1).size();
			board[tempMove] = 0;
			for(int j = 0; j < move.length; j++){
				if(move[j] == 0){
					move[j] = tempMove;
					break;
				}
				if(tempMoveNumber < move[j]){
					move[j] += tempMove;
					tempMove = (byte)(move[j] - tempMove);
					move[j] -= tempMove;
				}
				
			}
		}
		for(int i= 0; i < move.length; i++)
			playable.add(move[i]);
	}
	
	private static int evaluate(byte[] board, int player){
		int movePlayer = playableSquare(board, player).size();
		int moveOpponent = playableSquare(board, player%2+1).size();
		float mobilityScore = 0;
		if(movePlayer != 0 || moveOpponent != 0 )
			mobilityScore = 100*(movePlayer - moveOpponent)/(movePlayer + moveOpponent);
		float coinScore = 0;
		float coin = 0;
		for(int i = 0; i < board.length; i++){
			if(board[i] == player){
				coinScore += checkZone(i);
				coin += coinScore;
			}else if(board[i] != 0){
				coinScore -= checkZone(i);
				coin += coinScore;
			}
		}
		coinScore = 100*(coinScore/coin);
		int score =(int) (mobilityScore + coinScore);
		return score;
	}
	
	private static int checkZone(int i){
		if(i-1 == 0 || i+1==7 
				|| i-1 == 56 || i+1 == 63)
			return -50;
		if(i-8 == 0 || i - 8 == 7
				|| i+8 == 56 || i+8 == 63)
			return -50;
		if(i-9 == 0 || i-7 == 7
				|| i+7 == 56 || i+9 == 63)
			return -50;
		if(i == 0 || i == 7
					||i == 56 || i == 63)
			return 50;
		return 0;
	}
	private static Deque<Byte> playableSquare(byte[] board, int player){
		Deque<Byte> playable = new ArrayDeque<>();
		for(int i = 0; i < board.length; i++){
			if(isPlayable(i,board,player))
				playable.add((byte)i);
		}
		return playable;
	}
	private static boolean isPlayable(int square, byte[] board, int player){
			if(board[square] == 0){
				int width = 8;
				int height = 8;
				int x = square%width;
				int y = square/width;
				int curX, curY;
				for(int i = -1 ; i <= 1; i++){
					for(int j = -1; j<=1 ; j++){
						if(i != 0 || j != 0){
							curX = x+i;
							curY = y+j;
							if(curX >= 0 && curX < width
									&& curY >= 0 && curY < height){
								if(board[curX+curY*8] != 0 && board[curX+curY*8]  != player){
									do{
										curX += i;
										curY += j;
									}while(curX >= 0 && curX < width
											&& curY >= 0 && curY < height
											&& board[curX+curY*8]!= 0 
											&& board[curX+curY*8]!= player);
									if(curX >= 0 && curX < width
											&& curY >= 0 && curY < height
											&& board[curX+curY*8] == player){
										return true;
									}
								}
							}
						}
					}
				}
			}
			return false;
		
	}

    /*
        int hasAlreadyBeenTested(byte[] board)
        Return the score of a board if it had already been tested,
        Or if one of its rotation/symetry already had been.
        
        */
    private static int hasAlreadyBeenTested(byte[] board){
        BitSet hash = new BitSet(board.length * 2);
        for(int i = 0; i<board.length; i++){
                if(board[i] == 1)
                        hash.set(i*2);
                else if(board[i] == 2)
                        hash.set(i*2+1);
        }
        if(lookupTable.containsKey(hash))
            return lookupTable.get(hash)[0];
        for(int i = board.length - 1; i >= 0; i--){
                if(board[i] == 1){
                        hash.set((board.length - i)*2);
                        hash.set(i*2+1, false);
                }
                else if(board[i] == 2){
                        hash.set((board.length - i)*2+1);
                        hash.set((board.length - i)*2, false);
                }
                else{ 
                        hash.set((board.length - i)*2, false);
                        hash.set((board.length - i)*2+1, false);
                }
        }
        if(lookupTable.containsKey(hash))
            return lookupTable.get(hash)[0];
        for(int i = 0; i < WIDTH; i++){
            for(int j = 0; j < HEIGHT;j++){
                if(board[j*HEIGHT+i] == 1)
                        hash.set((i*HEIGHT+j)*2);
                else if(board[i] == 2)
                        hash.set((i*HEIGHT+j)*2+1);
                else{    
                        hash.set((board.length - i*HEIGHT+j)*2,false);
                        hash.set((board.length - i*HEIGHT+j)*2+1,false);
                }
            }
        }
        if(lookupTable.containsKey(hash))
            return lookupTable.get(hash)[0];
        for(int i = WIDTH-1; i >= 0; i--){
            for(int j = HEIGHT - 1; j >= 0;j--){
                if(board[j*HEIGHT+i] == 1){
                        hash.set((board.length - i*HEIGHT+j)*2);
                        hash.set((board.length - i*HEIGHT+j)*2+1,false);
                }
                else if(board[i] == 2){
                        hash.set((board.length - i*HEIGHT+j)*2+1);
                        hash.set((board.length - i*HEIGHT+j)*2,false);
                }
                else{   
                        hash.set((board.length - i*HEIGHT+j)*2,false);
                        hash.set((board.length - i*HEIGHT+j)*2+1,false);
                }
            }
        }
        if(lookupTable.containsKey(hash))
            return lookupTable.get(hash)[0];
        return MINUSINFINITE;
    }
	
}
