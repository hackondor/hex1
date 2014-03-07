package ia.game.hex.statistics;

import ia.game.hex.algorithms.AlgorithmsDefinition;
import ia.game.hex.algorithms.InvalidPlacementException;
import ia.game.hex.algorithms.MultipleActionExeption;


public class ASimplyWin extends AlgorithmsDefinition {

	
	private int i = 0;
	private int j = 7;
	
	public ASimplyWin(String name) {
		super(name);
		
	}

	@Override
	public void run() {
		try {
			this.placePiece(i++, j);
		} catch (InvalidPlacementException | MultipleActionExeption e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
