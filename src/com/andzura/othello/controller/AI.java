package com.andzura.othello.controller;

import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.Deque;
import java.util.HashMap;

import com.andzura.othello.model.Board;

import me.grea.antoine.utils.Dice;

public class AI {
	private  static final int MINUSINFINITE = Integer.MIN_VALUE;
	private  static final int PLUSINFINITE = Integer.MAX_VALUE;
    private  static final int WIDTH = 8;
    private  static final int  HEIGHT = 8;
    private  static final int branchingFactor = 7;
    private  static final byte[] weightedBoard = {	 4, -3,  2,  2,  2,  2, -3,  4,
													-3, -4, -1, -1, -1, -1, -4, -3,
													 2, -1,  1,  0,  0,  1, -1,  2,
													 2, -1,  0,  1,  1,  0, -1,  2,
													 2, -1,  0,  1,  1,  0, -1,  2,
													 2, -1,  1,  0,  0,  1, -1,  2,
													-3, -4, -1, -1, -1, -1, -4, -3,
													 4, -3,  2,  2,  2,  2, -3,  4};
    private  int turn = 0;
	private  HashMap<BitSet, Integer> lookupTable = new HashMap<>();
	private  HashMap<BitSet, Integer> transpositionTable = new HashMap<>();
	private int nonLeaf;
	private int node;
	private long MAXTIME;
	private float coefCorner;
	private float coefMobility;
	private float coefCoin;
	private float coefCSquare;
	
	
	public AI(){
		this(1000, 7,1, 80,32);
	}
	
	public AI(long maxtime){
		this(maxtime, 7,1, 80,32);
	}
	
	public AI(long maxtime, float coefMobility, float coefCoin, float coefCorner, float coefCSquare) {
		this.MAXTIME = maxtime;
		this.coefMobility = coefMobility;
		this.coefCoin = coefCoin;
		this.coefCorner = coefCorner;
		this.coefCSquare = coefCSquare;
	}
	
	public  int play(Board board, int player, int turn){
		this.turn = turn; 
		long start = System.currentTimeMillis();
		Deque<Byte> playable = playableSquare(board, player);
		int[][] scores = new int[playable.size()][2];
		int best;
		//compute score of every possible move.
		int alpha = MINUSINFINITE;
		int beta = PLUSINFINITE;
		int size = playable.size();
		if(size > 1){
			for(int i = 0; i < size; i++){
				scores[i][0] = playable.pop();
			}
			int depth = 0;	
			while(System.currentTimeMillis() - start < MAXTIME){
					nonLeaf = 0;
					node = 0;
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
				if(nonLeaf == 0)
					break;
				if(branchingFactor * time > (MAXTIME - (System.currentTimeMillis() - start)))
					break;
				depth++;
				transpositionTable.clear();
				}
			
			System.out.println("final DEPTH : " + depth + "node " +node+" nonLeaf "+ nonLeaf);
			lookupTable.clear();		
			playable = null;
			//find the best move
			best = 0;
			int bestScore = MINUSINFINITE;
			for(int i = 0; i < scores.length; i++){
				if(scores[i][1] >= bestScore){
					if(scores[i][1] > bestScore || Dice.roll(6)%2 == 0){
						bestScore = scores[i][1];
						best = scores[i][0];
					}
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
		this.turn++;
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
			node++;
			if(!board.checkEndGame()){
				nonLeaf++;
			}else{
			}
			score = evaluate(board, player);
			this.addTestedBoardToTranspositionTable(board.getBoard(), score);
			this.turn--;
			return score;
			
		}
		if(transpositionTable.containsKey(hash)){
			
			node++;
			return transpositionTable.get(hash);
		}
		Deque<Byte> playable = playableSquare(board, player);
		if(playable.isEmpty()){
			return -alphaBeta(depth - 1,board, -beta, -alpha, player%2+1);
		}
		Board testBoard;
		int size = playable.size();
		int move;
		int[][] order = new int[size][2];
		for(int i = 0; i < size; i++){
			move = playable.pop();
			testBoard = new Board(board.getBoard(),HEIGHT, WIDTH);
			for(int j = 0; j< WIDTH*HEIGHT; j++){
				if(testBoard.getSquareContent(j) == 1){
					hash.set(j*2);
					hash.clear(j*2+1);
				}
				else if(testBoard.getSquareContent(j) == 2){
					hash.set(j*2+1);
					hash.clear(j*2);
				}
				else{
					hash.clear(j*2);
					hash.clear(j*2+1);
				}
				
			}
			if(lookupTable.containsKey(hash)){
				score = lookupTable.get(hash);
			}else{
				score = 0;
			}
			for(int j = 0; j < order.length; j++){
				if(order[j][0] == 0){
					order[j][0] = move;
					order[j][1] = score;
					break;
				}
				if(score > order[j][1]){
					order[j][0] += move;
					move = order[j][0] - move;
					order[j][0] -= move;
					order[j][1] += score;
					score = order[j][1] - score;
					order[j][1] -= score;
				}
				
			}
		}
		hash = null;
		testBoard = null;
		for(int i = 0; i < order.length; i++){
			playable.addLast((byte)order[i][0]);
		}
		if(order.length != playable.size()){
			System.out.println("initial size " + size);
			System.out.print("[");
			for(int i = 0; i < order.length; i++){
				System.out.print(order[i][0] + ", ");
			}
			System.out.println("] != " + playable.toString());
		}
		order = null;
		int best = MINUSINFINITE;
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
			this.addTestedBoardToLookupTable(testBoard.getBoard(), score); 
			testBoard = null;
			if(score > best){
				best = score;
				if(best > alpha){
					alpha = best;
						if(alpha > beta){
							this.turn--;
							this.addTestedBoardToTranspositionTable(board.getBoard(), alpha); 
							return alpha;
						}
				}
			}
		}
		this.turn--;
		this.addTestedBoardToTranspositionTable(board.getBoard(), best);
		return best;
	}

	
	
	private  int evaluate(Board oBoard, int player){
		//mobilityScore represent which player has more move available
		byte[] board = oBoard.getBoard();
		int movePlayer = playableSquare(oBoard, player).size();
		int moveOpponent = playableSquare(oBoard, player%2+1).size();
		float mobilityScore = 0;
		//coinScore represents which player has more coin on the board.
		//it's actually what determine victory in othello, but it's not that important  
		//when you're looking for a strategy.
		float coinScorePlayer = 0;
		float coinScoreOpp = 0;
		float coinScore = 0;
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
		for(int i = 0; i < board.length; i++){
			if(board[i] == player){
					coinScorePlayer += weightedBoard[i];
					if(i == 0 || i == WIDTH-1
							|| i == (HEIGHT-1*WIDTH) || i == HEIGHT*WIDTH-1)
						cornerScorePlayer++;
			}else if(board[i] != 0){
					coinScoreOpp += weightedBoard[i];
					if(i == 0 || i == WIDTH-1
							|| i == (HEIGHT-1*WIDTH) || i == HEIGHT*WIDTH-1)
						cornerScoreOpp++;
			}
		}
		coinScore = 100*((coinScorePlayer-coinScoreOpp)/(coinScorePlayer+coinScoreOpp));
		if(turn < 50){
			if(movePlayer != 0 || moveOpponent != 0 ){
				if(movePlayer > moveOpponent)
					mobilityScore = 100*(movePlayer)/(movePlayer + moveOpponent);
				else if(movePlayer < moveOpponent)
					mobilityScore = -100*(moveOpponent)/(movePlayer + moveOpponent);
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
			cSquareScore = -6.25f*(cSquareScorePlayer-cSquareScoreOpp);
			cornerScore = 25f*(cornerScorePlayer-cornerScoreOpp);
		}
		//score is the final score, it's computed with all the other score, every one of those being weighted
		//relatively to its actual importance in the strategy.
		int score =(int) (coefMobility*mobilityScore+coefCoin*coinScore+coefCorner*cornerScore+coefCSquare*cSquareScore);
		return score;
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
        void addTestedBoardToLookupTable(byte[] board, int score)
        add the score of a board in the lookupTable, also add the rotation & symmetry of this board.
        EDIT: taking into account the symmetry and rotation of the board was actually highly impacting performance,
        so I don't add them anymore.             
        
        */
    private  void addTestedBoardToLookupTable(byte[] board, int score){
        BitSet hash = new BitSet(board.length * 2);
        for(int i = 0; i<board.length; i++){
                if(board[i] == 1){
                        hash.set(i*2);
                }
                else if(board[i] == 2){
                        hash.set(i*2+1);
                }
        }
        lookupTable.put(hash, score);
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
    private void addTestedBoardToTranspositionTable(byte[] board, int score){
        BitSet hash = new BitSet(board.length * 2);
        for(int i = 0; i<board.length; i++){
                if(board[i] == 1){
                        hash.set(i*2);
                }
                else if(board[i] == 2){
                        hash.set(i*2+1);
                }
        }
        transpositionTable.put(hash, score);
    }
    
    
	
}
