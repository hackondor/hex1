import ia.game.hex.algorithms.AlgorithmsDefinition;
import ia.game.hex.algorithms.InvalidPlacementException;
import ia.game.hex.algorithms.MultipleActionExeption;
import ia.game.hex.algorithms.Node;


/**
 * MiniMaxDH = MiniMax with Distance Heuristic
 * Minimax che utilizza la funzione euristica della distanza tra i vertici del grafo.
 * La Board viene rappresentata su un grado non direzionato.
 * Qualche informazione su questa rappresentazione del grafo si trova nell'articolo "Search in Hex, Jack van Rijswijck"
 * @author Nich
 *
 */
public class MiniMaxDH extends AlgorithmsDefinition {

	private HexGraph graph = null;
	private HexGraph graphOpponent = null;
	private Node lastPlaced = null;				//ultima pedina posizionata dall'avversario
	private int rows = -1;
	private int columns = -1;
	
	public MiniMaxDH(String name) {
		super(name);
		
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
					graph = new HexGraph(rows,columns,HexGraph.PLAYER_HOR);
					graphOpponent = new HexGraph(rows,columns,HexGraph.PLAYER_VERT);
				}else{
					graph = new HexGraph(rows,columns,HexGraph.PLAYER_VERT);
					graphOpponent = new HexGraph(rows,columns,HexGraph.PLAYER_HOR);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		if(getNumberOfPiece()!=0){
			lastPlaced = getLastNodePlaced();
			System.out.println("last:"+lastPlaced.getX()+" "+lastPlaced.getY());//test
			graph.placePiece(lastPlaced.getX(),lastPlaced.getY(),HexGraph.CUT_PLAYER);
			graphOpponent.placePiece(lastPlaced.getX(),lastPlaced.getY(),HexGraph.CONNECT_PLAYER);
		}

		int maxUtility = -1;
		bestMove = new Node(-1,-1);
		lastEmpty = new Node(-1,-1);
		
		boolean stop = false;
		for(int i=0;i<rows && !stop;i++){
			for(int j=0;j<columns && !stop;j++){
				if(!graph.isBusy(i, j)){
					lastEmpty = new Node(i,j);
					graph.placePiece(i, j,HexGraph.CONNECT_PLAYER);
					graphOpponent.placePiece(i, j,HexGraph.CUT_PLAYER);
					int x = valoreMin();
					System.out.println("utility:"+x);//test
					if(maxUtility < x){
						maxUtility = x;
						bestMove.setX(i);bestMove.setY(j);
					}	
					if(maxUtility==1){				//mi fermo alla prima mossa utile
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
				graph.placePiece(bestMove.getX(),bestMove.getY(), HexGraph.CONNECT_PLAYER);
				graphOpponent.placePiece(bestMove.getX(),bestMove.getY(), HexGraph.CUT_PLAYER);
			}
			else{
				System.out.println("best move in a busy cell");//test
				placePiece(lastEmpty.getX(),lastEmpty.getY());
				graph.placePiece(lastEmpty.getX(),lastEmpty.getY(), HexGraph.CONNECT_PLAYER);
				graphOpponent.placePiece(lastEmpty.getX(),lastEmpty.getY(), HexGraph.CUT_PLAYER);
			}
		} catch (InvalidPlacementException | MultipleActionExeption e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}
	
	private int valoreMax(){
		System.out.println("valore Max");
		//graph.viewGraph("MAX");
		int utilita;
		if((utilita = TestTerminazione())!=-1)
			return utilita;
		int v = -2;
		boolean stop = false;
		for(int i=0;i<rows && !stop;i++){
			for(int j=0;j<columns && !stop;j++){
				if(!graph.isBusy(i, j)){
					graph.placePiece(i, j, HexGraph.CONNECT_PLAYER);
					graphOpponent.placePiece(i, j, HexGraph.CUT_PLAYER);
					v = max(v,valoreMin());
					graph.removePiece(i, j);
					graphOpponent.removePiece(i, j);
					//stop = true;
				}
			}
		}
		
		return v;
	}
	
	private int valoreMin(){
		System.out.println("valore Min");
		//graph.viewGraph("MIN");
		//graphOpponent.viewGraph();
		
		int utilita;
		if((utilita = TestTerminazione())!=-1)
			return utilita;
		
		int v = 2;
		boolean stop = false;
		for(int i=0;i<rows && !stop;i++){
			for(int j=0;j<columns && !stop;j++){
				if(!graph.isBusy(i, j)){
					graph.placePiece(i, j, HexGraph.CUT_PLAYER);
					graphOpponent.placePiece(i, j, HexGraph.CONNECT_PLAYER);
					v = min(v,valoreMax());
					
					graph.removePiece(i, j);
					graphOpponent.removePiece(i, j);
					
					//stop = true;
				}
				
			}
			
		}
		
		return v;
	}
	
	
	private int TestTerminazione(){
		if(graph.getDistance() == 1){	//ho vinto
			
			return 1;
		}
		else if(graphOpponent.getDistance()==1){
			//graphOpponent.viewGraph();
			return 0;
		}
		else 
			return -1;
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
