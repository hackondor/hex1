


import ia.game.hex.gui.model.Game;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;





public class Main {
	
	public static void main(String args[]){
		
		
			 Game g = new Game(3,3);
			 JFrame gui = g.getGui();
			 gui.setVisible(true);
			 //g.addPlayer(new Algorithm2("GoodIA"));
			 
			 g.addPlayer(new Algorithm("StupidIA"));g.addPlayer("Player 2");
			

	}
	

}
