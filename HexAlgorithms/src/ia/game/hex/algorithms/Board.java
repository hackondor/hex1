package ia.game.hex.algorithms;

import java.util.Observable;

/***
 * Stato di ogni cella del gioco.
 * @author Nich
 *
 */
class Info{
	boolean busy;
	int player;
	Info(){
		busy = false;
		player = Costant.NOPLAYER;
	}
	Info(boolean busy, int player){
		this.busy=busy;
		this.player=player;
	}
	public Info takeACopy(){
		return new Info(busy,player);
	}
}

/***
 * Possiede la rappresentazione del gioco e i metodi per modificare lo stato.
 * @author Nich
 *
 */
public class Board extends Observable {
	private int rows;
	private int columns;
	private Info[][] board;
	private int number_of_piece = 0;				// numero di pedine sulla scacchiera
	private boolean isStealDone = false;			// � stato fatto lo steal della prima mossa
	private Node lastNodePlaced;					// mantiene gli indici dell'ultima pedina posizionata sulla scacchiera
	
	/**
	 * Crea una bord delle dimnesione @rows x @columns
	 * @param rows
	 * @param columns
	 */
	public Board(int rows,int columns){
		this.rows = rows;
		this.columns = columns;
		board = new Info[rows][columns];
		for(int i=0;i<rows;i++)
			for(int j=0;j<columns;j++)
				board[i][j] = new Info();
	}

	/**
	 * Posiziona la pedina del @player nella posizione indicata da @row e @column
	 * @param row
	 * @param column
	 * @param player
	 * @return true se la mossa � lecita
	 */
	public boolean placePiece(int row,int column,int player){

		boolean check = false;
		try{	
			if(!board[row][column].busy){
				board[row][column].player = player;
				board[row][column].busy = true;
				number_of_piece++;
				check = true;
				lastNodePlaced=new Node(row, column);
				this.setChanged();
				this.notifyObservers(new BoardEvent(row,column,BoardEvent.SET,player));

			}
		}catch(IndexOutOfBoundsException e){
			e.printStackTrace();
			System.out.println("row e column eccedono la dimensione della matrice");
		}
		return check;
	}

	/**
	 * Toglie dalla board la pedina che si trova in @row e in @column
	 * @param row
	 * @param column
	 */
	public void resetPosition(int row,int column){
		int oldPlayer=board[row][column].player;
		board[row][column].busy = false;
		board[row][column].player = Costant.NOPLAYER;
		number_of_piece--;
		this.setChanged();
		this.notifyObservers(new BoardEvent(row,column,BoardEvent.UNSET,oldPlayer));
	}

	public int GetRowsNumber(){
		return rows;
	}

	public int GetColumnsNumber(){
		return columns;
	}

	/**
	 * 
	 * @param riga della cella
	 * @param colonna della cella
	 * @return true se la cella � occupata
	 */
	public boolean isBusy(int row,int column){
		return board[row][column].busy;
	}

	/**
	 * 
	 * @param riga della cella
	 * @param colonna della cella
	 * @return	l'identificativo del player a cui appartiene la cella. Se � vuota ritorna la costante NOPLAYER
	 */
	public int belongTo(int row,int column){
		return board[row][column].player;
	}

	/**
	 * 
	 * @return un intero contenente il numero di pedine attualmente posizionate sulla board
	 */
	public int getNumberOfPiece(){
		return number_of_piece;
	}

	/**
	 * Cambia il player che possiede la pedina i,j 
	 * @param i
	 * @param j
	 * @param player il nuovo possessore della pedina
	 */
	public boolean stealPiece(int i,int j,int player){
		if(!isStealDone){
			isStealDone=true;
			board[i][j].player = player;
			this.setChanged();
			this.notifyObservers(new BoardEvent(i,j,BoardEvent.STEAL,player));
			return true;
		}
		else
			return false;
	}

	/**
	 * Riporta la pedina (i,j) al player avversario come reset da uno steal
	 * @param i riga della cella
	 * @param j colonna della cella
	 */
	public void resetFromSteal(int i, int j){
		if(isStealDone){
			int old=board[i][j].player;
			board[i][j].player = (old+1)%2;
			isStealDone=false;
			this.setChanged();
			this.notifyObservers(new BoardEvent(i,j,BoardEvent.UNSET,old));
		}
	}



	public Node getLastNodePlaced() {
		return lastNodePlaced;
	}

	/**
	 * fornisce un oggetto Board che � esattamente la copia della board corrente
	 * @return	un oggetto Board
	 */
	public Board takeACopy(){
		Board b = new Board(rows,columns);
		if(lastNodePlaced!=null)
			b.lastNodePlaced = new Node(lastNodePlaced.getX(),lastNodePlaced.getY());
		else
			b.lastNodePlaced = null;
		b.number_of_piece=number_of_piece;
		b.isStealDone=isStealDone;
		for(int i=0;i<rows;i++)
			for(int j=0;j<columns;j++)
				b.board[i][j] = board[i][j].takeACopy();
		return b;
	}

	/**
	 * Stabilisce se lo steal della prima mossa � consentito nel corrente stato della board
	 * @return true se lo steal � consentito
	 */
	public boolean isStealLegal(){
		return !isStealDone && number_of_piece==1;
	}

}
