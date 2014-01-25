package ia.game.hex.gui.model;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import library.pattern.command.Action;
import library.pattern.command.Command;
import ia.game.hex.algorithms.AlgorithmsDefinition;
import ia.game.hex.algorithms.Arbiter;
import ia.game.hex.algorithms.Board;
import ia.game.hex.algorithms.Player;
import ia.game.hex.gui.controller.HexGuiVm;
import ia.game.hex.gui.view.HexGui;

public class Game implements Observer {
	
	private HexGui gui;
	private HexGuiVm vm;
	private Board board;
	private Arbiter arbiter;
	private Command onFinishCommand;
	
	public Game(){
		board = new Board(Utility.DEFAULT_NUMBERS_OF_ROWS,Utility.DEFAULT_NUMBERS_OF_COLUMNS);
		arbiter = new Arbiter(board);
		arbiter.addObserver(this);
		vm = new HexGuiVm(board,arbiter);
		gui = new HexGui(vm);
		board.addObserver(vm);
		
	}
	
	public Game(int rows,int columns){
		board = new Board(rows,columns);
		arbiter = new Arbiter(board);
		arbiter.addObserver(this);
		vm = new HexGuiVm(board,arbiter);
		gui = new HexGui(vm);
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

	@Override
	public void update(Observable arg0, Object arg1) {
		//arg1: the player that has win
		onFinishCommand.execute(arg1);	
	}


}
