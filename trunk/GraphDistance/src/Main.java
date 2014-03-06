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
		
		int maxFlow = 0;
		try {
			for(int i =0;i<10000;i++){
			DirectedHexGraph g = new DirectedHexGraph(10, 10, 0);
			g.placePiece(0, 0, HexGraph.CONNECT_PLAYER);
			g.placePiece(0, 5, HexGraph.CUT_PLAYER);			
			g.placePiece(1, 0, HexGraph.CONNECT_PLAYER);			
			g.placePiece(3, 8, HexGraph.CUT_PLAYER);
			g.removePiece(3, 8);
			g.removePiece(1, 0);
			g.placePiece(2, 2, HexGraph.CONNECT_PLAYER);
			g.placePiece(4, 3, HexGraph.CONNECT_PLAYER);
			g.placePiece(9, 9, HexGraph.CONNECT_PLAYER);
			g.removePiece(9, 9);
			g.placePiece(8, 9, HexGraph.CUT_PLAYER);
			g.placePiece(1, 1, HexGraph.CONNECT_PLAYER);
			//g.viewGraph("test");
			
			if(g.getMaxFlow()!=maxFlow && i!=0){
				maxFlow = g.getMaxFlow();
				System.out.println("Errore "+ i+":"+maxFlow);
			}else{
				maxFlow = g.getMaxFlow();
			}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public static void main(String[] args) {
		 Main m = new Main();
		m.test();
		//m.test_game();

		 
		
	}

}