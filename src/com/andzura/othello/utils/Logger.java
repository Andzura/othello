package com.andzura.othello.utils;

import java.io.PrintStream;

public class Logger {
	public static final int  DEBUG = 0;
	public static final int WARNING = 1;
	public static final int ERROR = 2;
	public static final int INFO = 3;
	private static int LEVEL = WARNING;
	private static PrintStream out = System.out;
	
	public static void println(String s, int level){
		if(level >= Logger.LEVEL){
			Logger.out.println(s);
		}
	}
	
	public static void print(String s, int level){
		if(level >= Logger.LEVEL){
			Logger.out.print(s);
		}
	}
	
	public static void setLevel(int level){
		Logger.LEVEL = level;
	}
	
	public static void setOut(PrintStream out){
		Logger.out = out;
	}

}
