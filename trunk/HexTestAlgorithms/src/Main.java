


import javax.swing.JFrame;
import javax.swing.SwingUtilities;







import model.Game;



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
