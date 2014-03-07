package ia.game.hex.statistics;

import ia.game.hex.algorithms.Player;
import ia.game.hex.gui.model.Game;
import javax.swing.JFrame;
import library.pattern.command.Action;
import library.pattern.command.CommandExec;

public class Test {
	
	private static StatisticsCalculator statistics;

	
	public void start(int numberOfGame){
		
		statistics = new StatisticsCalculator(numberOfGame,"FlowDistanceTest");
		for(int i=0;i<numberOfGame;i++){
			 Game g = new Game(10,10);
			 g.addPlayer(new Algorithm("StupidIA"));
			 g.addPlayer(new ASimplyWin("Sheldon"));
			 g.setOnGameFinish(new CommandExec<Player>(new MyAction<Player>()));
			 statistics.init(g.getPlayers());
			 JFrame gui = g.getGui();
			 gui.setVisible(true);
		}

	}

	
	public class MyAction<T> implements Action<T>{

		@Override
		public boolean run(T p) {
			statistics.calculate((Player) p);
			return true;
		}
		
	}
}
