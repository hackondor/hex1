package ia.game.hex.gui.controller;

import java.awt.Color;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import library.pattern.command.Action;
import library.pattern.command.Command;
import library.pattern.command.CommandExec;
import ia.game.hex.algorithms.*;
import ia.game.hex.gui.model.Utility;


public class HexGuiVm implements Observer {

	private Board board;
	private int side;
	private double apotema;
	private int number_rows = Utility.DEFAULT_NUMBERS_OF_ROWS;
	private int number_columns = Utility.DEFAULT_NUMBERS_OF_COLUMNS ;
	private Cell[][]  cells;	   			//list of binding object
	private SelectedCell selected_cell;		//binding objects
	private Command<Object> on_selected_command;	//command
	private Arbiter arbiter;

	public HexGuiVm(Board b,Arbiter a){
		board = b;
		number_rows = board.GetRowsNumber();
		number_columns = board.GetColumnsNumber();
		side = Utility.DEFAULT_SIDE;
		apotema = side*0.866025;	
		selected_cell = null;
		arbiter = a;
		on_selected_command = new CommandExec<Object>(new OnSelectedAction<Object>());
		_createCells();
	}

	public int getNumbersOfRows(){
		return number_rows;
	}

	public int getNumbersOfColumn(){
		return number_columns;
	}

	public void changeBoard(Board board){
		this.board = board;
	}

	/**
	 * Crea le celle del gioco
	 */
	private void _createCells(){

		//costruzione degli esagoni: Cell
		Cell c;
		cells = new Cell[number_rows][number_columns];
		GeneralPath shape;
		Point2D[] points = null;
		float xc = Utility.XCENTER ,yc = Utility.YCENTER;
		int i = 0,j = 0;
		for(i=0;i<board.GetRowsNumber();i++){
			for(j=0;j<board.GetColumnsNumber();j++) {
				c = new Cell();
				c.SetCenter(new Point2D.Double(xc,yc));
				points = new Point2D[6];
				points[0] = new Point2D.Double(xc-side/2,yc-apotema); 
				points[1] = new Point2D.Double(xc+side/2,yc-apotema); 
				points[2] = new Point2D.Double(xc+side,yc); 
				points[3] = new Point2D.Double(xc+side/2,yc+apotema);
				points[4] = new Point2D.Double(xc-side/2,yc+apotema); 
				points[5] = new Point2D.Double(xc-side,yc); 
				c.SetVertices(points);
				_hexagonRotation(c);
				cells[j][i] = c;

				yc = (float) (yc +2*apotema + Utility.MARGIN);
			}	
			xc = (float) (xc + 1.5*side + Utility.MARGIN);
			yc = (float)(Utility.YCENTER + (i+1)*apotema + Utility.MARGIN);
		}
		
		/*
		 * Spostiamo i vertici degli esagoni in modo da eguagliare le distanze
		 * dai bordi. La scacchiera viene centrata nel frame.
		 */
		Point2D a =cells[0][0].GetCenter();
		Point2D b=cells[getNumbersOfRows()-1][getNumbersOfColumn()-1].GetCenter();
		int l_x=Utility.DEFAULT_GUI_WIDTH;
		int l_y=Utility.PLAYER_NAME_RECT_HEIGHT;
		double dx=l_x-b.getX()-a.getX();
		dx=dx/2;
		double dy=l_y-b.getY()-a.getY();
		dy=dy/2;
				
		for(i=0;i<board.GetRowsNumber();i++){
			for(j=0;j<board.GetColumnsNumber();j++) {
				Point2D[] vertices = new Point2D.Double[6];
				for(int k =0; k<6;k++){
					double x =cells[i][j].GetVertices()[k].getX();
					double y =cells[i][j].GetVertices()[k].getY();

					vertices[k]=new Point2D.Double(x+dx,y+dy);

				}
				
				cells[i][j].SetVertices(vertices);
				Point2D cent = cells[i][j].GetCenter();
				cells[i][j].SetCenter(new Point2D.Double(cent.getX()+dx,cent.getY()+dy));
				shape = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 6);
				shape.moveTo(vertices[0].getX(), vertices[0].getY());
				for (int m = 1; m < 6; m++)
					shape.lineTo(vertices[m].getX(), vertices[m].getY());
				shape.closePath();
				cells[i][j].setShape(shape);
			}
		}
		
	}



	private void _hexagonRotation(Cell c){
		double x,y;
		Point2D[] points = c.GetVertices(); 
		Point2D center = c.GetCenter();

		x = center.getX()*Math.cos(Utility.ANGLE) - center.getY()*Math.sin(Utility.ANGLE);
		y = center.getX()*Math.sin(Utility.ANGLE) + center.getY()*Math.cos(Utility.ANGLE);
		center.setLocation(x, y);
		for(int i = 0;i <6;i++){
			x = points[i].getX()*Math.cos(Utility.ANGLE) - points[i].getY()*Math.sin(Utility.ANGLE);
			y = points[i].getX()*Math.sin(Utility.ANGLE) + points[i].getY()*Math.cos(Utility.ANGLE);
			points[i].setLocation(x, y);
		}
	}

	public Point2D pointRotation(Point2D p,float angle){
		double x = p.getX()*Math.cos(angle) - p.getY()*Math.sin(angle);
		double y = p.getX()*Math.sin(angle) + p.getY()*Math.cos(angle);
		return new Point2D.Double(x,y);
	} 
	public Point2D pointRotation(Point2D p){
		return pointRotation(p,Utility.ANGLE);
	} 

	/**
	 * Binding method to SelectedCell
	 */
	public void setSelectedCell(SelectedCell cell){
		selected_cell = cell;
	}


	/**
	 * Binding method
	 * @return polygons. Gli esagoni da disegnare
	 */
	public Cell[][] getShapes(){
		return cells;
	} 

	/**
	 * Command
	 * @return
	 */
	public Command<Object> OnSelectedCommand(){
		return on_selected_command;
	}

	private class OnSelectedAction<Object> implements Action<Object>{

		@Override
		public boolean run(Object o) {
			boolean find = false;
			if(arbiter.isTurnOfHumanPlayer()){
				int i = 0, j= 0;
				for(i = 0; i<number_rows && !find;i++){
					for(j = 0; j<number_columns && !find;j++){
						find = cells[i][j].getShape().contains(selected_cell.getSelectedX(),selected_cell.getSelectedY());
					}
				}
				i--;j--;
				System.out.println("Selected cell:"+i+" "+j);//test
				//System.out.println(find);	//test
				if(find){
					if(board.isBusy(i, j) && board.isStealLegal()) {
						board.stealPiece(i, j, arbiter.getCurrentPlayer());
						arbiter.nextStep();
					}
					else if(board.placePiece(i, j,arbiter.getCurrentPlayer())){
						//cells[i][j].setColor(arbiter.getCurrentPlayerColor());
////						System.out.println(i+" "+j);
//						System.out.println("Player: " + arbiter.getCurrentPlayer());
						arbiter.nextStep();
					}
					else
						System.out.println("Mossa non consentita");

					
				}
			}

			return find;
		}

	}


	//when the state of board change
	@Override
	public void update(Observable o, Object arg) {
		BoardEvent event = (BoardEvent)arg; 
//		System.out.println("UPDATE VM. Color " + arbiter.getCurrentPlayerColor());  //test
		cells[event.getX()][event.getY()].setColor(arbiter.getCurrentPlayerColor());

	}

	ArrayList<PlayerInfo> players = null;
	public ArrayList<PlayerInfo> getPlayersInfo(){
		ArrayList<PlayerInfo> players = new ArrayList<PlayerInfo>();
		for(Player p:arbiter.getPlayers()){
			players.add(new PlayerInfo(p.getPlayerColor(),p.getName()));
		}
		return players;
	}

	/**
	 * This class contains basic info of a player
	 * @author Nich
	 *
	 */
	public class PlayerInfo{
		private Color color;
		private String name;

		public PlayerInfo(Color color,String name){
			this.color = color;
			this.name = name;
		}

		public Color getColor() {
			return color;
		}

		public void setColor(Color color) {
			this.color = color;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}



	}










}
