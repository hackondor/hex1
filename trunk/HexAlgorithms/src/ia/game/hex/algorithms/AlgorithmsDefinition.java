package ia.game.hex.algorithms;

import java.util.Observable;

public abstract class AlgorithmsDefinition extends Observable{


	private Board board;
	private int player; 
	private boolean actionPlayed;
	private String name; 							//a name for the alghorithm
	//statistics info
	private int numberOfMove = 0;
	private double avgTimePlacement = 0;
	private double sumTime = 0;						//sum of the time for calculate each placement	

	public AlgorithmsDefinition(String name){
		board = null;
		player = -1;
		actionPlayed = false;
		this.name =  name;
	}

	public String getName(){
		return name;
	}

	public void setBoard(Board board) {
		this.board = board;
	}


	public void setPlayer(int player){
		this.player = player;
	}


	private long startTime;
	private long endTime;
	private long elapsedTime;
	public void action(){
		actionPlayed = false;   //permetti una mossa
		numberOfMove++;
		startTime = System.currentTimeMillis();
		run();
		endTime   = System.currentTimeMillis();
		elapsedTime = endTime - startTime;
		sumTime += elapsedTime; 
		avgTimePlacement = sumTime/numberOfMove; 
		setChanged();
		this.notifyObservers();		//invia un evento che comunica la fine del calcolo della mossa
	}

	public abstract void run();
/**
 * Prova a piazzare una pedina sulla scacchiera
 * @param i riga della cella in cui si vuole piazzare la pedina
 * @param j colonna della cella in cui si vuole piazzare la pedina
 * @throws InvalidPlacementException se la mossa non è consentita (cella già occupata o Index out of bounds)
 * @throws MultipleActionExeption se è già stata effettuata una mossa dall'algoritmo
 */
	public void placePiece(int i,int j) throws InvalidPlacementException, MultipleActionExeption {
		boolean placementFine = false;
		if(actionPlayed)
			throw new MultipleActionExeption();
		if(!actionPlayed)
			placementFine = board.movePiece(i, j, player);
		if(!placementFine)
			throw new InvalidPlacementException();
		actionPlayed = true;
	}

	/**
	 * Prova ad appropriarsi della mossa del player avversario.
	 * @param i riga della cella
	 * @param j colona della cella
	 * @throws InvalidStealException  se tale mossa non è consentita
	 * @throws MultipleActionExeption  se è già stata effettuata una mossa dall'algoritmo
	 */
	public void stealPiece(int i,int j) throws InvalidStealException, MultipleActionExeption{
		if(actionPlayed)
			throw new MultipleActionExeption();
		if(!board.isBusy(i, j)){
			System.out.println("is busy");
			throw new InvalidStealException();
		}
		if(board.getNumberOfPiece()!=1){
			System.out.println("piece: "+board.getNumberOfPiece());
			throw new InvalidStealException();

		}
		board.setPiecePlayer(i, j,player);
		actionPlayed = true;

	}
/**
 * Restituisce l'occupazione di una cella
 * @param i riga
 * @param j colonna
 * @return true se la cella è occupata, false se la cella è libera
 */
	public boolean isBusy(int i,int j){
		return board.isBusy(i, j);
	}
	/**
	 * Restituisce il numero di mosse fatte fino a quel punto dall'algoritmo
	 * @return intero che rappresenta il numero di mosse fatte dall'algoritmo
	 */
	public int getNumberOfMove() {
		return numberOfMove;
	}

	public double getAvgTimePlacement() {
		return avgTimePlacement;
	}

	public double getSumTime() {
		return sumTime;
	}
	
	/**
	 * Restituisce il numero di pedine posizionate sulla scacchiera
	 * @return il numero di pedine
	 */
	public int getNumberOfPiece(){
		return board.getNumberOfPiece();
	}
	
	/**
	 * Restituisce la proprietà di una cella
	 * @param i riga
	 * @param j colonna
	 * @return true se la cella appartiene al player corrente, false altrimenti
	 */
	public boolean isMine(int i, int j){
		if (board.belongTo(i, j)==player)
			return true;
		else
			return false;
	}

	
	public int getRowsNumber(){
		return board.GetRowsNumber();
	}
	
	public int getColumnsNumber(){
		return board.GetColumnsNumber();
	}









}
