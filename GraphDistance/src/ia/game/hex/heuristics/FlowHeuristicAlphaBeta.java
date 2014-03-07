package ia.game.hex.heuristics;
import ia.game.hex.algorithms.AlgorithmsDefinition;
import ia.game.hex.algorithms.InvalidPlacementException;
import ia.game.hex.algorithms.MultipleActionExeption;
import ia.game.hex.algorithms.Node;


public abstract class FlowHeuristicAlphaBeta extends AlgorithmsDefinition {

	protected final static int FAILTEST = 10000000;	//il test di taglio nn ha successo		
	protected DirectedHexGraph graph = null;
	protected DirectedHexGraph graphOpponent = null;
	private Node lastPlaced = null;				//ultima pedina posizionata dall'avversario
	private int rows = -1;
	private int columns = -1;
	protected int profonditaLimite = 0;
	protected int profondita = 0;
	private ScrollAction scroll = null;
	protected int ratio = 0;



	public FlowHeuristicAlphaBeta(String name,int profondita,int ratio) {
		super(name);
		profonditaLimite = profondita;
		this.ratio = ratio;
	}

	@Override
	public void run() {
		
		Node bestMove;
		Node lastEmpty;
		if(graph == null){
			rows = getRowsNumber();
			columns = getColumnsNumber();
			try{
				if(getPlayer()==0){
					graph = new DirectedHexGraph(rows,columns,DirectedHexGraph.PLAYER_HOR);
					graphOpponent = new DirectedHexGraph(rows,columns,DirectedHexGraph.PLAYER_VERT);
					scroll = new ScrollActionByColumn(rows, columns);


				}else{
					graph = new DirectedHexGraph(rows,columns,DirectedHexGraph.PLAYER_VERT);
					graphOpponent = new DirectedHexGraph(rows,columns,DirectedHexGraph.PLAYER_HOR);
					scroll = new ScrollActionByRow(rows, columns);
				}
			}catch(Exception e){
				e.printStackTrace();
			}

		}

		scroll.reset();//reinizializza la classe che itera sulle azioni disponibili
		if(getNumberOfPiece()!=0){
			lastPlaced = getLastNodePlaced();
			System.out.println("last:"+lastPlaced.getX()+" "+lastPlaced.getY());//test
			graph.placePiece(lastPlaced.getX(),lastPlaced.getY(),DirectedHexGraph.CUT_PLAYER);
			graphOpponent.placePiece(lastPlaced.getX(),lastPlaced.getY(),DirectedHexGraph.CONNECT_PLAYER);
		}

		int maxUtility = -DirectedHexGraph.INF-1;
		bestMove = new Node(0,0);
		lastEmpty = new Node(-1,-1);



		boolean stop = false;
		long totalTime=System.currentTimeMillis();

		int i=0,j=0;
		while(!scroll.isEndCycle() && !stop){
			i = scroll.getFirstIndex();
			j = scroll.getSecondIndex();
			if( !((i==0 || i==rows-1) && getNumberOfPiece()==0)){
				if(!graph.isBusy(i, j)){
					lastEmpty = new Node(i,j);
					graph.placePiece(i, j,DirectedHexGraph.CONNECT_PLAYER);
					graphOpponent.placePiece(i, j,DirectedHexGraph.CUT_PLAYER);
					long time1 = System.currentTimeMillis();
					int x = valoreMin(-DirectedHexGraph.INF,DirectedHexGraph.INF);
					long time2 = System.currentTimeMillis();
					long time=(time2-time1);
					System.out.println("Ramo: "+(i*getRowsNumber()+j)+" tempo: "+time);//test
					if(maxUtility < x){
						maxUtility = x;
						bestMove.setX(i);bestMove.setY(j);
					}	
					if(maxUtility==DirectedHexGraph.INF){				//mi fermo perchè ho vinto
						stop = true;
						System.out.println("mi fermo perchè ho vinto");
					}
					graph.removePiece(i, j);
					graphOpponent.removePiece(i, j);
				}
			}
			scroll.increment();
		}

	

	totalTime=(System.currentTimeMillis()-totalTime);
	System.out.println("Tempo totale impiegato: "+totalTime);
	try {
		if(!isBusy(bestMove.getX(),bestMove.getY())){
			System.out.println("best move:"+bestMove.getX()+" "+bestMove.getY());
			placePiece(bestMove.getX(),bestMove.getY());
			graph.placePiece(bestMove.getX(),bestMove.getY(), DirectedHexGraph.CONNECT_PLAYER);
			graphOpponent.placePiece(bestMove.getX(),bestMove.getY(), DirectedHexGraph.CUT_PLAYER);
		}
		else{
			System.out.println("best move in a busy cell");//test
			placePiece(lastEmpty.getX(),lastEmpty.getY());
			graph.placePiece(lastEmpty.getX(),lastEmpty.getY(), DirectedHexGraph.CONNECT_PLAYER);
			graphOpponent.placePiece(lastEmpty.getX(),lastEmpty.getY(), DirectedHexGraph.CUT_PLAYER);
		}
	} catch (InvalidPlacementException | MultipleActionExeption e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}


}


private int valoreMax(int alpha,int beta){
	profondita++;
	int v = -DirectedHexGraph.INF;
	int utilita=0;
	if((utilita = TestTaglio())!=FAILTEST)
		v= utilita;
	else{
		boolean stop = false;
		for(int i=0;i<rows && !stop;i++){
			for(int j=0;j<columns && !stop;j++){
				if(!graph.isBusy(i, j)){
					graph.placePiece(i, j, DirectedHexGraph.CONNECT_PLAYER);
					graphOpponent.placePiece(i, j, DirectedHexGraph.CUT_PLAYER);
					v = max(v,valoreMin(alpha,beta));

					graph.removePiece(i, j);
					graphOpponent.removePiece(i, j);
					if(v>=beta){
						profondita--;
						return v;
					}
					alpha = max(v,alpha);

					//stop = true;
				}
			}
		}
	}

	profondita--;
	return v;
}


private int valoreMin(int alpha,int beta){
	profondita++;
	int utilita = 0;
	int v = DirectedHexGraph.INF;
	if((utilita = TestTaglio())!=FAILTEST)
		v= utilita;
	else{
		boolean stop = false;
		for(int i=0;i<rows && !stop;i++){
			for(int j=0;j<columns && !stop;j++){
				if(!graph.isBusy(i, j)){
					graph.placePiece(i, j, DirectedHexGraph.CUT_PLAYER);
					graphOpponent.placePiece(i, j, DirectedHexGraph.CONNECT_PLAYER);
					v = min(v,valoreMax(alpha,beta));

					graph.removePiece(i, j);
					graphOpponent.removePiece(i, j);
					if(v<=alpha){
						profondita--;
						return v;
					}
					beta = min(v,beta);



					//stop = true;
				}

			}
		}
	}
	profondita--;
	return v;
}


public abstract int TestTaglio();

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

public void printGraph(){
	graph.viewGraph("");
}
}
