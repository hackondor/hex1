

import ia.game.hex.algorithms.Board;

import javax.swing.SwingUtilities;



import view.HexGui;
import controller.HexGuiVm;


public class Main {
	
	public static void main(String args[]){
		
		SwingUtilities.invokeLater(new Runnable() {
		 public void run() {
			 Board b = new Board(10,10);
			HexGuiVm vm = new HexGuiVm(b);
			HexGui gui = new HexGui(vm);
			gui.setVisible(true);
		 }
		});		
	}
}
