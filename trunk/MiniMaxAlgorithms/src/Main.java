import ia.game.hex.gui.model.Game;


public class Main {
	public static void main(String[] args){
		Game g = new Game(3,3);
		g.getGui().setVisible(true);
		//g.addPlayer("Giggino1");
		
		//g.addPlayer(new MiniMaxAlgorithm("MiniMax2"));
		g.addPlayer(new IntelligentMiniMaxAlgorithm("MiniMax"));
		g.addPlayer("Giggino2");
		
	
		
	}
}
