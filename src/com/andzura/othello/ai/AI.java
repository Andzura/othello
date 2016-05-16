package com.andzura.othello.ai;

import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.andzura.othello.model.Board;



import com.andzura.othello.utils.Logger;


public class AI {
	private  static final int MINUSINFINITE = -Integer.MAX_VALUE;
	private  static final int PLUSINFINITE = Integer.MAX_VALUE;
    private  static final int WIDTH = 8;
    private  static final int  HEIGHT = 8;
    private  static final float timeDepthFactor = 3f;
    private  static final byte[] weightedBoard = {	 4, -3,  2,  2,  2,  2, -3,  4,
													-3, -4, -1, -1, -1, -1, -4, -3,
													 2, -1,  1,  0,  0,  1, -1,  2,
													 2, -1,  0,  1,  1,  0, -1,  2,
													 2, -1,  0,  1,  1,  0, -1,  2,
													 2, -1,  1,  0,  0,  1, -1,  2,
													-3, -4, -1, -1, -1, -1, -4, -3,
													 4, -3,  2,  2,  2,  2, -3,  4};
	private  HashMap<BitSet, TTEntry> transpositionTable = new HashMap<>();
	private boolean leafOnly;
	private long MAXTIME;
	private float coefCorner;
	private float coefMobility;
	private float coefCoin;
	private float coefCSquare;
	private float coefWeighted;
	private int node;
	
	
	public AI(){
		this(1000, 79,1, 800,382,5);
	}
	
	public AI(long maxtime){
		this(maxtime, 100,1, 800,300,5);
	}
	
	public AI(long maxtime, float coefMobility, float coefCoin, float coefCorner, float coefCSquare,float coefWeighted) {
		this.MAXTIME = maxtime;
		this.coefMobility = coefMobility;
		this.coefCoin = coefCoin;
		this.coefCorner = coefCorner;
		this.coefCSquare = coefCSquare;
		this.coefWeighted = coefWeighted;
	}
	
	public  int play(Board board, int player, int turn){
		long start = System.currentTimeMillis();
		Deque<Byte> playable = playableSquare(board, player);
		int[][] scores = new int[playable.size()][2];
		int best;
		//compute score of every possible move.
		int size = playable.size();
		if(size > 1){
			for(int i = 0; i < size; i++){
				scores[i][0] = playable.pop();
			}
			int depth = 0;	
			node = 0;
			while(System.currentTimeMillis() - start < MAXTIME){
					int alpha = MINUSINFINITE;
					int beta = PLUSINFINITE;
					leafOnly = true;
					long startDepth = System.currentTimeMillis();
					for(int i = 0; i < scores.length; i++){
						Board testBoard = new Board(board.getBoard(), HEIGHT, WIDTH);
						testBoard.play(scores[i][0]%WIDTH, scores[i][0]/WIDTH, player);
						scores[i][1] = -alphaBeta(depth, testBoard,-beta, -alpha, player%2+1);
						testBoard = null;
						if(scores[i][1] > alpha){
							alpha = scores[i][1];
						}
					}
					long time = System.currentTimeMillis() - startDepth;
					for(int j = 0; j <scores.length; j++){
						for(int k = 0; k < scores.length; k++){
							if(scores[k][1] < scores[j][1]){
								scores[k][1] += scores[j][1];
								scores[j][1] = (byte)(scores[k][1] - scores[j][1]);
								scores[k][1] -= scores[j][1];
								scores[k][0] += scores[j][0];
								scores[j][0] = (byte)(scores[k][0] - scores[j][0]);
								scores[k][0] -= scores[j][0];
							}
						}	
					}
				if(leafOnly)
					break;
				if(timeDepthFactor * time > (MAXTIME - (System.currentTimeMillis() - start)))
					break;
				depth++;
				}
			transpositionTable.clear();
			Logger.println("final DEPTH : "+ depth + " done in " + (System.currentTimeMillis() - start) + "ms", Logger.DEBUG);
			Logger.println("transposition Table used " + node + " times ", Logger.DEBUG);
			playable = null;
			//find the best move
			best = 0;
			int bestScore = MINUSINFINITE;
			for(int i = 0; i < scores.length; i++){
				if(scores[i][1] > bestScore){
						bestScore = scores[i][1];
						best = scores[i][0];
				}
			}
		}else{
			if(!playable.isEmpty())
				best = playable.pop();
			else
				best = -1;
		}
		return best;
	}
	
	private  int alphaBeta(int depth, Board board, int alpha, int beta, int player) {
		int score;
		BitSet hash = new BitSet(WIDTH*HEIGHT * 2);
		for(int j = 0; j< WIDTH*HEIGHT; j++){
			if(board.getSquareContent(j) == 1){
				hash.set(j*2);
			}
			else if(board.getSquareContent(j) == 2){
				hash.set(j*2+1);
			}
		}
		if(depth == 0 || board.checkEndGame()){
			if(!board.checkEndGame())
				leafOnly = false;
			score = evaluate(board, player);
			return score;
			
		}
		int bestMove = -1;
		if(transpositionTable.containsKey(hash)){
			TTEntry e = transpositionTable.get(hash);
			bestMove = e.bestMove;
			if(e.depth >= depth){
				node++;
				if(e.flag == TTEntry.EXACT){
			         return (e.player == player ? 1 : -1) * e.value;
				}
			    else if(e.flag == TTEntry.LOWER)
			         alpha = max(alpha, (e.player == player ? 1 : -1) * e.value);
			    else if(e.flag == TTEntry.UPPER)
			         beta = min(beta, (e.player == player ? 1 : -1) * e.value);
			    if(alpha >= beta){
			        return (e.player == player ? 1 : -1) * e.value;
			    }
			}
		}
		Deque<Byte> playable = playableSquare(board, player);
		if(playable.isEmpty()){
			return -alphaBeta(depth - 1,board, -beta, -alpha, player%2+1);
		}
		Board testBoard;
		int size = playable.size();
		int move;
		if(bestMove != -1 && playable.remove((byte)bestMove)){
			playable.addFirst((byte)bestMove);
		}
		
		int best = MINUSINFINITE;
		int initialAlpha = alpha;
		bestMove = -1;
		size = playable.size();
		for(int i = 0; i < size; i++){
			move = playable.pop();
			testBoard = new Board(board.getBoard(), HEIGHT, WIDTH);
			testBoard.play(move%WIDTH, move/WIDTH, player);
			if(i == 0){
				score = -alphaBeta(depth - 1,testBoard, -beta, -alpha, player%2+1);
			}else{
				score = -alphaBeta(depth - 1,testBoard, -alpha-1, -alpha, player%2+1);
				if(score > alpha && score<beta)
					score = -alphaBeta(depth - 1,testBoard, -beta, -score, player%2+1);
			}
			testBoard = null;
			if(score > best){
				best = score;
				bestMove = move;
				if(best > alpha){
					alpha = best;
						if(alpha > beta){
							break;
						}
				}
			}
		}
		if(best < initialAlpha){
			this.addTTEntry(board.getBoard(), best,bestMove, depth, player, TTEntry.UPPER);
		}else if(best > beta){
			this.addTTEntry(board.getBoard(), best,bestMove, depth, player, TTEntry.LOWER);
		}else{
			this.addTTEntry(board.getBoard(), best,bestMove, depth, player, TTEntry.EXACT);
		}
		return best;
	}

	
	
	private  int evaluate(Board oBoard, int player){
		//mobilityScore represent which player has more move available
		byte[] board = oBoard.getBoard();
		float movePlayer = playableSquare(oBoard, player).size();
		float moveOpponent = playableSquare(oBoard, player%2+1).size();
		float mobilityScore = 0;
		//coinScore represents which player has more coin on the board.
		//it's actually what determine victory in othello, but it's not that important  
		//when you're looking for a strategy.
		float coinScorePlayer = 0;
		float coinScoreOpp = 0;
		float coinScore = 0;
		//coinScoreWeighted is the same as CoinScore, but every position on the board is weighted
		//by the position on the board.
		float coinScoreWeightedPlayer = 0;
		float coinScoreWeightedOpp = 0;
		float coinScoreWeighted = 0;		
		//cornerScore represents which player has corner, corner being a really important square to have
		//because of it's stability (it can be taken) and because he permits you to take a lot of coin
		float cornerScore = 0;
		float cornerScorePlayer = 0;
		float cornerScoreOpp = 0;
		//cSquareScore represents which player has the square around a corner, when the corner is not taken
		//having those square is a bad thing as it gives away the corner.
		float cSquareScore = 0;
		float cSquareScorePlayer = 0;
		float cSquareScoreOpp = 0;
		//cSquareScore represents which player has the square around a corner, when the corner is not taken
		//having those square is a bad thing as it gives away the corner.
		float frontierScore = 0;
		float frontierScorePlayer = 0;
		float frontierScoreOpp = 0;
		for(int i = 0; i < board.length; i++){
			for(int j= -1; j < 1 ; j++){
				for(int k = -1; k < 1; k++){
					
				}
			}
			if(board[i] == player){
					coinScorePlayer++;
					coinScoreWeightedPlayer += weightedBoard[i];
					if(i == 0 || i == WIDTH-1
							|| i == (HEIGHT-1*WIDTH) || i == HEIGHT*WIDTH-1)
						cornerScorePlayer++;
			}else if(board[i] != 0){
					coinScoreOpp++;
					coinScoreWeightedOpp += weightedBoard[i];
					if(i == 0 || i == WIDTH-1
							|| i == (HEIGHT-1*WIDTH) || i == HEIGHT*WIDTH-1)
						cornerScoreOpp++;
			}
		}
		if(coinScorePlayer > coinScoreOpp)
			coinScore = 100*((coinScorePlayer)/(coinScorePlayer+coinScoreOpp));
		else
			coinScore = -100*((coinScoreOpp)/(coinScorePlayer+coinScoreOpp));
		if(countEmptySquare(board) > 12){
			if(coinScoreWeightedPlayer != 0 || coinScoreWeightedOpp != 0)
				coinScoreWeighted = 100*(coinScoreWeightedPlayer - coinScoreWeightedOpp)/(coinScoreWeightedPlayer + coinScoreWeightedOpp);
			if(movePlayer != 0 || moveOpponent != 0){
					mobilityScore = -100*((movePlayer - moveOpponent)/(movePlayer+moveOpponent));
			}
			if(board[0] == 0){
				if(board[1] == player){
					cSquareScorePlayer++;
				}else if(board[1] != 0){
					cSquareScoreOpp++;
				}
				if(board[WIDTH] == player){
					cSquareScorePlayer++;
				}else if(board[WIDTH] != 0){
					cSquareScoreOpp++;
				}
				if(board[WIDTH+1] == player){
					cSquareScorePlayer++;
				}else if(board[WIDTH+1] != 0){
					cSquareScoreOpp++;
				}
			}
			if(board[WIDTH-1] == 0){
				if(board[WIDTH-2] == player){
					cSquareScorePlayer++;
				}else if(board[WIDTH-2] != 0){
					cSquareScoreOpp++;
				}
				if(board[WIDTH*2-1] == player){
					cSquareScorePlayer++;
				}else if(board[WIDTH*2-1] != 0){
					cSquareScoreOpp++;
				}
				if(board[WIDTH*2-2] == player){
					cSquareScorePlayer++;
				}else if(board[WIDTH*2-2] != 0){
					cSquareScoreOpp++;
				}
			}		
			if(board[WIDTH*(HEIGHT-1)] == 0){
				if(board[WIDTH*(HEIGHT-1)+1] == player){
					cSquareScorePlayer++;
				}else if(board[WIDTH*(HEIGHT-1)+1] != 0){
					cSquareScoreOpp++;
				}
				if(board[WIDTH*(HEIGHT-2)] == player){
					cSquareScorePlayer++;
				}else if(board[WIDTH*(HEIGHT-2)] != 0){
					cSquareScoreOpp++;
				}
				if(board[WIDTH*(HEIGHT-2)+1] == player){
					cSquareScorePlayer++;
				}else if(board[WIDTH*(HEIGHT-2)+1] != 0){
					cSquareScoreOpp++;
				}
			}
			if(board[WIDTH*HEIGHT - 1] == 0){
				if(board[WIDTH*HEIGHT - 2] == player){
					cSquareScorePlayer++;
				}else if(board[WIDTH*HEIGHT - 2] != 0){
					cSquareScoreOpp++;
				}
				if(board[WIDTH*(HEIGHT-1) - 1] == player){
					cSquareScorePlayer++;
				}else if(board[WIDTH*(HEIGHT-1) - 1] != 0){
					cSquareScoreOpp++;
				}
				if(board[WIDTH*(HEIGHT-1) - 2] == player){
					cSquareScorePlayer++;
				}else if(board[WIDTH*(HEIGHT-1) - 2] != 0){
					cSquareScoreOpp++;
				}
			}
			cSquareScore = -12.5f*(cSquareScorePlayer-cSquareScoreOpp);
			cornerScore = 25f*(cornerScorePlayer-cornerScoreOpp);
		}
		//score is the final score, it's computed with all the other score, every one of those being weighted
		//relatively to its actual importance in the strategy.
		int score =(int) (coefMobility*mobilityScore
							+coefCoin*coinScore
							+coefCorner*cornerScore
							+coefCSquare*cSquareScore
							+coefWeighted*coinScoreWeighted);
		
		return score;
	}
	
	private int countEmptySquare(byte[] board) {
		int count = 0;
		for(int i = 0; i < board.length; i++)
			count += (board[i] == 0 ? 1 : 0);
		return count;
	}

	private  Deque<Byte> playableSquare(Board board, int player){
		Deque<Byte> playable = new ArrayDeque<>();
		for(int i = 0; i < WIDTH*HEIGHT; i++){
			if(board.isPlayable(i%WIDTH,i/WIDTH,player))
				playable.add((byte)i);
		}
		return playable;
	}
	

    /*
    void addTestedBoardToTranspositionTable(byte[] board, int score)
    	add a board we already computed the score to the Transposition Table
    	the main difference between transpositionTable and lookupTable, is that transpositionTable is cleared everytime we compute a new depth
    	but lookupTable is only cleared everytime the play function is used.
    	that way, lookupTable is used to actually store the score and order the move for each new depth
    	and transpositionTable is used to get the score of a board we already computed at a given depth 
    	( which gives boost in performance as any node might be the child of multiples others nodes).            
    
    */
    private void addTTEntry(byte[] board, int score, int bestMove, int turn, int player, byte flag){
        BitSet hash = new BitSet(board.length * 2);
        for(int i = 0; i<board.length; i++){
                if(board[i] == 1){
                        hash.set(i*2);
                }
                else if(board[i] == 2){
                        hash.set(i*2+1);
                }
        }
        transpositionTable.put(hash, new TTEntry(flag ,score, (byte)bestMove, (byte)turn, (byte)player));
    }
    
    private static int min(int a, int b){
    	return (a < b ? a : b);
    }
    
    private static int max(int a , int b){
    	return (a > b ? a : b);
    }
	
}
