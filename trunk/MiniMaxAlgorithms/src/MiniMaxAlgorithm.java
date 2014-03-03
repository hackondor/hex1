import ia.game.hex.algorithms.AlgorithmsDefinition;
import ia.game.hex.algorithms.Board;
import ia.game.hex.algorithms.InvalidPlacementException;
import ia.game.hex.algorithms.InvalidStealException;
import ia.game.hex.algorithms.MultipleActionExeption;
import ia.game.hex.algorithms.Node;
import ia.game.hex.algorithms.WinDetection;


public class MiniMaxAlgorithm extends AlgorithmsDefinition {


	private Board localBoard;
	private WinDetection maxWinDetection;
	private WinDetection minWinDetection;
	private int rows;
	private int columns;
	private int max,min;
	private Node lastNode;
	/*
	 * Questo attributo ci dice se l'oggetto è stato inizializzato.
	 */


	public MiniMaxAlgorithm(String name) {
		super(name);
		lastNode=new Node(-1,-1);
	}

	@Override
	public void setBoard(Board b){
		super.setBoard(b);
		//Creo la board locale su cui lavorerà l'algoritmo
		localBoard=new Board(getRowsNumber(),getColumnsNumber());
		rows = this.getRowsNumber();
		columns = this.getColumnsNumber();

	}

	@Override
	public void setPlayer(int p){
		super.setPlayer(p);
		// indico con max il player corrente e con min l'avversario
		max= getPlayer();
		min = (getPlayer()+1)%2;
		maxWinDetection = new WinDetection(max,3,3);
		minWinDetection = new WinDetection(min,3,3);
		localBoard.addObserver(maxWinDetection);
		localBoard.addObserver(minWinDetection);
		
	}

	@Override
	public void run() {
		
		// node conterrà gli indici dell'ultima pedina inserita sulla Board reale
		Node node = getLastNodePlaced();
		
		
		if(node!=null)			//se almeno una pedina è stata piazzata
			
			/* c'è stato uno steal in quanto l'ultima pedina sulla board l'ha inserita l'algoritmo stesso
			 * aggiorno la board locale con tale mossa		
			 */
			if(node.getX()==lastNode.getX() && node.getY()==lastNode.getY()){

				localBoard.stealPiece(node.getX(), node.getY(), min);
			}
			/*
			 * aggiorno la board locale con l'ultima pedina inserita
			 */
			else{
				localBoard.placePiece(node.getX(), node.getY(), min);
			}

		/*
		 * Esecuzione dell'algoritmo MiniMax
		 */
		boolean stop = false;
		int i=0,j=0,x=-1;
		int lastFreeI=0,lastFreeJ=0;
		int stealI=-1,stealJ =-1;

		while(!stop && i<rows ){
			j=0;
			while(!stop && j<columns){
					/*
					 * se la cella i-j è libera posso piazzare la pedina			
					 */
				if(!localBoard.isBusy(i, j)){
					
					lastFreeI=i;
					lastFreeJ=j;
					
					localBoard.placePiece(i, j,max);
					x = valoreMin();
					localBoard.resetPosition(i,j);

				}
				/*
				 * se la cella è occupata non posso piazzare la pedina ma potrei rubarla
				 */
				else if(localBoard.isBusy(i, j) && localBoard.isStealLegal())
				{
					stealI=i;
					stealJ=j;
					localBoard.stealPiece(i, j,max);
					x = valoreMin();
					localBoard.resetFromSteal(i, j);
				}
				j++;
				if(x == 1) stop = true;	//mi fermo alla prima mossa utile.

			}
			i++;
		}

		/*
		 * Esecuzione della mossa
		 */
		try {
			/*
			 * Se la cella in cui mi sono fermato non è occupata posiziono lì la mia pedina
			 * e aggiorno la board locale
			 */
			if(!isBusy(i-1, j-1)){
				placePiece(i-1, j-1);
				localBoard.placePiece(i-1, j-1, max);
				lastNode=new Node(i-1,j-1);
			}

			
			else
				/*
				 * se la cella dove mi sono fermato è occupata ma l'algoritmo ha scelto di
				 * rubare la pedina (stealI diverso da -1) allora rubo e aggiorno la
				 * board locale
				 */
				if (stealI!=-1){

					localBoard.stealPiece(stealI, stealJ, max);
					stealPiece(stealI, stealJ);
				}
			/*
			 * altrimenti devo piazzare la pedina in una posizione libera
			 */
				else{
					localBoard.placePiece(lastFreeI, lastFreeJ, max);
					placePiece(lastFreeI, lastFreeJ);
					lastNode=new Node(lastFreeI,lastFreeJ);
				}
		} catch (InvalidPlacementException | MultipleActionExeption e) {
			e.printStackTrace();
		} catch (InvalidStealException e) {
			e.printStackTrace();
		}


	}



	private int valoreMax(){

		int x = 0;
		x = isTerminalState();
		if(x==1 || x==0)
			return x; 

		//Dal momento che valori di utilità saranno o 0 o 1, scegliere v=-2 è come sceglierlo meno infinito
		int v = -2;

		for(int i = 0;i<rows;i++){
			for(int j = 0;j<columns;j++){
				if(!localBoard.isBusy(i, j)){
					localBoard.placePiece(i, j,max);
					v = max(v,valoreMin());
					localBoard.resetPosition(i,j);	
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
		//Dal momento che valori di utilità saranno o 0 o 1, scegliere v=2 è come sceglierlo più infinito

		int v = 2;

		for(int i = 0;i<rows;i++){
			for(int j = 0;j<columns;j++){
				if(!localBoard.isBusy(i, j)){
					localBoard.placePiece(i, j,min);
					v = min(v,valoreMax());
					localBoard.resetPosition(i,j);
				}
				else if(localBoard.isBusy(i, j) && localBoard.isStealLegal())
				{
					//System.out.println("sono entrato. Num: "+boardCopy.getNumberOfPiece()+"legal "+boardCopy.isStealLegal());
					localBoard.stealPiece(i, j,max);
					v = max(v,valoreMax());
					localBoard.resetFromSteal(i, j);
				}
			}
		}

		return v;
	}


	private int min(int x,int y){
		if(x<=y)
			return x;
		else return y;
	}

	private int max(int x,int y){
		if(x>=y)
			return x;
		else return y;
	}


	//{0,1} partita finita. -1 partita ancora da giocare
	private int isTerminalState(){
		if(maxWinDetection.isWin())
			return 1;
		else if(minWinDetection.isWin())
			return 0;
		else return -1;
	}

}
