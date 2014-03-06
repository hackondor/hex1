import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.swing.JFrame;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;

import library.pattern.command.Action;
import library.pattern.command.Command;
import library.pattern.command.CommandDoUndo;
import edu.uci.ics.jung.algorithms.flows.EdmondsKarpMaxFlow;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;



public class DirectedHexGraph implements HexGraph {

	private final static int MAXFLOW = 9999;
	private int rows = -1;
	private int columns = -1;
	private DirectedSparseGraph<Node, Edge> g = new DirectedSparseGraph<Node, Edge>();		//grafo  direzionato
	private int k = 1;															//progressivo con cui vengono numerati gli archi del grafo					
	private Node[][] nodes;														//mantiene i riferimenti ai nodi del grafo
	private Node s = new Node(-1,-1),t = new Node(8,8);							//nodo sorgente e nodo destinazione del grafo
	private Stack<Command<Node>> commandHistory; 
	private int direction = -1;


	/**
	 * la classe custodisce le 2 azioni che realizzano e disfano l'operazione di Reduce 
	 * @author Nich
	 *
	 */
	private class Reduce{
		private Collection<Node> adjacents = null;				//i nodi adiacenti a quello che scatena la Reduce
		private ArrayList<Pair<Node>> pairs_added_edge= null;	//tutte le coppie che sono stati collegate da archi dalla Reduce
		private boolean isConnectedToSource = false;
		private class ReduceAction implements Action<Node>{

			@Override
			public boolean run(Node n){
				pairs_added_edge = new ArrayList<Pair<Node>>();
				adjacents = getNeighbors(n);
				Collection<Node> source_adjacents = getNeighbors(s);
				if(source_adjacents.contains(n)){
					isConnectedToSource = true;
					for(Node node1:adjacents){
						if(!source_adjacents.contains(node1)){
							Edge e = new Edge(k++,MAXFLOW);
							g.addEdge(e, s,node1);
							pairs_added_edge.add(new Pair<Node>(s,node1));
						}
					}
				}

				//realizzo i collegamenti tra tutte le coppie di nodi adiacenti liberi
				for(Node node1:adjacents){
					for(Node node2:adjacents){
						if(node1!=node2)
							if(!isNeighbor(node2, node1)){
								if(node1!=t){
									Edge e = new Edge(k++,1);
									g.addEdge(e,node1, node2);
									if(!isNeighbor(node1, node2)){
										Edge e1 = new Edge(k++,1);
										g.addEdge(e1, node2, node1);
										pairs_added_edge.add(new Pair<Node>(node2,node1));
									}
									pairs_added_edge.add(new Pair<Node>(node1,node2));
								}else{
									Edge e = new Edge(k++,MAXFLOW);
									g.addEdge(e,node1, node2);
									pairs_added_edge.add(new Pair<Node>(node1,node2));
								}
							}
					}
				}

				//rimozione del nodo dal grafo. Questo cancellerà anche tutti gli archi tra il nodo e gli adiacenti

				g.removeVertex(n);


				return true;
			}

		}

		private class UndoReduceAction implements Action<Node>{

			@Override
			public boolean run(Node n) {
				//rimozione di tutti gli archi inseriti dalla Reduce
				for(Pair<Node> p:pairs_added_edge){
					g.removeEdge(g.findEdge(p.getFirst(), p.getSecond()));
				}

				//aggiunta del vertice rimosso
				g.addVertex(n);


				if(isConnectedToSource)
					g.addEdge(new Edge(k++,MAXFLOW),s,n);
				//aggiunta di tutti gli archi tra n e gli adiacenti.
				for(Node node:adjacents){
					if(node!=t){
						g.addEdge(new Edge(k++,1),node, n);
						g.addEdge(new Edge(k++,1),n,node);
					}else
						g.addEdge(new Edge(k++,MAXFLOW),n,node);

				}

				return true;
			}
		}
		private ReduceAction doAction;
		private UndoReduceAction undoAction;

		public Reduce(){
			doAction = new ReduceAction();
			undoAction = new UndoReduceAction();
		}

		public Action<Node> getDoAction(){
			return doAction;
		}

		public Action<Node> getUndoDoAction(){
			return undoAction;
		}
	}

	/**
	 * la classe custodisce le 2 azioni che realizzano e disfano l'operazione di Cut
	 * @author Nich
	 *
	 */
	private class Cut{
		private Collection<Node> adjacents = null;				//i nodi adiacenti a quello che scatena la Cut
		private boolean isConnectedToSource = false;
		private class CutAction implements Action<Node>{

			@Override
			public boolean run(Node n) {
				adjacents = getNeighbors(n);
				Collection<Node> source_adjacents = getNeighbors(s);
				if(source_adjacents.contains(n))
					isConnectedToSource = true;


				g.removeVertex(n);
				return true;
			}
		}
		private class UndoCutAction implements Action<Node>{

			@Override
			public boolean run(Node n) {
				g.addVertex(n);
				if(isConnectedToSource)
					g.addEdge(new Edge(k++,MAXFLOW), s,n);
				for(Node node:adjacents){
					if(node!=t){
						g.addEdge(new Edge(k++,1),n, node);
						g.addEdge(new Edge(k++,1),node, n);
					}else
						g.addEdge(new Edge(k++,MAXFLOW),n, t);
				}
				return true;
			}

		}


		private CutAction doAction;
		private UndoCutAction undoAction;

		public Cut(){
			doAction = new CutAction();
			undoAction = new UndoCutAction();
		}

		public Action<Node> getDoAction(){
			return doAction;
		}

		public Action<Node> getUndoDoAction(){
			return undoAction;
		}


	}


	public DirectedHexGraph(int rows,int columns,int direction) throws Exception{
		if(direction!=0 && direction!=1)
			throw new Exception();
		this.direction = direction;
		this.rows = rows;
		this.columns = columns;
		commandHistory = new Stack<Command<Node>>();
		nodes = new Node[rows][columns];
		create();


	}

	private void create(){


		//crea vertici
		for(int i=0;i<rows;i++){
			for(int j=0;j<columns;j++){
				nodes[i][j] = new Node(i,j);
				g.addVertex(nodes[i][j]);
			}
		}


		//crea collegamenti tra i vertici

		for(int i=0;i<rows;i++){
			for(int j=0;j<columns;j++){
				if(j<columns-1){
					g.addEdge(new Edge(k++,1),nodes[i][j],nodes[i][j+1]);
				}
				if(j>0){
					g.addEdge(new Edge(k++,1),nodes[i][j],nodes[i][j-1]);
				}
				if(i>0){
					g.addEdge(new Edge(k++,1),nodes[i][j],nodes[i-1][j]);
				}
				if(i>0 && j<columns-1){
					g.addEdge(new Edge(k++,1),nodes[i][j],nodes[i-1][j+1]);
				}
				if(i<rows-1){
					g.addEdge(new Edge(k++,1),nodes[i][j],nodes[i+1][j]);
				}
				if(i<rows-1 && j>0){
					g.addEdge(new Edge(k++,1),nodes[i][j],nodes[i+1][j-1]);
				}
			}
		}

		if(direction == PLAYER_HOR){
			for(int j=0;j<rows;j++)
				g.addEdge(new Edge(k++,MAXFLOW), s,nodes[j][0]);

			for(int j=0;j<rows;j++)
				g.addEdge(new Edge(k++,MAXFLOW),nodes[j][columns-1],t);
		}

		if(direction == PLAYER_VERT){
			for(int j=0;j<columns;j++)
				g.addEdge(new Edge(k++,MAXFLOW), s,nodes[0][j]);

			for(int j=0;j<columns;j++)
				g.addEdge(new Edge(k++,MAXFLOW),nodes[rows-1][j],t);
		}

	}


	@Override
	public void placePiece(int i, int j, int player) {
		nodes[i][j].setPlayer(player);
		nodes[i][j].setRow(i);
		nodes[i][j].setColum(j);
		Command<Node> command;
		if(player == CONNECT_PLAYER){
			Reduce reduce = new Reduce();
			command = new CommandDoUndo<Node>(reduce.getDoAction(), reduce.getUndoDoAction());
		}else{
			Cut cut = new Cut();
			command = new CommandDoUndo<Node>(cut.getDoAction(), cut.getUndoDoAction());
		}
		commandHistory.push(command);
		command.execute(nodes[i][j]);

	}

	@Override
	public void removePiece(int i, int j) {
		nodes[i][j].setPlayer(EMPTY_CELL);
		Command<Node> last = commandHistory.pop();
		last.undo(nodes[i][j]);	
	}

	public int getDistance(){
		DijkstraShortestPath<Node,Edge> alg = new DijkstraShortestPath<Node,Edge>(g);
		Number distance;
		try{
			distance= alg.getDistance(s, t);
			if(distance == null)	//percorso nn esistente
				return INF;
			else
				return distance.intValue();
		}catch(IllegalArgumentException e){
			e.printStackTrace();
			return INF;
		}			
	}

	private Collection<Node> getNeighbors(Node n){
		Collection<Edge> outEdge = g.getOutEdges(n);
		ArrayList<Node> neighbors = new ArrayList<Node>();
		for(Edge e:outEdge){
			neighbors.add(g.getDest(e));
		}
		return neighbors;
	}

	private boolean isNeighbor(Node n1, Node n2){
		return getNeighbors(n2).contains(n1);

	}

	/*########################################################################
	/*
	 * Oggetti usati per il calcolo del flusso
	 */
	private Transformer<Edge, Integer> edge_capacities =
			new Transformer<Edge, Integer>(){
		public Integer transform(Edge link) {

			return link.getCapacity();
		}
	};

	private Map<Edge, Double> edgeFlowMap= new HashMap<Edge, Double>();;

	// This Factory produces new edges for use by the algorithm
	private Factory<Edge> edgeFactory = new Factory<Edge>() {
		int count = 0;
		public Edge create() {
			return new Edge(count++,0);
		}	
	};
	
	
	/*
	 * ##########################################################################
	 * Calcolo del flusso
	 */
	
	
	@Override
	public int getMaxFlow() {

		EdmondsKarpMaxFlow<Node, Edge> ek = new EdmondsKarpMaxFlow(g, s, t, edge_capacities, edgeFlowMap, 
				edgeFactory);
		ek.evaluate(); // This instructs the class to compute the max flow

		return ek.getMaxFlow();
	}




	@Override
	public boolean isBusy(int i, int j) {
		return nodes[i][j].getPlayer()!=EMPTY_CELL;
	}






	public  void  viewGraph(String name){
		// The Layout<V, E> is parameterized by the vertex and edge types
		Layout<Node, Integer> layout = new CircleLayout(g);
		layout.setSize(new Dimension(600,600)); // sets the initial size of the space
		// The BasicVisualizationServer<V,E> is parameterized by the edge types
		BasicVisualizationServer<Node,Integer> vv =
				new BasicVisualizationServer<Node,Integer>(layout);
		vv.setPreferredSize(new Dimension(750,750)); //Sets the viewing area size

		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);

		JFrame frame = new JFrame(name);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true); 

		System.out.println(g);
	}




}
