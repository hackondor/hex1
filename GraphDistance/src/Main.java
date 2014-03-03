import ia.game.hex.gui.model.Game;

import javax.swing.JFrame;




public class Main {

	public static void main(String[] args) {
		Game g = new Game(3,3);
		 JFrame gui = g.getGui();
		 gui.setVisible(true);
		 g.addPlayer(new MiniMaxDH("Distance H"));g.addPlayer("Player 2");
		
	}

}
