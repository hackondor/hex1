import ia.game.hex.algorithms.AlgorithmsDefinition;
import ia.game.hex.algorithms.Board;
import ia.game.hex.algorithms.InvalidPlacementException;
import ia.game.hex.algorithms.MultipleActionExeption;
import ia.game.hex.algorithms.WinDetection;


public class MiniMaxAlgorithm extends AlgorithmsDefinition {

	
	private Board board;
	private WinDetection maxWinDetection;
	private WinDetection minWinDetection;
	private int rows;
	private int columns;
	private int max,min;
	
	public MiniMaxAlgorithm(String name) {
		super(name);

		max= getPlayer();
		min = (getPlayer()+1)%2;

	}

	
	
	@Override
	public void run() {
		int x = -99;
		boolean stop = false;
		int i=0,j=0;
		board = getBoard();
		maxWinDetection = new WinDetection(max,board.GetRowsNumber(),board.GetColumnsNumber());
		minWinDetection = new WinDetection(min,board.GetRowsNumber(),board.GetColumnsNumber());
		board.addObserver(maxWinDetection);
		board.addObserver(minWinDetection);
		rows = this.getRowsNumber();
		columns = this.getColumnsNumber();
		
		while(!stop && i<rows ){
			j=0;
			while(!stop && j<columns){
				if(!board.isBusy(i, j)){
					board.movePiece(i, j,max);
					x = valoreMin();
					board.resetPosition(i,j);
				}
				j++;
				if(x == 1) stop = true;	//mi fermo alla prima mossa utile.
				System.out.println(i+","+(j-1)+" x:"+x);
			}
			i++;
		}
		
		try {
			System.out.println("piazzamento ia: "+(i-1)+";"+(j-1)+" occupata: "+board.isBusy(i-1, j-1));//test
			placePiece(i-1, j-1);
		} catch (InvalidPlacementException | MultipleActionExeption e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

	
	private int valoreMax(){
		
		int x = 0;
		x = isTerminalState();
		if(x==1 || x==0)
			return x; 
		
		int v = -2;
		
		for(int i = 0;i<rows;i++){
			for(int j = 0;j<columns;j++){
				if(!board.isBusy(i, j)){
					board.movePiece(i, j,max);
					v = max(v,valoreMin());
					board.resetPosition(i,j);	
				}
			}
		}
		
		return v;
	}
	
	
	private int valoreMin(){
		
		int x = 0;
		x = isTerminalState();
		if(x==1 || x==0)
			return x; 
		
		int v = 2;
		
		for(int i = 0;i<rows;i++){
			for(int j = 0;j<columns;j++){
				if(!board.isBusy(i, j)){
					board.movePiece(i, j,min);
					v = min(v,valoreMax());
					board.resetPosition(i,j);
					
				}
			}
		}
		
		return v;
	}
	
	
	private int min(int x,int y){
		if(x<y)
			return x;
		else return y;
	}
	
	private int max(int x,int y){
		if(x>y)
			return x;
		else return y;
	}
	
//
//	//{0,1} partita finita. -1 partita ancora da giocare
//	private int isTerminalState(){
//		if(maxWinDetection.isWin())
//			return 1;
//		else if(minWinDetection.isWin())
//			return 0;
//		else return -1;
//
//	}
	
	
	private int isTerminalState(){
		double rand = Math.random();
		if(rand>0.5)
			return 1;
		else
			return 0;
	}

}
