package ia.game.hex.algorithms;

import java.awt.Color;

/**
 * Informazioni del giocatore di HEX
 * @author Nich
 *
 */
public class Player{
	private AlgorithmsDefinition algorithm;
	private Color player_color;
	private boolean IA;
	private String name;


	public Player(String name,Color c){
		player_color = c;
		IA = false;
		algorithm = null;
		this.name = name;
	}

	public Player(String name,AlgorithmsDefinition a,Color c){
		player_color = c;
		algorithm = a;
		IA = true;
		this.name = name;
	}

	public AlgorithmsDefinition getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(AlgorithmsDefinition algorithm) {
		IA = true;
		this.algorithm = algorithm;
	}

	public Color getPlayerColor() {
		return player_color;
	}

	public void setPlayerColor(Color player_color) {
		this.player_color = player_color;
	}

	public boolean isIA(){
		return IA;
	}
	
	public String getName(){
		return name;
	}
}
	