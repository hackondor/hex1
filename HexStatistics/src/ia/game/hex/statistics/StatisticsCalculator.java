package ia.game.hex.statistics;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import ia.game.hex.algorithms.Player;


public class StatisticsCalculator {
	
	private ArrayList<Player> players;
	private int numberOfGame;						//numero di partite totali della sessione di test
	private int game = 0;							//numero di partite eseguite nella sessione di test
	private PrintWriter fw;
	private String p1;
	private String p2;
	private int winP1 = 0;
	private int winP2 = 0;
	
	public StatisticsCalculator(int numberOfGame,String fileName){
		this.numberOfGame = numberOfGame;
		try {
			fw = new PrintWriter(fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * when a new game is created
	 * @param a
	 */
	public void init(ArrayList<Player> a ){
		players = a;
		p1 = players.get(0).getName();
		p2 = players.get(1).getName();
		fw.println("Primo giocatore: "+p1);
		fw.println("Secondo giocatore: "+p2);
	}
	
	/**
	 * when a game end
	 * @param p
	 */
	public void calculate(Player p){
		if(players!=null){
			game++;
			if(p.getName().compareTo(p1)==0 )
				winP1++;
			else
				winP2++;
			fw.println("---------------------------");
			fw.println("Game: "+game);
			fw.println("Win: "+p.getName());
			fw.println(players.get(0).getName()+" avg move: "+players.get(0).getAlgorithm().getNumberOfMove());
			fw.println(players.get(0).getName()+" avg time: "+players.get(1).getAlgorithm().getAvgTimePlacement());
			fw.println(players.get(1).getName()+" avg move: "+players.get(0).getAlgorithm().getNumberOfMove());
			fw.println(players.get(1).getName()+" avg time: "+players.get(1).getAlgorithm().getAvgTimePlacement());
			fw.println("---------------------------");
		}
		
		if(game == numberOfGame){
			fw.println(p1+" "+winP1);
			fw.println(p2+" "+winP2);
			fw.close();
		}
			
	}

}
