package ia.game.hex.gui.controller;



import ia.game.hex.gui.model.Game;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;





public class Test {
	
	public static void main(String args[]){
		
		
			 Game g = new Game(11,11);
			 JFrame gui = g.getGui();
			 gui.setVisible(true);
			 //g.addPlayer(new Algorithm2("GoodIA"));
			 
			 g.addPlayer("Stupid");g.addPlayer("Player 2");
			

	}
	

}
