import ia.game.hex.algorithms.AlgorithmsDefinition;
import ia.game.hex.algorithms.Board;
import ia.game.hex.algorithms.InvalidPlacementException;
import ia.game.hex.algorithms.MultipleActionExeption;
import ia.game.hex.algorithms.WinDetection;


public class MiniMaxAlgorithm extends AlgorithmsDefinition {


	private Board boardCopy;
	private WinDetection maxWinDetection;
	private WinDetection minWinDetection;
	private int rows;
	private int columns;
	private int max,min;
	/*
	 * Questo attributo ci dice se l'oggetto è stato inizializzato.
	 */
	private boolean isInitialized;

	public MiniMaxAlgorithm(String name) {
		super(name);
		//setto a false il flag di controllo dell'inizializzazione
		isInitialized=false;

	}



	@Override
	public void run() {
		//Ottengo la copia della board corrente
		boardCopy = getBoard();
		/* Controllo se l'oggetto è stato inizializzato
		 *  Se non lo è 
		 *  - assegno i valori ai giocatori min e max
		 *  - creo gli oggetti di detection della vittoria
		 *  - li registro alla board del gioco
		 */
		if(!isInitialized){
			max= getPlayer();
			min = (getPlayer()+1)%2;
			maxWinDetection = new WinDetection(max,3,3);
			minWinDetection = new WinDetection(min,3,3);
			BoardRegister(maxWinDetection);
			BoardRegister(minWinDetection);
			isInitialized=true;
		}
		//Registro i WinDetection alla copia della board
		boardCopy.addObserver(maxWinDetection);
		boardCopy.addObserver(minWinDetection);

		rows = this.getRowsNumber();
		columns = this.getColumnsNumber();

		/*
		 * Esecuzione dell'algoritmo MiniMax
		 */
		boolean stop = false;
		int i=0,j=0,x=-1;
		int lastFreeI=0,lastFreeJ=0;

		while(!stop && i<rows ){
			j=0;
			while(!stop && j<columns){
				if(!boardCopy.isBusy(i, j)){
					lastFreeI=i;
					lastFreeJ=j;
					boardCopy.movePiece(i, j,max);
					x = valoreMin();
					boardCopy.resetPosition(i,j);
				}
				j++;
				if(x == 1) stop = true;	//mi fermo alla prima mossa utile.
				System.out.println(i+","+(j-1)+" x:"+x);
			}
			i++;
		}
		
		/*
		 * Piazzamento della pedina
		 */
		try {
			/*
			 * Se la cella in cui mi sono fermato non è occupata posiziono lì la mia pedina
			 */
			if(!isBusy(i-1, j-1))
				placePiece(i-1, j-1);
			
			// La cella potrebbe essere occupata se l'algoritmo si è fermato all'ultima cella
			// e questa già è stata occupata precedentemente
			else
				placePiece(lastFreeI, lastFreeJ);
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

		//Dal momento che valori di utilità saranno o 0 o 1, scegliere v=-2 è come sceglierlo meno infinito
		int v = -2;

		for(int i = 0;i<rows;i++){
			for(int j = 0;j<columns;j++){
				if(!boardCopy.isBusy(i, j)){
					boardCopy.movePiece(i, j,max);
					v = max(v,valoreMin());
					boardCopy.resetPosition(i,j);	
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
				if(!boardCopy.isBusy(i, j)){
					boardCopy.movePiece(i, j,min);
					v = min(v,valoreMax());
					boardCopy.resetPosition(i,j);

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
