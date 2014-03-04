import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;









import ia.game.hex.algorithms.Player;
import ia.game.hex.gui.model.Game;

import javax.swing.JFrame;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.flows.EdmondsKarpMaxFlow;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.transformation.DirectionTransformer;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import library.pattern.command.Action;
import library.pattern.command.CommandExec;




public class Main {

	private DistanceHeuristicAlphaBeta dh;
	
	public class MyAction<T> implements Action<T>{
		
		@Override
		public boolean run(T p) {
			dh.printGraph();
			return true;
		}
		
	}
	
	public Main(){

		
		 
		// g.setOnGameFinish(new CommandExec<Void>(new MyAction<Void>()));
	}
	
	void test_game(){
		Game g = new Game(5,5);
		 JFrame gui = g.getGui();
		 gui.setVisible(true);
		 dh = new DistanceHeuristicAlphaBeta("Distance H",1);
		 g.addPlayer(dh);
		 g.addPlayer("Player 2");
	}
	
	
	void test(){
		try {
			HexGraph hg = new HexGraph(1, 2, HexGraph.PLAYER_HOR);
//			hg.placePiece(0, 3,HexGraph.CONNECT_PLAYER);
//			hg.placePiece(2, 2,HexGraph.CONNECT_PLAYER);
//			hg.placePiece(4, 1,HexGraph.CONNECT_PLAYER);
//			hg.placePiece(1, 3,HexGraph.CONNECT_PLAYER);
//			hg.placePiece(3, 1,HexGraph.CONNECT_PLAYER);
			Graph<Node,Integer> g = hg.getGraph(); 
			
			
			
			Transformer<Integer, Double> edge_capacities =
					 new Transformer<Integer, Double>(){
					 public Double transform(Integer link) {
						 return (double)1;
					 }
					 };
			Map<Integer, Double> edgeFlowMap = new HashMap<Integer, Double>();
			
			 // This Factory produces new edges for use by the algorithm
			Factory<Integer> edgeFactory = new Factory<Integer>() {
				int count = 0;
			public Integer create() {
				return count++;
			}
			};
			
			
			Factory<DirectedGraph<Node, Integer>> graphFactory = new Factory<DirectedGraph<Node, Integer>>() {
				
			public DirectedGraph<Node, Integer> create() {
				return new DirectedSparseGraph<Node,Integer>();
			}
			};
			
			

			DirectedGraph<Node,Integer> gdirected1 = (DirectedGraph<Node, Integer>) DirectionTransformer.toDirected(g, graphFactory, edgeFactory, true);
			DirectedGraph<Node,Integer> gdirected = new DirectedSparseGraph<Node,Integer>();
		
			Node n = new Node(1,1) ;
			gdirected.addVertex(n);
			Node n1 = new Node(1,2) ;
			gdirected.addVertex(n1);
			Node n2 = new Node(1,3) ;
			gdirected.addVertex(n2);
			Node n3 = new Node(1,4) ;
			gdirected.addVertex(n3);
			gdirected.addEdge(1,n, n1);
			gdirected.addEdge(2,n1, n2);
			gdirected.addEdge(3,n2, n1);
			gdirected.addEdge(4,n2, n3);
			
			System.out.println("n edge: "+gdirected.getEdges().size());
			 
			
			EdmondsKarpMaxFlow<Node, Integer> ek = new EdmondsKarpMaxFlow(gdirected, n, n3, edge_capacities, edgeFlowMap, 
					 edgeFactory);
					 ek.evaluate(); // This instructs the class to compute the max flow
					 System.out.println(ek.getMaxFlow());
					 for(Map.Entry<Integer,Double> d:edgeFlowMap.entrySet())
						 System.out.println(d.getKey()+" flow:"+d.getValue());
					 
					// The Layout<V, E> is parameterized by the vertex and edge types
						Layout<Node, Integer> layout = new CircleLayout(gdirected);
						layout.setSize(new Dimension(600,600)); // sets the initial size of the space
						// The BasicVisualizationServer<V,E> is parameterized by the edge types
						BasicVisualizationServer<Node,Integer> vv =
								new BasicVisualizationServer<Node,Integer>(layout);
						vv.setPreferredSize(new Dimension(750,750)); //Sets the viewing area size

						vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
						vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
						vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);

						JFrame frame = new JFrame("aho");
						frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						frame.getContentPane().add(vv);
						frame.pack();
						frame.setVisible(true); 

					 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args) {
		 Main m = new Main();
		// m.test_game();
		 m.test();
		 
		
	}

}
