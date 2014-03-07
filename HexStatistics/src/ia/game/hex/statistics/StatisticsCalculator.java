package ia.game.hex.statistics;

import java.util.ArrayList;
import ia.game.hex.algorithms.Player;


public class StatisticsCalculator {
	
	private ArrayList<Player> players;
	private int numberOfGame;						//numero di partite della sessione di test
	public StatisticsCalculator(int numberOfGame){
		this.numberOfGame = numberOfGame;
	}
	
	/**
	 * when a new game is created
	 * @param a
	 */
	public void init(ArrayList<Player> a ){
		players = a;
	}
	
	/**
	 * when a game end
	 * @param p
	 */
	public void calculate(Player p){
		if(players!=null){
			System.out.println("Statistics: Win "+p.getName());
			System.out.println(players.get(0).getAlgorithm().getNumberOfMove());
			System.out.println(players.get(0).getAlgorithm().getAvgTimePlacement());
		
		}
	}

}
