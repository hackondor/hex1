package ia.game.hex.algorithms;



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
}

/***
 * Possiede la rappresentazione del gioco e i metodi per modificare lo stato.
 * @author Nich
 *
 */
public class Board {
	private int rows;
	private int columns;
	private Info[][] board;
	
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
	 * @return true se la mossa è lecita
	 */
	public boolean movePiece(int row,int column,int player){
	
		try{	
			if(!board[row][column].busy){
				board[row][column].player = player;
				board[row][column].busy = true;
			}
		}catch(IndexOutOfBoundsException e){
			System.out.println("row e column eccedono la dimensione della matrice");
		}
		
		
		return board[row][column].busy;
	}
	
	/**
	 * Toglie dalla board la pedina che si trova in @row e in @column
	 * @param row
	 * @param column
	 */
	public void resetPosition(int row,int column){
		board[row][column].busy = false;
		board[row][column].player = Costant.NOPLAYER;
	}
	
	public int GetRowsNumber(){
		return rows;
	}
	
	public int GetColumnsNumber(){
		return columns;
	}
	
	
	

}
