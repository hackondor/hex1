package ia.game.hex.algorithms;


import java.awt.Color;
import java.util.ArrayList;




public class Arbiter{
	
	private int turn = 0;		
	private ArrayList<Player> players;
	private Board board;
	private Color[] color;

	
	public Arbiter(Board board){
		this.board = board;
		players = new ArrayList<Player>();
		color = Costant.PLAYER_COLOR;
	}
	
	/**
	 * 
	 * @return true se ci sono almeno due giocatori in gioco
	 */
	public boolean isStarted(){
	    if(players.size()>=2)
	    	return true;
	    else
	    	return false;
	}
	
	
	public void nextStep() {
		System.out.println(players.size()); //test
		if(players.size()!=0){
			turn++;
			turn = turn%2;
			System.out.println("turn: "+turn); //test
			if(players.get(turn).isIA())
				players.get(turn).getAlgorithm().action();
		}
	}

	public Color getCurrentPlayerColor() {
		return players.get(turn).getPlayerColor();
	}

	public int getCurrentPlayer() {
		return turn;
	}
	
	public boolean isTurnOfHumanPlayer(){
		return isStarted() && !players.get(turn).isIA();
	}
	
	/**
	 * Aggiunge un giocatore umano
	 */
	public void addPlayer(){
		Player p = new Player(color[players.size()]);
		players.add(p);
	}
	/**
	 * Aggiunge un giocatore artificiale
	 */
	public void addPlayer(AlgorithmsDefinition a){
		a.setPlayer(players.size());
		Player p = new Player(a,color[players.size()]);
		players.add(p);
	}
	
	public boolean isPlacementLegal(int i,int j){
		return board.isBusy(i, j);
	}
	
	public boolean isStealLegal(){
		int k = 0;
		for(int i = 0;i<board.GetRowsNumber();i++)
			for(int j = 0;j<board.GetColumnsNumber();j++)
			   if(board.isBusy(i,j))
				   k++;
		if(k == 1)
			return true;
		else
			return false;
	}
	
	/**
	 * Informazioni del giocatore di HEX
	 * @author Nich
	 *
	 */
	private class Player{
		private AlgorithmsDefinition algorithm;
		private Color player_color;
		private boolean IA;
		
		
		public Player(Color c){
			player_color = c;
			IA = false;
			algorithm = null;
		}
		
		public Player(AlgorithmsDefinition a,Color c){
			player_color = c;
			algorithm = a;
			IA = true;
		}
		
		public AlgorithmsDefinition getAlgorithm() {
			return algorithm;
		}
		
		public void setAlgorithm(AlgorithmsDefinition algorithm) {
			IA = true;
			this.algorithm = algorithm;
		}
		
		public Color getPlayerColor() {
			return player_color;
		}
		
		public void setPlayerColor(Color player_color) {
			this.player_color = player_color;
		}
		
		public boolean isIA(){
			return IA;
		}
	}
	

}
