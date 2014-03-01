package ia.game.hex.gui.model;

import java.util.ArrayList;


import javax.swing.JFrame;
import library.pattern.command.Action;
import library.pattern.command.Command;
import ia.game.hex.algorithms.AlgorithmsDefinition;
import ia.game.hex.algorithms.Arbiter;
import ia.game.hex.algorithms.Board;
import ia.game.hex.algorithms.GameListener;
import ia.game.hex.algorithms.Player;
import ia.game.hex.gui.controller.HexGuiVm;
import ia.game.hex.gui.view.HexGui;

public class Game {
	
	private HexGui gui;
	private HexGuiVm vm;
	private Board board;
	private Arbiter arbiter;
	private Command onFinishCommand;
	
	public Game(){
		this(Utility.DEFAULT_NUMBERS_OF_ROWS,Utility.DEFAULT_NUMBERS_OF_COLUMNS);
	}
	
	public Game(int rows,int columns){
		board = new Board(rows,columns);
		arbiter = new Arbiter(board);
		arbiter.addFinishGameListener(new FinishActionListener());
		vm = new HexGuiVm(board,arbiter);
		gui = new HexGui(vm);
		arbiter.addFinishGameListener(gui);
		board.addObserver(vm);
		
	}
	
	public JFrame getGui(){
		return gui;
	}
	
	/**
	 * add a human player
	 */
	public void addPlayer(String name){
		arbiter.addPlayer(name);
	}
	
	/**
	 * add a IA player. 
	 * @param a: IA player
	 */
	public void addPlayer(AlgorithmsDefinition a){
		a.setBoard(board);
		arbiter.addPlayer(a);
		
	}
	
	public ArrayList<Player> getPlayers(){
		return arbiter.getPlayers();
	}
	
	/**
	 * 
	 */
	public void setOnGameFinish(Command c){
		onFinishCommand = c;
	}


	public class FinishActionListener implements GameListener{

		public void update(Player args) {
			//args: the player that has win
			if(onFinishCommand!=null)
				onFinishCommand.execute(args);	
		}
		
	}


}
