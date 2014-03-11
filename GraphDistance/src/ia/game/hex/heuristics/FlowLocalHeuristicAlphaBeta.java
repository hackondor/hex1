package ia.game.hex.heuristics;
import java.util.ArrayList;

import ia.game.hex.algorithms.AlgorithmsDefinition;
import ia.game.hex.algorithms.InvalidPlacementException;
import ia.game.hex.algorithms.MultipleActionExeption;
import ia.game.hex.algorithms.Node;


public  class FlowLocalHeuristicAlphaBeta extends AlgorithmsDefinition {


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

	private ArrayList<Node> adjacents = new ArrayList<Node>();

	private PatternRecognition patternRecognition;

	public FlowLocalHeuristicAlphaBeta(String name,int profondita,int ratio) {
		super(name);
		profonditaLimite = profondita;
		this.ratio = ratio;

	}



	private ArrayList<Node> getAdjacents(Node n){
		//adjacents = new ArrayList<Node>();
		int i = n.getX();int j = n.getY();
		//		if(j<columns-1){
		//			adjacents.add(new Node(i,j+1));
		//		}
		//		if(j>0){
		//			adjacents.add(new Node(i,j-1));
		//		}
		//		if(i>0){
		//			adjacents.add(new Node(i-1,j));
		//		}
		//		if(i>0 && j<columns-1){
		//			adjacents.add(new Node(i-1,j+1));
		//		}
		//		if(i<rows-1){
		//			adjacents.add(new Node(i+1,j));
		//		}
		//		if(i<rows-1 && j>0){
		//			adjacents.add(new Node(i+1,j-1));
		//		}
		if(i>1 && j<columns-1)
			adjacents.add(new Node(i-2,j+1));
		if(i>1 && j<columns-2)
			adjacents.add(new Node(i-2,j+2));
		if(i>0 && j<columns-2)
			adjacents.add(new Node(i-1,j+2));
		if(j<columns-2)
			adjacents.add(new Node(i,j+2));
		if(i<rows-1 && j<columns-1)
			adjacents.add(new Node(i+1,j+1));
		if(i<rows-2)
			adjacents.add(new Node(i+2,j));
		if(i<columns-2 && j>0)
			adjacents.add(new Node(i+2,j-1));
		if(i<rows-2 && j>1)
			adjacents.add(new Node(i+2,j-2));
		if(i<rows-1 && j>1)
			adjacents.add(new Node(i+1,j-2));
		if(j>1)
			adjacents.add(new Node(i,j-2));
		if(i>0 && j>0)
			adjacents.add(new Node(i-1,j-1));
		if(i>1)
			adjacents.add(new Node(i-2,j));


		return adjacents;
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
				patternRecognition = new PatternRecognition(graph.getNodes(),HexGraph.CUT_PLAYER, rows, graph.getSource(),graph.getDest());
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
			ArrayList<Pattern> patterns = patternRecognition.detectPattern1(new ia.game.hex.heuristics.Node(lastPlaced.getX(),lastPlaced.getY()));
			for(Pattern p:patterns){
				for(ia.game.hex.heuristics.Node n:p.getEmptyNodes()){
					graph.placePiece(n.getRow(), n.getColumn(),HexGraph.CUT_PLAYER);
					graphOpponent.placePiece(n.getRow(), n.getColumn(),HexGraph.CONNECT_PLAYER);
				}
			}


		}

		int maxUtility = -DirectedHexGraph.INF-1;
		bestMove = new Node(0,0);
		lastEmpty = new Node(-1,-1);


		if(getNumberOfPiece()==0){
			try {
				placePiece(5,4);
			} catch (InvalidPlacementException | MultipleActionExeption e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else{
			Node n1 = getLastNodePlaced();
			adjacents = getAdjacents(n1);
			for(Node n:adjacents){
				int i = n.getX();int j = n.getY();
				if(!isBusy(n.getX(),n.getY())){
					lastEmpty = new Node(i,j);
					graph.placePiece(i, j,DirectedHexGraph.CONNECT_PLAYER);
					graphOpponent.placePiece(i, j,DirectedHexGraph.CUT_PLAYER);


					int x = valoreMin(-DirectedHexGraph.INF,DirectedHexGraph.INF);
					if(maxUtility < x){
						maxUtility = x;
						bestMove.setX(i);bestMove.setY(j);
					}	
					if(maxUtility==DirectedHexGraph.INF){				//mi fermo perchè ho vinto
						System.out.println("mi fermo perchè ho vinto");
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
			for(Node n:adjacents){
				int i = n.getX();int j=n.getY();
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
			for(Node n:adjacents){
				int i = n.getX();int j=n.getY();
				if(!graph.isBusy(i, j)){
					graph.placePiece(i, j, DirectedHexGraph.CUT_PLAYER);
					graphOpponent.placePiece(i, j, DirectedHexGraph.CONNECT_PLAYER);
				}
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

		profondita--;
		return v;
	}


	public int TestTaglio() {
		//int flowM = graph.getMaxFlow();
		int flowO = graphOpponent.getMaxFlow();
		int distanceO = graphOpponent.getDistance();
		int distance = graph.getDistance();
		//int distance2 = graph.getDistance2();
		
		if(distance == 1){	//ho vinto
			return DirectedHexGraph.INF;
		}
		else if(distanceO==1){
			return -DirectedHexGraph.INF;
		}
		else if(profondita == profonditaLimite)
			return -flowO;//100
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
