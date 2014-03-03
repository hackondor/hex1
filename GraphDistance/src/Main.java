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

	private MiniMaxDH mini;
	
	public class MyAction<T> implements Action<T>{
		
		@Override
		public boolean run(T p) {
			mini.printGraph();
			return true;
		}
		
	}
	
	public Main(){
		Game g = new Game(3,3);
		 JFrame gui = g.getGui();
		 gui.setVisible(true);
		 mini = new MiniMaxDH("Distance H");
		 g.addPlayer(mini);
		 g.addPlayer("Player 2");
		// g.setOnGameFinish(new CommandExec<Void>(new MyAction<Void>()));
	}
	
	public static void main(String[] args) {
		 Main m = new Main();
//		try {
//			HexGraph g = new HexGraph(2, 2, HexGraph.PLAYER_HOR);
//			//g.viewGraph("test0");
//			g.placePiece(0, 0, HexGraph.CONNECT_PLAYER);
//			//g.viewGraph("test1");
//			
//			g.placePiece(0, 1, HexGraph.CUT_PLAYER);
//			g.viewGraph("test2");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

}
