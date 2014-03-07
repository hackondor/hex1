package ia.game.hex.heuristics;

public class FlowMineOpponentAlphaBeta extends FlowHeuristicAlphaBeta{

	public FlowMineOpponentAlphaBeta(String name, int profondita, int ratio) {
		super(name, profondita, ratio);
		// TODO Auto-generated constructor stub
	}


	@Override
	public int TestTaglio() {
		int flowO = graphOpponent.getMaxFlow();
		int flowM = graph.getMaxFlow();


		if(flowO == 0){	//ho vinto
			return DirectedHexGraph.INF;
		}
		else if(flowM==0){
			return -DirectedHexGraph.INF;
		}
		else if(profondita == profonditaLimite)
			return -flowO+flowM*ratio;
		return FAILTEST;
	}

	
	
	
}
