import ia.game.hex.algorithms.AlgorithmsDefinition;
import ia.game.hex.algorithms.Board;
import ia.game.hex.algorithms.InvalidPlacementException;
import ia.game.hex.algorithms.MultiplePlacementExeption;



public class Algorithm extends AlgorithmsDefinition {

	
	private int i;
	private int j;
	
	public Algorithm() {
		super();
		i = 0;
		j = 0;
		
	}


	@Override
	public void run() {
		try {
			//while(this.isBusy(i++, j++));
			if(j>=5){
				i++;
				j=0;
			}
			placePiece(i,j++);
		} catch (InvalidPlacementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MultiplePlacementExeption e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
