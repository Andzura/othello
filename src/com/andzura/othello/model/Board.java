

package com.andzura.othello.model;

public class Board {
	//WHITE  = 1 BLACK = 2
	private int[] board;
	private int height, width;
	
	public Board(int height, int width){
		this.height = height;
		this.width = width;
		board = new int[height * width];
		
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
	
	public void setSquareContent(int x, int y, int content){
		if(x >= 0 && x < width){
			if(y >= 0 && y < height)
				board[y*width+x] = content;
		}
	}
	public void setSquareContent(int i, int content){
		board[i] = content;
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
						if(curX > 0 && curX < width
								&& curY > 0 && curY < height){
							if(this.getSquareContent(curX, curY) != 0 && this.getSquareContent(curX, curY) != color){
								do{
									curX += i;
									curY += j;
								}while(curX > 0 && curX < width
										&& curY > 0 && curY < height
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
						if(curX > 0 && curX < width
								&& curY > 0 && curY < height){
							if(this.getSquareContent(curX, curY) != 0 && this.getSquareContent(curX, curY) != color){
								do{
									curX += i;
									curY += j;
								}while(curX > 0 && curX < width
										&& curY > 0 && curY < height
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
}
