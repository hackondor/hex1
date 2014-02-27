package ia.game.hex.algorithms;


import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Arbiter{

	private static final int PLAYER_HOR = 0;
	private static final int PLAYER_VERT = 1;
	private int turn = -1;		
	private ArrayList<Player> players;
	private Board board;
	private Color[] color;
	private ArrayList<GameListener> finishGameListener;
	private Groups groupsHor,groupsVert;

	public Arbiter(Board board){
		this.board = board;
		players = new ArrayList<Player>();
		color = Costant.PLAYER_COLOR;
		finishGameListener = new ArrayList<GameListener>();
		groupsHor=new Groups();
		groupsVert=new Groups();
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

		// se è il turno del giocatore orizzontale controllo se la mossa precedente ha portato alla vittoria
		if(turn==PLAYER_HOR && isWin(groupsHor))
			notifyListener(players.get(this.getCurrentPlayer()));
		
		// se è il turno del giocatore verticale controllo se la mossa precedente ha portato alla vittoria
		else if(turn==PLAYER_VERT && isWin(groupsVert))
			notifyListener(players.get(this.getCurrentPlayer()));
		
		// se non c'è stata vittoria
		if(players.size()!=0){
			
			//incremento il turno
			turn++;
			turn = turn%2;
			
			//se il prossimo giocatore è artificiale invoco il metodo action che compie l'azione opportuna
			if(players.get(turn).isIA()){
				players.get(turn).getAlgorithm().action();
				nextStep();
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
	public void addPlayer(String aname){
		Player p = new Player(aname,color[players.size()]);
		players.add(p);
		if(this.isStarted() && players.get(0).isIA()) //se il primo giocatore è un IA
			_startGame();
	}
	/**
	 * Aggiunge un giocatore artificiale
	 */
	public void addPlayer(AlgorithmsDefinition a){
		a.setPlayer(players.size());
		Player p = new Player(a.getName(),a,color[players.size()]);
		players.add(p);
		if(this.isStarted() && players.get(0).isIA()) //se il primo giocatore è un IA
			_startGame();
	}

	public void removePlayers(){
		players.clear();
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

	public boolean isWin(Groups groups){
		return _winDetect( groups);
	}

	/**
	 * Decreta l'inizio del gioco. Il che significa che se il primo giocatore è 
	 * umano allora aspetterà la sua prima mossa, altrimenti se il giocatore è un IA
	 * gli comunica che deve calcolare una mossa.
	 */
	private void _startGame(){
		if(players.size()!=0){
			turn=0;
			if(players.get(turn).isIA()){
				players.get(turn).getAlgorithm().action();
				nextStep();
			}
		}

	}


	private boolean _winDetect(Groups groups){
		
		//Carico nelle variabili i e j le coordinate dell'ultima pedina inserita
		Node last = board.getLastNodePlaced();
		int i=last.getX();
		int j=last.getY();
		
		int nRows=board.GetRowsNumber();
		int nCols=board.GetColumnsNumber();
		
		//segs conterrà la lista dei gruppi che contengono una pedina adiacente a (i,j)
		List<Integer> segs=new ArrayList<Integer>();
		
		//ciclo su tutte le celle adiacenti a (i,j)
		for (Node n:last.getAdjacentNodes()){
			
			//se i e j sono nei range ammissibili
			if(n.getX()>=0 && n.getX()<nRows && n.getY()>=0 && n.getY()<nCols)
			{
				//ricavo la chiave con la procedura univoca Node2Int
				int key=Node.Node2Int(n, board.GetRowsNumber());
				
				//Se la chiave è contenuta in un gruppo
				if(groups.contains(key)){
					//aggiungo il gruppo a cui appartiene alla lista segs
					segs.add(groups.get(key));
				}	
			}

		}
		int k=segs.size();
		
		// se non ci sono pedine adiacenti a (i,j) creo un nuovo gruppo
		if (k==0)
			groups.createGroup(Node.Node2Int(new Node(i,j), board.GetRowsNumber()));
		
		// se esiste una sola pedina adiacente a (i,j) aggiungo quest'ultima al suo gruppo
		else if (k==1)
			groups.addToGroup(Node.Node2Int(new Node(i,j), board.GetRowsNumber()), segs.get(0));
		
		// se esiste più di una pedina adiacente a (i,j) unifico i gruppi di queste pedine e aggiungo a questo gruppo (i,j)
		else{
			groups.unify(segs);
			//RICORDA: unify attribuisce a tutti i gruppi il valore segs.get(0)
			groups.addToGroup(Node.Node2Int(new Node(i,j), board.GetRowsNumber()), segs.get(0));
		}
		
		// se il turno è del giocatore verticale
		if(turn==PLAYER_VERT)
		{
			//Controllo se esiste un gruppo che unisce una pedina dell'ultima riga con una pedina della prima riga
			for(int x=0; x<nRows; x++)
				for(int y=0;y<nRows;y++)
					if(groups.groupContains(Node.Node2Int(new Node(x, 0),nRows), Node.Node2Int(new Node(y, nCols-1),nRows)))
						return true;
		}
		
		//se il turno è del giocatore orizzontale
		else
		{
			//Controllo se esiste un gruppo che unisce una pedina della prima colonna con una pedina dell'ultima colonna
			for(int x=0; x<nCols; x++)
				for(int y=0;y<nCols;y++)
					if(groups.groupContains(Node.Node2Int(new Node(0, x),nRows), Node.Node2Int(new Node( nRows-1,y),nRows)))
						return true;
		}
		
		//Se non esiste alcun segmento vincente, il metodo ritorna false
		return false;

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



}
