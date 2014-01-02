

import ia.game.hex.algorithms.Board;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;





import model.Game;
import view.HexGui;
import controller.HexGuiVm;


public class Main {
	
	public static void main(String args[]){
		
	//	SwingUtilities.invokeLater(new Runnable() {
	//	 public void run() {
			 Game g = new Game();
			 JFrame gui = g.getGui();
			 g.addPlayer();
			 g.addPlayer(new Algorithm());
			 gui.setVisible(true);
			 
	//	 }
	//	});		
	}
}
