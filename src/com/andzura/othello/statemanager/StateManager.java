package com.andzura.othello.statemanager;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;


/* The statemanager concept was explained to me by Tomyi,
 * So I want to thanks him to explained me how it works and
 * how to implements it, and also for all others tiny things
 * he could have help me with in this project, or for other ones. 
*/
public class StateManager {

		private Map<String, State> states = new HashMap<String, State>();
	    private Deque<State> stack = new ArrayDeque<State>();
	    private JPanel screen;

	    //create an empty StateManager
	    //adding State and pushing one on Top of the stack 
	    // is needed before any other action on this StateManager.
	    public StateManager(){	   
	    }
	    //create a StateManager with only the State state
	    //push this State on top of the stack.
	    public StateManager(State state, String nameState){
	    	this.states.put(nameState, state);
	    	state.init();
	    	this.stack.push(state);
	    	screen = stack.peek().getScreen();
	    }
	    
	    //create a StateManager with Map of States
	    //Doesn't push any state on top of the stack
	    public StateManager(Map<String,State> states){
	    	this.states.putAll(states);
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
	 
	    public void push(String name)
	    {
	        State state = states.get(name);
	        state.init();
	        stack.push(state);
	        screen = stack.peek().getScreen();
	        
	    }
	 
	    public State pop()
	    {
	        if(stack.peek() != null){
	        	stack.peek().exit();
	    		return stack.pop();
	        }
	        return null;
	    }
	    
	    public void addState(State state, String nameState){
	    	states.put(nameState, state);
	    }

		public State getCurrent() {
			return stack.peek();
		}
		
		public State getState(String nameState) {
			return states.get(nameState);
		}
		
		public JPanel getScreen(){
			return screen;
		}
}