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
			graph.placePiece(lastPlaced.getX(),lastPlaced.getY(),HexGraph.CUT_PLAYER);
			graphOpponent.placePiece(lastPlaced.getX(),lastPlaced.getY(),HexGraph.CONNECT_PLAYER);
		}

		int maxUtility = -1;
		bestMove = new Node(-1,-1);
		lastEmpty = new Node(-1,-1);
		for(int i=0;i<rows;i++){
			for(int j=0;j<columns;j++){
				if(!graph.isBusy(i, j)){
					lastEmpty = new Node(i,j);
					graph.placePiece(i, j,HexGraph.CONNECT_PLAYER);
					graphOpponent.placePiece(i, j,HexGraph.CUT_PLAYER);
					int x = valoreMin();
					if(maxUtility < x){
						maxUtility = x;
						bestMove.setX(i);bestMove.setY(j);
					}
					graph.removePiece(i, j);
					graphOpponent.removePiece(i, j);
				}
			}
		}
		try {
			if(!isBusy(bestMove.getX(),bestMove.getY())){
				placePiece(bestMove.getX(),bestMove.getY());
				graph.placePiece(bestMove.getX(),bestMove.getY(), HexGraph.CONNECT_PLAYER);
				graphOpponent.placePiece(bestMove.getX(),bestMove.getY(), HexGraph.CUT_PLAYER);
			}
			else{
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
		int utilita;
		if((utilita = TestTerminazione())!=-1)
			return utilita;
		
		int v = -2;
		for(int i=0;i<rows;i++)
			for(int j=0;j<columns;j++){
				if(!graph.isBusy(i, j)){
					graph.placePiece(i, j, graph.CONNECT_PLAYER);
					graphOpponent.placePiece(i, j, graph.CUT_PLAYER);
					v = max(v,valoreMin());
					graph.removePiece(i, j);
					graphOpponent.removePiece(i, j);
					
				}
			}
				
		
		return v;
	}
	
	private int valoreMin(){
		int utilita;
		if((utilita = TestTerminazione())!=-1)
			return utilita;
		
		int v = 2;
		for(int i=0;i<rows;i++)
			for(int j=0;j<columns;j++){
				if(!graph.isBusy(i, j)){
					graph.placePiece(i, j, graph.CUT_PLAYER);
					graphOpponent.placePiece(i, j, graph.CONNECT_PLAYER);
					v = min(v,valoreMax());
					graph.removePiece(i, j);
					graphOpponent.removePiece(i, j);
					
				}
			}
				
		
		return v;
	}
	
	
	private int TestTerminazione(){
		if(graph.getDistance() == 1)	//ho vinto
			return 1;
		else if(graphOpponent.getDistance()==1)
			return 0;
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

}
