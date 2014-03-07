package ia.game.hex.statistics;
import ia.game.hex.algorithms.AlgorithmsDefinition;
import ia.game.hex.algorithms.Board;
import ia.game.hex.algorithms.InvalidPlacementException;
import ia.game.hex.algorithms.MultipleActionExeption;




public class Algorithm extends AlgorithmsDefinition {

	
	private int i;
	private int j;
	
	public Algorithm(String name) {
		super(name);
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
		} catch (MultipleActionExeption e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
