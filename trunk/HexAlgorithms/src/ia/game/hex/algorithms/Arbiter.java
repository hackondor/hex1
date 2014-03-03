package ia.game.hex.algorithms;


import java.awt.Color;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;



public class Arbiter implements Observer{

	private int turn = -1;		
	private ArrayList<Player> players;
	private Board board;
	private Color[] color;
	private ArrayList<GameListener> finishGameListener;
	private WinDetection[] winDetection ;
	private boolean isStealLegal;

	public Arbiter(Board board){
		this.board = board;
		players = new ArrayList<Player>();
		color = Costant.PLAYER_COLOR;
		finishGameListener = new ArrayList<GameListener>();
		winDetection=new WinDetection[2];
		winDetection[0]=new WinDetection(0, board.GetRowsNumber(),board.GetColumnsNumber());
		winDetection[1]=new WinDetection(1, board.GetRowsNumber(),board.GetColumnsNumber());
		board.addObserver(winDetection[0]);
		board.addObserver(winDetection[1]);
		isStealLegal=true;
	}

	/**
	 * 
	 * @return true se ci sono almeno due giocatori in gioco
	 */
	public boolean isStartable(){
		if(players.size()>=2)
			return true;
		else
			return false;
	}


	public void nextStep(){
		// se è il turno del giocatore orizzontale controllo se la mossa precedente ha portato alla vittoria
		if(winDetection[turn].isWin()){
			notifyListener(players.get(this.getCurrentPlayer()));
			turn = -1;//nex può giocare
		}
		
//		// se è il turno del giocatore verticale controllo se la mossa precedente ha portato alla vittoria
//		else if(turn==PLAYER_VERT && isWin(groupsVert)){
//			notifyListener(players.get(this.getCurrentPlayer()));
//			turn = -1;//nex può giocare
//		}
		else
		// se non c'è stata vittoria
		if(players.size()!=0){
			
			//incremento il turno
			turn++;
			turn = turn%2;
			
			//se il prossimo giocatore è artificiale invoco il metodo action che compie l'azione opportuna
			if(players.get(turn).isIA()){
				new Thread(new Runnable(){
					public void run(){
						players.get(turn).getAlgorithm().action();
					}
				}).start();
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
		return isStartable() && !players.get(turn).isIA();
	}

	/**
	 * Aggiunge un giocatore umano
	 */
	public void addPlayer(String aname){
		Player p = new Player(aname,color[players.size()]);
		players.add(p);
		if(isStartable()) //se il primo giocatore è un IA
			_startGame();
	}
	/**
	 * Aggiunge un giocatore artificiale
	 */
	public void addPlayer(AlgorithmsDefinition a){
		a.setPlayer(players.size());
		a.setBoard(board);
		Player p = new Player(a.getName(),a,color[players.size()]);
		players.add(p);
		a.addObserver(this);	//aggiunta di Arbiter alla lista degli ascoltatori che attendono la l'evento che segnala la fine della mossa dell'algoritmo
		if(isStartable()) //se il primo giocatore è un IA
			_startGame();
	}

	public void removePlayers(){
		players.clear();
	}


	public boolean isPlacementLegal(int i,int j){
		return !board.isBusy(i, j);
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

//	public boolean isWin(Groups groups){
//		return _winDetect( groups);
//	}

	/**
	 * Decreta l'inizio del gioco. Il che significa che se il primo giocatore è 
	 * umano allora aspetterà la sua prima mossa, altrimenti se il giocatore è un IA
	 * gli comunica che deve calcolare una mossa.
	 */
	private void _startGame(){
		if(players.size()!=0){
			turn=0;
			if(players.get(turn).isIA()){
				new Thread(new Runnable(){
					public void run(){
						players.get(turn).getAlgorithm().action();
					}
				}).start();			
			}
		}
	}


	


	public ArrayList<Player> getPlayers(){
		return players;
	}

	public void addFinishGameListener(GameListener l){
		finishGameListener.add(l);
	}

	private void notifyListener(Player args){
		for(GameListener g :finishGameListener)
			g.update(args);
	}

	/**
	 * quando l'algortmo termina di calcolare la mossa
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		nextStep();
		
	}



}
