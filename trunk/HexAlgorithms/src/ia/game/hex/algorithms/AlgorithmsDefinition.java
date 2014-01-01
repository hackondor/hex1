package ia.game.hex.algorithms;

import java.util.Observable;
import java.util.Observer;

public abstract class AlgorithmsDefinition implements Observer {
	
	private Board board;
	
	public AlgorithmsDefinition(Board b){
		board = b;
	}
	
	public void update(Observable obs, Object args) {
		action();
	}
	
	public abstract void action();

}
