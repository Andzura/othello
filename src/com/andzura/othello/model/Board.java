

package com.andzura.othello.model;

import com.andzura.othello.utils.Logger;

public class Board {
	//WHITE  = 1 BLACK = 2
	private byte[] board;
	private int height, width;
	
	public Board(){
		this(8,8);
	}
	public Board(byte[] board, int height, int width){
		this.board = board;
		this.height = height;
		this.width = width;
	}
	public Board(int height, int width){
		this.height = height;
		this.width = width;
		board = new byte[height * width];
		
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				if((i+1 == height/2 && j+1 == width/2) || (i+1 == height/2+1 && j+1 == width/2+1)){
					board[i*width+j] = 1;
				}
				else if((i+1 == height/2 && j+1 == width/2+1) || (i+1 == height/2+1 && j+1 == width/2)){
					board[i*width+j] = 2;
				}
				else{
					board[i*width+j] = 0;
				}
			}
		}
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getSquareContent(int x, int y){
		if(x >= width || x < 0)
			return -1;
		if(y >= height || y < 0)
			return -1;

		return board[y*width+x];
	}
	public int getSquareContent(int i){
		if(i >= width * height)
			return -1;
		return board[i];
	}
	
	public byte[] getBoard(){
		return board.clone();
	}
	public void setSquareContent(int x, int y, int content){
		if(x >= 0 && x < width){
			if(y >= 0 && y < height)
				board[y*width+x] = (byte)content;
		}
	}
	public void setSquareContent(int i, int content){
		board[i] = (byte)content;
	}
	
	
	public boolean isPlayable(int x, int y, int color){
		boolean playable = false;
		int curX, curY;
		if(this.getSquareContent(x, y) == 0){
			for(int i = -1 ; i <= 1; i++){
				for(int j = -1; j<=1 ; j++){
					if(i != 0 || j != 0){
						curX = x+i;
						curY = y+j;
						if(curX >= 0 && curX < width
								&& curY >= 0 && curY < height){
							if(this.getSquareContent(curX, curY) != 0 && this.getSquareContent(curX, curY) != color){
								do{
									curX += i;
									curY += j;
								}while(curX >= 0 && curX < width
										&& curY >= 0 && curY < height
										&& this.getSquareContent(curX, curY) != 0 
										&& this.getSquareContent(curX, curY) != color);
								if(this.getSquareContent(curX, curY) == color){
									playable = true;
									break;
								}
							}
						}
					}
				}
			}
		}
		return playable;
	}
	
	public boolean play(int x, int y, int color){
		boolean playable = false;
		boolean reverse;
		int curX, curY;
		if(this.getSquareContent(x, y) == 0){
			for(int i = -1 ; i <= 1; i++){
				for(int j = -1; j<=1 ; j++){
					reverse = false;
					if(i != 0 || j != 0){
						curX = x+i;
						curY = y+j;
						if(curX >= 0 && curX < width
								&& curY >= 0 && curY < height){
							if(this.getSquareContent(curX, curY) != 0 && this.getSquareContent(curX, curY) != color){
								do{
									curX += i;
									curY += j;
								}while(curX >= 0 && curX < width
										&& curY >= 0 && curY < height
										&& this.getSquareContent(curX, curY) != 0 
										&& this.getSquareContent(curX, curY) != color);
								if(this.getSquareContent(curX, curY) == color){
									playable = true;
									reverse = true;
								}
								if(reverse){
									while(curX != x || curY != y){
										this.setSquareContent(curX, curY, color);
										curX -= i;
										curY -= j;	
									}
									this.setSquareContent(curX, curY, color);
								}
							}
						}
					}
				}
			}
		}
		return playable;
	}
	
	public boolean checkEndGame(){
		for(int i = 0; i < this.getHeight() * this.getWidth(); i++){
			if((this.isPlayable(i%8, i/8, 1) || this.isPlayable(i%8, i/8, 2)))
					return false;
		}
		return true;
	}
	
	public int winner() {
		int scoreP1 = 0;
		int scoreP2 = 0;
		for(int i = 0; i < this.getHeight() * this.getWidth(); i++){
			if(this.getSquareContent(i) == 1)
				scoreP1++;
			else if(this.getSquareContent(i) == 2)
				scoreP2++;
		}
		return (scoreP1 > scoreP2 ? 1 : 2);
	}
	public void print() {
		for(int i = 0; i < this.getHeight(); i++){
			for(int j = 0; j < this.getWidth(); j++){
				Logger.print(this.getSquareContent(j, i) + " ", Logger.INFO);
			}
			Logger.println("", Logger.INFO);
		}
	}
	
}
