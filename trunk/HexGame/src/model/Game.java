package model;

import javax.swing.JFrame;

import ia.game.hex.algorithms.AlgorithmsDefinition;
import ia.game.hex.algorithms.Arbiter;
import ia.game.hex.algorithms.Board;
import controller.HexGuiVm;
import view.HexGui;

public class Game {
	
	private HexGui gui;
	private HexGuiVm vm;
	private Board board;
	private Arbiter arbiter;
	
	public Game(){
		board = new Board(Utility.DEFAULT_NUMBERS_OF_ROWS,Utility.DEFAULT_NUMBERS_OF_COLUMNS);
		arbiter = new Arbiter(board);
		vm = new HexGuiVm(board,arbiter);
		gui = new HexGui(vm);
		board.addObserver(vm);
		
	}
	
	public Game(int rows,int columns){
		board = new Board(rows,columns);
		arbiter = new Arbiter(board);
		vm = new HexGuiVm(board,arbiter);
		gui = new HexGui(vm);
		board.addObserver(vm);
		
	}
	
	public JFrame getGui(){
		return gui;
	}
	
	public void addPlayer(){
		arbiter.addPlayer();
	}
	
	public void addPlayer(AlgorithmsDefinition a){
		a.setBoard(board);
		arbiter.addPlayer(a);
	}
	
	//Pattern State
	public abstract class GameMode{ protected abstract void initialize();}
	//first state
	public class HumanVsIAGame extends GameMode{

		@Override
		protected void initialize() {
			// TODO Auto-generated method stub
			
		}}
	//second state
	public class IAVsIAGame extends GameMode{

		@Override
		protected void initialize() {
			// TODO Auto-generated method stub
			
		}}

	public GameMode HumanVsIAGame = new HumanVsIAGame();
	public GameMode IAVsIAGame = new IAVsIAGame();
	public GameMode currentMode = HumanVsIAGame; //Default
	public void setMode(GameMode g){
		currentMode = g;
		g.initialize();
	}

}
