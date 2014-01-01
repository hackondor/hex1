

import javax.swing.SwingUtilities;

import view.HexGui;
import controller.HexGuiVm;


public class Main {
	
	public static void main(String args[]){
		
		SwingUtilities.invokeLater(new Runnable() {
		 public void run() {
			HexGuiVm vm = new HexGuiVm();
			HexGui gui = new HexGui(vm);
			gui.setVisible(true);
		 }
		});		
	}
}
