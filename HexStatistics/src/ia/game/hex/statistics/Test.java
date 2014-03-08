package ia.game.hex.statistics;

import ia.game.hex.algorithms.Player;
import ia.game.hex.gui.model.Game;
import ia.game.hex.heuristics.DistanceFlowHeuristicAlphaBeta;
import ia.game.hex.heuristics.DistanceHeuristic;
import ia.game.hex.heuristics.DistanceHeuristicAlphaBeta;

import javax.swing.JFrame;

import library.pattern.command.Action;
import library.pattern.command.CommandExec;

public class Test {

	private static StatisticsCalculator statistics;
	private int numberOfGame = 0;
	private int count = 0;

	public void start(int numberOfGame){
		this.numberOfGame = numberOfGame;
		statistics = new StatisticsCalculator(numberOfGame,"FlowDistanceTest1.txt");
		_start(1);
	}

	private void _start(int test){
		if(test==1){
			
			Game g = new Game(6,6);
			g.setOnGameFinish(new CommandExec<Player>(new MyAction<Player>()));
			g.addPlayer(new DistanceFlowHeuristicAlphaBeta("fd", 4, count) );
			g.addPlayer(new DistanceHeuristicAlphaBeta("distance", 4));
			statistics.init(g.getPlayers());
			g.start();
			g.getGui().setVisible(false);
		}else if(test==2){
			
			Game g = new Game(6,6);
			g.addPlayer(new DistanceHeuristicAlphaBeta("distance", 4));
			g.addPlayer(new DistanceFlowHeuristicAlphaBeta("fd", 4, count) );
			g.setOnGameFinish(new CommandExec<Player>(new MyAction<Player>()));
			statistics.init(g.getPlayers());
			g.start();
			g.getGui().setVisible(false);

		}else if(test==3){
	
			Game g = new Game(6,6);
			g.addPlayer(new DistanceFlowHeuristicAlphaBeta("fd", 3, count) );
			g.addPlayer(new DistanceHeuristicAlphaBeta("distance", 4));
			g.setOnGameFinish(new CommandExec<Player>(new MyAction<Player>()));
			statistics.init(g.getPlayers());
			g.start();
			g.getGui().setVisible(false);
		}else{
			
			Game g = new Game(6,6);
			g.addPlayer(new DistanceHeuristicAlphaBeta("distance", 4));
			g.addPlayer(new DistanceFlowHeuristicAlphaBeta("fd", 3, count) );
			g.setOnGameFinish(new CommandExec<Player>(new MyAction<Player>()));
			statistics.init(g.getPlayers());
			g.start();
			g.getGui().setVisible(false);

		}
	}

	private int test = 1;

	public class MyAction<T> implements Action<T>{

		@Override
		public boolean run(T p) {
			count++;
			statistics.calculate((Player) p);
			if(count == numberOfGame){	
				count = 0;
				test++;
				if(test<=4)
					statistics = new StatisticsCalculator(numberOfGame,"FlowDistanceTest"+test+".txt");
			}
			if(test<=4)
				_start(test);
			

			return true;
		}

	}
}
