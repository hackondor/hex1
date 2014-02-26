package ia.game.hex.algorithms;

public abstract class AlgorithmsDefinition{
	

	private Board board;
	private int player; 
	private boolean actionPlayed;
	private String name; 							//a name for the alghorithm
	//statistics info
	private int numberOfMove = 0;
	private double avgTimePlacement = 0;
	private double sumTime = 0;						//sum of the time for caclculate each placement	
	
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
	}
	
	public abstract void run();
	
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
	
	public boolean isBusy(int i,int j){
		return board.isBusy(i, j);
	}

	public int getNumberOfMove() {
		return numberOfMove;
	}

	public double getAvgTimePlacement() {
		return avgTimePlacement;
	}

	public double getSumTime() {
		return sumTime;
	}
	
	
	

	
	
	
	 
	

}
