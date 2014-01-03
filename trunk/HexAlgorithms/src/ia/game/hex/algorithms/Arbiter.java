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


	public void nextStep(){
		if(isWin())
			System.out.println("Player has win!");  //test
		else{
			if(players.size()!=0){
				turn++;
				turn = turn%2;
				if(players.get(turn).isIA())
					players.get(turn).getAlgorithm().action();
			}
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

	
	private class Node{
		private int x;
		private int y;
		
		public Node(int x,int y){
			this.x = x;
			this.y = y;
		}
		public int getX() {
			return x;
		}
		public void setX(int x) {
			this.x = x;
		}
		public int getY() {
			return y;
		}
		public void setY(int y) {
			this.y = y;
		}
		
		
		
	}
	private ArrayList<Node> blackNodes;
	
	/**
	 * Detection della vittoria
	 * @return true se il giocatore corrente ha vinto la partita
	 */
	public boolean isWin(){
		blackNodes = new ArrayList<Node>();
		boolean win = false;
		for(int i=0;i<board.GetRowsNumber() && !win;i++)
			win = _winDetect(0,i);
		return win;
	}
	
	private boolean _winDetect(int i,int j){
		
		if(i<0 || j<0 || i >= board.GetRowsNumber() || j>= board.GetColumnsNumber() ) //CASO DEGENERE
			return false;
		blackNodes.add(new Node(i,j));
		
		if(board.belongTo(i, j) != this.getCurrentPlayer()){ //CASO DEGENERE
			return false;
		}
		else if(i==board.GetColumnsNumber() - 1) {//CASO BASE
			return true;
		}
		else{ //CASO INDUTTIVO
			boolean find1 = false;
			boolean find2 = false;
			
			int k =0;
			while(k<blackNodes.size()){
				if(blackNodes.get(k).getX() == i && blackNodes.get(k).getY() == j+1) 
					find1 = true;
				if(blackNodes.get(k).getX() == i && blackNodes.get(k).getY() == j-1)
					find2 = true;
				k++;
			}
			if(!find1 && !find2)
				return _winDetect(i,j-1) || _winDetect(i,j+1) || _winDetect(i+1,j) || 
						_winDetect(i+1,j-1);
			else if(!find1 && find2)
				return _winDetect(i,j+1) || _winDetect(i+1,j) || 
						_winDetect(i+1,j-1);
			else if(find1 && !find2)
				return _winDetect(i,j-1) || _winDetect(i+1,j) || 
						_winDetect(i+1,j-1);
			else
				return  _winDetect(i+1,j) || 
						_winDetect(i+1,j-1);
				
				
		
		}

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
