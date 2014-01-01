package controller;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import view.command.Action;
import view.command.Command;
import view.command.CommandExec;
import model.Arbiter;
import model.Utility;
import ia.game.hex.algorithms.*;


public class HexGuiVm implements Observer {

	private Board board;
	private int side;
	private double apotema;
	private int number_rows = Utility.DEFAULT_NUMBERS_OF_ROWS;
	private int number_columns = Utility.DEFAULT_NUMBERS_OF_COLUMNS ;
	private Cell[][]  cells;	   			//list of binding object
	private SelectedCell selected_cell;		//binding objects
	private Command on_selected_command;	//command
	private Arbiter arbiter;
	
	public HexGuiVm(Board b){
		board = b;
		side = Utility.DEFAULT_SIDE;
		apotema = side*0.866025;	
		selected_cell = null;
		arbiter = new Arbiter();
		on_selected_command = new CommandExec(new OnSelectedAction());
		_createCells();
	}
	
	public int getNumbersOfRows(){
		return number_rows;
	}
	
	public int getNumbersOfColumn(){
		return number_columns;
	}

	public void changeBoardSize(int rows,int columns){
		board = new Board(rows,columns);
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
				shape = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 6);
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
				
				//creazione della shape
				shape.moveTo(points[0].getX(), points[0].getY());
				for (int m = 1; m < 6; m++)
					shape.lineTo(points[m].getX(), points[m].getY());
				shape.closePath();
				c.setShape(shape);
				
				cells[i][j] = c;
				
				yc = (float) (yc +2*apotema + Utility.MARGIN);
			}	
		xc = (float) (xc + 1.5*side + Utility.MARGIN);
		yc = (float)(Utility.YCENTER + (i+1)*apotema + Utility.MARGIN);
		}
	}
	
	private void _hexagonRotation(Cell c){
		double x,y;
		Point2D[] points = c.GetVertices(); 
		Point2D center = c.GetCenter();
		
		x = center.getX()*Math.cos(Utility.ANGLE) - center.getY()*Math.sin(Utility.ANGLE);
		y = center.getX()*Math.sin(Utility.ANGLE) + center.getY()*Math.cos(Utility.ANGLE);
		for(int i = 0;i <6;i++){
		x = points[i].getX()*Math.cos(Utility.ANGLE) - points[i].getY()*Math.sin(Utility.ANGLE);
		y = points[i].getX()*Math.sin(Utility.ANGLE) + points[i].getY()*Math.cos(Utility.ANGLE);
		points[i].setLocation(x, y);
		}
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
	public Command OnSelectedCommand(){
		return on_selected_command;
	}
	
	private class OnSelectedAction implements Action{

		@Override
		public boolean run() {
			boolean find = false;
			int i = 0, j= 0;
			for(i = 0; i<number_rows && !find;i++){
				for(j = 0; j<number_columns && !find;j++){
					find = cells[i][j].getShape().contains(selected_cell.getSelectedX(),selected_cell.getSelectedY());
					System.out.println(find);	//test
				}
			}
			i--;j--;
			System.out.println(find);	//test
			if(find){	
				if(board.movePiece(i, j,arbiter.getCurrentPlayer())){
					cells[i-1][j-1].setColor(arbiter.getCurrentPlayerColor());
					arbiter.nextStep();
					
				}
			}
			return find;
		}
		
	}

	
	//when the state of board change
	@Override
	public void update(Observable o, Object arg) {
		BoardEvent event = (BoardEvent)arg; 
		cells[event.getX()][event.getY()].setColor(arbiter.getCurrentPlayerColor());
		arbiter.nextStep();
	}
	
	
	
	
	
	
	
	
	

}
