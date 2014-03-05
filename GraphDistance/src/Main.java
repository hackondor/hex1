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
	private FlowHeuristicAlphaBeta fh;
	
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
		Game g = new Game(2,2);
		 JFrame gui = g.getGui();
		 gui.setVisible(true);
		 //dh = new DistanceHeuristicAlphaBeta("Distance H",20);
		 fh = new FlowHeuristicAlphaBeta("Flow H",20);
		 g.addPlayer(fh);
		 g.addPlayer("Player 2");
	}
	
	public void test(){
		try {
			DirectedHexGraph g = new DirectedHexGraph(2, 2, 0);
			g.placePiece(0, 0, HexGraph.CONNECT_PLAYER);
			g.placePiece(0, 1, HexGraph.CUT_PLAYER);			
			g.placePiece(1, 0, HexGraph.CONNECT_PLAYER);			
			g.placePiece(1, 1, HexGraph.CUT_PLAYER);
			System.out.println("11--> "+g.getMaxFlow());
			g.removePiece(1, 1);
			g.removePiece(1, 0);
			g.viewGraph("test");
//			g.placePiece(1, 1,HexGraph.CONNECT_PLAYER );
//			g.placePiece(1, 0,HexGraph.CUT_PLAYER );
//			
//			System.out.println("10--> "+g.getMaxFlow());
//			g.removePiece(1, 0);
//			g.removePiece(1, 1);
//			g.removePiece(0, 1);
//			g.placePiece(1, 0, HexGraph.CONNECT_PLAYER);
//			g.placePiece(0, 1,HexGraph.CUT_PLAYER );
//			g.placePiece(1, 1,HexGraph.CONNECT_PLAYER );
//			System.out.println("11--> "+g.getMaxFlow());
//			g.removePiece(1, 1);
//			g.removePiece(0, 1);
//			g.placePiece(1, 1,HexGraph.CONNECT_PLAYER );
//			g.placePiece(0, 1,HexGraph.CUT_PLAYER );
//			System.out.println("01--> "+g.getMaxFlow());
//			
//			
//			System.out.println(g.getMaxFlow());
//			g.viewGraph("test");
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
