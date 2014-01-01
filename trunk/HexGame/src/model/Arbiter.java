package model;

import java.awt.Color;
import java.util.Observable;


///devo implementare io observer perchè ho bisogno di un mecccanismo di notifica singlecast
public class Arbiter extends Observable {
	
	private int turn = 0;		//
	private int numbers_of_player;

	
	public Arbiter(){}
	
	
	public void nextStep() {
		// TODO Auto-generated method stub
		//turn++;
		//turn%numbers_of_player		
	}

	public Color getCurrentPlayerColor() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getCurrentPlayer() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	
	

}
