


import ia.game.hex.gui.model.Game;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;





public class Main {
	
	public static void main(String args[]){
		
		
			 Game g = new Game();
			 JFrame gui = g.getGui();
			 g.addPlayer(new Algorithm("StupidIA")); g.addPlayer("Nich");
			 gui.setVisible(true);

	}
	

}