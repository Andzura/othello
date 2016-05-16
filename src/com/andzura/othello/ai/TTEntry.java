package com.andzura.othello.ai;

public class TTEntry {
	
	public static final byte EXACT = 0;
	public static final byte UPPER = 1;
	public static final byte LOWER = 2;
	public byte flag;
	public int value;
	public byte depth;
	public byte bestMove;
	public byte player;
	
	public TTEntry(byte flag, int value,byte bestMove, byte depth, byte player){
		this.flag = flag;
		this.value = value;
		this.depth = depth;
		this.player = player;
		this.bestMove = bestMove;
		
	}
}
