package ia.game.hex.heuristics;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import ia.game.hex.algorithms.Player;
import ia.game.hex.gui.model.Game;

import javax.swing.JFrame;
import javax.swing.text.DefaultEditorKit.CutAction;

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
	private DistanceFlowHeuristicAlphaBeta fh;
	private FlowLocalHeuristicAlphaBeta fl;
	private ShannonStrategy ss;
	
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
		Game g = new Game(6,6);
		 JFrame gui = g.getGui();
		 gui.setVisible(true);
		 //dh = new DistanceHeuristicAlphaBeta("Distance H",1);
		 //fh = new DistanceFlowHeuristicAlphaBeta("Flow H",4,30);
		 //fl = new FlowLocalHeuristicAlphaBeta("Local", 4, 100);
		 ss = new ShannonStrategy("ss");
		 
		 
		 g.addPlayer("b");
		 g.addPlayer(ss);
		 g.start();
		
	}
	
	public void test(){
		boolean diverso=false;
		int maxFlow = 0;
		try {
			for(int i =0;i<1000;i++){
			DirectedHexGraph g = new DirectedHexGraph(10, 10, 0);
			g.placePiece(0, 0, HexGraph.CONNECT_PLAYER);
			g.placePiece(0, 5, HexGraph.CUT_PLAYER);
			g.placePiece(0,9, HexGraph.CONNECT_PLAYER);
			g.placePiece(8, 4, HexGraph.CONNECT_PLAYER);				
			g.placePiece(3, 8, HexGraph.CUT_PLAYER);
			g.removePiece(3, 8);
			g.removePiece(8, 4);
			g.placePiece(2, 2, HexGraph.CUT_PLAYER);
			g.placePiece(1, 1, HexGraph.CONNECT_PLAYER);
			g.placePiece(9, 9, HexGraph.CONNECT_PLAYER);
			g.removePiece(9, 9);
			g.placePiece(8, 9, HexGraph.CUT_PLAYER);
			g.placePiece(5,3, HexGraph.CONNECT_PLAYER);
	//		g.viewGraph("test "+i);
			
		
			if(g.getMaxFlow()!=maxFlow && i!=0){
				maxFlow = g.getMaxFlow();
				System.out.println("Errore "+ i+":"+maxFlow);
			}else{
				
				maxFlow = g.getMaxFlow();
				System.out.println("ok "+ i+":"+maxFlow);

			}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	
	public static void main(String[] args) {
		 Main m = new Main();
		//m.test();
		m.test_game();

	}

}
