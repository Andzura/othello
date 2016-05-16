package com.andzura.othello.statemanager;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;


/* The statemanager concept was explained to me by Tomyi,
 * So I want to thanks him to explained me how it works and
 * how to implements it, and also for all others tiny things
 * he could have help me with in this project, or for other ones. 
*/
public class StateManager {

	    private Deque<State> stack = new ArrayDeque<State>();
	    private JFrame screen = new JFrame();

	    //create an empty StateManager
	    //adding State and pushing one on Top of the stack 
	    // is needed before any other action on this StateManager.
	    public StateManager(){	   
	    }
	    //create a StateManager with only the State state
	    //push this State on top of the stack.
	    public StateManager(State state){
	    	state.init();
	    	this.stack.push(state);
	    	screen.add(stack.peek().getScreen());
	    }
	    
	    public void update(long elapsedTime)
	    {
	        State top = stack.peek();
	        if(top != null)
	        	top.update(elapsedTime);
	    }
	 
	    public void render()
	    {
	        State top = stack.peek();
	        if(top != null)
	        	top.render();
	    }
	 
	    public void push(State state)
	    {
	        state.init();
	        if(stack.peek() != null)
	        	screen.remove(stack.peek().getScreen());
	        stack.push(state);
	        screen.add(stack.peek().getScreen());
	        
	    }
	 
	    public State pop()
	    {
	        if(stack.peek() != null){
	        	screen.remove(stack.peek().getScreen());
	        	stack.peek().exit();
	        	State s = stack.pop();
	        	if(stack.peek() != null)
	        		screen.add(stack.peek().getScreen());
	    		return s;
	        }
	        return null;
	    }
	    

		public State getCurrent() {
			return stack.peek();
		}
		
		public JFrame getScreen(){
			return screen;
		}
}