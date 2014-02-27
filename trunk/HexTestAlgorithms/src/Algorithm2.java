import ia.game.hex.algorithms.AlgorithmsDefinition;
import ia.game.hex.algorithms.InvalidPlacementException;
import ia.game.hex.algorithms.MultipleActionExeption;


public class Algorithm2 extends AlgorithmsDefinition {
	private int i,j;
	public Algorithm2(String name) {
		super(name);
		
		i = 0;
		j = 8;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			placePiece(i++, j);
		} catch (InvalidPlacementException | MultipleActionExeption e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
