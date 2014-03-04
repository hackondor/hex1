import java.awt.Dimension;

import ia.game.hex.algorithms.Node;
import ia.game.hex.algorithms.Player;
import ia.game.hex.gui.model.Game;

import javax.swing.JFrame;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import library.pattern.command.Action;
import library.pattern.command.CommandExec;




public class Main {

	private DistanceHeuristic dh;
	
	public class MyAction<T> implements Action<T>{
		
		@Override
		public boolean run(T p) {
			dh.printGraph();
			return true;
		}
		
	}
	
	public Main(){
		Game g = new Game(3,3);
		 JFrame gui = g.getGui();
		 gui.setVisible(true);
		 dh = new DistanceHeuristic("Distance H",2);
		 g.addPlayer(dh);
		 g.addPlayer("Player 2");
		
		 
		// g.setOnGameFinish(new CommandExec<Void>(new MyAction<Void>()));
	}
	
	public static void main(String[] args) {
		 Main m = new Main();
//		try {
//			HexGraph g = new HexGraph(2, 2, HexGraph.PLAYER_HOR);
//			
//			g.placePiece(0, 0, HexGraph.CONNECT_PLAYER);
//		//	g.viewGraph("MAX");
//			
//			g.placePiece(0, 1, HexGraph.CUT_PLAYER);
//		//	g.viewGraph("MIN");
//	
//			g.placePiece(1, 0, HexGraph.CONNECT_PLAYER);
//			g.viewGraph("MAX");
//
//			g.placePiece(1, 1, HexGraph.CUT_PLAYER);
//			g.viewGraph("MIN");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

}
