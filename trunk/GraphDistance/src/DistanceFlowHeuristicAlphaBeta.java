
public class DistanceFlowHeuristicAlphaBeta extends FlowHeuristicAlphaBeta {

	public DistanceFlowHeuristicAlphaBeta(String name, int profondita, int ratio) {
		super(name, profondita, ratio);
	}

	@Override
	public int TestTaglio() {
		//int flowM = graph.getMaxFlow();
		int flowO = graphOpponent.getMaxFlow();
		int distanceO = graphOpponent.getDistance();
		int distance = graph.getDistance();


		if(flowO == 0){	//ho vinto
			return DirectedHexGraph.INF;
		}
		else if(distanceO==1){
			return -DirectedHexGraph.INF;
		}
		else if(profondita == profonditaLimite)
			return -flowO+(distanceO+distance)*ratio;
		return FAILTEST;
	}
	
	

	
}
