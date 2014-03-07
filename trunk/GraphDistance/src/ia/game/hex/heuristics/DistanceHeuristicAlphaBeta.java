package ia.game.hex.heuristics;
import ia.game.hex.algorithms.AlgorithmsDefinition;
import ia.game.hex.algorithms.InvalidPlacementException;
import ia.game.hex.algorithms.MultipleActionExeption;
import ia.game.hex.algorithms.Node;


public class DistanceHeuristicAlphaBeta extends AlgorithmsDefinition {

	private final static int FAILTEST = 10000;	//il test di taglio nn ha successo		
	private UndirectedHexGraph graph = null;
	private UndirectedHexGraph graphOpponent = null;
	private Node lastPlaced = null;				//ultima pedina posizionata dall'avversario
	private int rows = -1;
	private int columns = -1;
	int profonditaLimite = 0;
	int profondita = 0;
	

	public DistanceHeuristicAlphaBeta(String name,int profondita) {
		super(name);
		profonditaLimite = profondita;
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
					graph = new UndirectedHexGraph(rows,columns,UndirectedHexGraph.PLAYER_HOR);
					graphOpponent = new UndirectedHexGraph(rows,columns,UndirectedHexGraph.PLAYER_VERT);
				}else{
					graph = new UndirectedHexGraph(rows,columns,UndirectedHexGraph.PLAYER_VERT);
					graphOpponent = new UndirectedHexGraph(rows,columns,UndirectedHexGraph.PLAYER_HOR);
				}
			}catch(Exception e){
				e.printStackTrace();
			}

		}
		if(getNumberOfPiece()!=0){
			lastPlaced = getLastNodePlaced();
			System.out.println("last:"+lastPlaced.getX()+" "+lastPlaced.getY());//test
			graph.placePiece(lastPlaced.getX(),lastPlaced.getY(),UndirectedHexGraph.CUT_PLAYER);
			graphOpponent.placePiece(lastPlaced.getX(),lastPlaced.getY(),UndirectedHexGraph.CONNECT_PLAYER);
		}

		int maxUtility = -UndirectedHexGraph.INF-1;
		bestMove = new Node(0,0);
		lastEmpty = new Node(-1,-1);

		boolean stop = false;
		for(int i=0;i<rows && !stop;i++){
			for(int j=0;j<columns && !stop;j++){

				if(!graph.isBusy(i, j)){
					lastEmpty = new Node(i,j);
					graph.placePiece(i, j,UndirectedHexGraph.CONNECT_PLAYER);
					graphOpponent.placePiece(i, j,UndirectedHexGraph.CUT_PLAYER);
					int x = valoreMin(-UndirectedHexGraph.INF,UndirectedHexGraph.INF);
					System.out.println("utility:"+x);//test
					if(maxUtility < x){
						maxUtility = x;
						bestMove.setX(i);bestMove.setY(j);
					}	
					if(maxUtility==UndirectedHexGraph.INF){				//mi fermo alla prima mossa utile
						stop = true;
						System.out.println("mi fermo alla prima mossa utile");
					}
					graph.removePiece(i, j);
					graphOpponent.removePiece(i, j);
				}
			}
		}
		try {
			if(!isBusy(bestMove.getX(),bestMove.getY())){
				System.out.println("best move:"+bestMove.getX()+" "+bestMove.getY());
				placePiece(bestMove.getX(),bestMove.getY());
				graph.placePiece(bestMove.getX(),bestMove.getY(), UndirectedHexGraph.CONNECT_PLAYER);
				graphOpponent.placePiece(bestMove.getX(),bestMove.getY(), UndirectedHexGraph.CUT_PLAYER);
			}
			else{
				System.out.println("best move in a busy cell");//test
				placePiece(lastEmpty.getX(),lastEmpty.getY());
				graph.placePiece(lastEmpty.getX(),lastEmpty.getY(), UndirectedHexGraph.CONNECT_PLAYER);
				graphOpponent.placePiece(lastEmpty.getX(),lastEmpty.getY(), UndirectedHexGraph.CUT_PLAYER);
			}
		} catch (InvalidPlacementException | MultipleActionExeption e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	private int valoreMax(int alpha,int beta){
		profondita++;
		int v = -UndirectedHexGraph.INF;
		int utilita=0;
		if((utilita = TestTaglio())!=FAILTEST)
			v= utilita;
		else{
			boolean stop = false;
			for(int i=0;i<rows && !stop;i++){
				for(int j=0;j<columns && !stop;j++){
					if(!graph.isBusy(i, j)){
						graph.placePiece(i, j, UndirectedHexGraph.CONNECT_PLAYER);
						graphOpponent.placePiece(i, j, UndirectedHexGraph.CUT_PLAYER);
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
		int utilita;
		int v = UndirectedHexGraph.INF;
		if((utilita = TestTaglio())!=FAILTEST)
			v= utilita;
		else{
			boolean stop = false;
			for(int i=0;i<rows && !stop;i++){
				for(int j=0;j<columns && !stop;j++){
					if(!graph.isBusy(i, j)){
						graph.placePiece(i, j, UndirectedHexGraph.CUT_PLAYER);
						graphOpponent.placePiece(i, j, UndirectedHexGraph.CONNECT_PLAYER);
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


	private int TestTaglio(){

		int distanceM = graph.getDistance();
		int distanceO = graphOpponent.getDistance();

		if(distanceM == 1){	//ho vinto
			return UndirectedHexGraph.INF;
		}
		else if(distanceO==1){
			return -UndirectedHexGraph.INF;
		}
		else if(profondita == profonditaLimite)
			return distanceO-distanceM;
		return FAILTEST;
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

	public void printGraph(){
		graph.viewGraph("");
	}
}
