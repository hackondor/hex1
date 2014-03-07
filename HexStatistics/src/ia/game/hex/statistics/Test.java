package ia.game.hex.statistics;

import ia.game.hex.algorithms.Player;
import ia.game.hex.gui.model.Game;
import ia.game.hex.heuristics.DistanceFlowHeuristicAlphaBeta;
import ia.game.hex.heuristics.DistanceHeuristic;

import javax.swing.JFrame;

import library.pattern.command.Action;
import library.pattern.command.CommandExec;

public class Test {
	
	private static StatisticsCalculator statistics;

	
	public void start(int numberOfGame){
		
		statistics = new StatisticsCalculator(numberOfGame,"FlowDistanceTest1.txt");
		for(int i=0;i<numberOfGame;i++){
			 Game g = new Game(6,6);
			 g.addPlayer(new DistanceFlowHeuristicAlphaBeta("fd", 4, numberOfGame) );
			 g.addPlayer(new DistanceHeuristic("distance", 4));
			 g.setOnGameFinish(new CommandExec<Player>(new MyAction<Player>()));
			 statistics.init(g.getPlayers());
			 JFrame gui = g.getGui();
			 gui.setVisible(true);
		}
		
		statistics = new StatisticsCalculator(numberOfGame,"FlowDistanceTest2.txt");
		for(int i=0;i<numberOfGame;i++){
			 Game g = new Game(6,6);
			 g.addPlayer(new DistanceHeuristic("distance", 4));
			 g.addPlayer(new DistanceFlowHeuristicAlphaBeta("fd", 4, numberOfGame) );
			 g.setOnGameFinish(new CommandExec<Player>(new MyAction<Player>()));
			 statistics.init(g.getPlayers());
			 JFrame gui = g.getGui();
			 gui.setVisible(true);
		}
		
		
		statistics = new StatisticsCalculator(numberOfGame,"FlowDistanceTest3.txt");
		for(int i=0;i<numberOfGame;i++){
			 Game g = new Game(6,6);
			 g.addPlayer(new DistanceFlowHeuristicAlphaBeta("fd", 3, numberOfGame) );
			 g.addPlayer(new DistanceHeuristic("distance", 4));
			 g.setOnGameFinish(new CommandExec<Player>(new MyAction<Player>()));
			 statistics.init(g.getPlayers());
			 JFrame gui = g.getGui();
			 gui.setVisible(true);
		}
		
		
		statistics = new StatisticsCalculator(numberOfGame,"FlowDistanceTest4.txt");
		for(int i=0;i<numberOfGame;i++){
			 Game g = new Game(6,6);
			 g.addPlayer(new DistanceHeuristic("distance", 4));
			 g.addPlayer(new DistanceFlowHeuristicAlphaBeta("fd", 3, numberOfGame) );
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
