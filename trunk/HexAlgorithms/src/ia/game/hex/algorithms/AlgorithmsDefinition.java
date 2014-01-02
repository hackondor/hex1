package ia.game.hex.algorithms;

public abstract class AlgorithmsDefinition{
	

	private Board board;
	private int player; 
	private boolean piecePlaced;
	private Arbiter arbiter;
	
	
	public AlgorithmsDefinition(){
		board = null;
		player = -1;
		piecePlaced = false;
	}
	
	public void setBoard(Board board) {
		this.board = board;
	}
	
	public void setArbiter(Arbiter a){
		arbiter = a;
	}
	
	public void setPlayer(int player){
		this.player = player;
	}
	
	
	public void action(){
		piecePlaced = false;   //permetti una mossa
		run();
	}
	
	public abstract void run();
	
	public void placePiece(int i,int j) throws InvalidPlacementException, MultiplePlacementExeption {
		boolean placementFine = false;
		if(piecePlaced)
			throw new MultiplePlacementExeption();
		if(!piecePlaced)
			placementFine = board.movePiece(i, j, player);
		if(!placementFine)
			throw new InvalidPlacementException();
		piecePlaced = true;
	}
	
	public boolean isBusy(int i,int j){
		return board.isBusy(i, j);
	}
	

	
	
	
	 
	

}
