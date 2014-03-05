package ia.game.hex.gui.view;

import ia.game.hex.algorithms.Costant;
import ia.game.hex.algorithms.GameListener;
import ia.game.hex.algorithms.Player;
import ia.game.hex.gui.controller.Cell;
import ia.game.hex.gui.controller.HexGuiVm;
import ia.game.hex.gui.controller.HexGuiVm.PlayerInfo;
import ia.game.hex.gui.controller.SelectedCell;
import ia.game.hex.gui.model.Utility;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;



@SuppressWarnings("serial")
public class HexGui extends JFrame implements Observer,GameListener  {

	private HexGuiVm vm;					//view model of the gui
	private Cell[][] polygons;       		//board
	private JPanel mainpanel;
	private SelectedCell selected_cell;
	private BufferedImage buffer;
	private boolean isWin = false;			//true if a player win
	private Player playerWin = null;		//the player that has win
	private Font font = null;
	private ArrayList<PlayerInfo> players;	//binding objects

	public HexGui(HexGuiVm vm){
		this.vm=vm;
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				init();
			};
		});
	}

	private void init(){

		// set LookAndFeel
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//

		players = vm.getPlayersInfo();
		Utility.createFont();
		setSize(Utility.DEFAULT_GUI_WIDTH,Utility.DEFAULT_GUI_HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainpanel = new HexPanel();
		mainpanel.setSize(this.getSize());
		mainpanel.addMouseListener(new ClickListener());
		mainpanel.setBackground(Color.RED);
		add(mainpanel);
		polygons = vm.getShapes(); //binding method
		for(Cell[] crow:polygons)
			for(Cell c:crow)
				c.addObserver(this);
		selected_cell = new SelectedCell();//binding object
		vm.setSelectedCell(selected_cell);
		buffer = new BufferedImage(mainpanel.getWidth(),mainpanel.getHeight(),BufferedImage.TYPE_INT_RGB);


		// Creates a menubar for a JFrame
		JMenuBar menuBar = new JMenuBar();
		// Add the menubar to the frame
		setJMenuBar(menuBar);

		JMenu newMenu = new JMenu("New Game");
		JMenu editMenu = new JMenu("Edit");
		menuBar.add(newMenu);
		menuBar.add(editMenu);
		//newMenu
		JRadioButtonMenuItem radioAction1 = new JRadioButtonMenuItem(
				"Human VS IA");
		JRadioButtonMenuItem radioAction2 = new JRadioButtonMenuItem(
				"IA VS IA");
		// Create a ButtonGroup and add both radio Button to it. Only one radio
		// button in a ButtonGroup can be selected at a time.
		ButtonGroup bg = new ButtonGroup();
		bg.add(radioAction1);
		bg.add(radioAction2);
		bg.setSelected(radioAction1.getModel(), true);
		newMenu.add(radioAction1);
		newMenu.add(radioAction2);
		newMenu.getPopupMenu().setLightWeightPopupEnabled(false);
		editMenu.getPopupMenu().setLightWeightPopupEnabled(false);
		radioAction1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Human VS IA selected");
				//restart game
				//use a Vm method. Vm use a setMode method of Game class. Delegate(observer) is an idea
				repaint();
			}
		});

		radioAction2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("IA VS IA selected");
				//restart game
				//use a Vm method. Vm use a setMode method of Game class. Delegate is an idea
				repaint();
			}
		});

		//editMenu
		JMenuItem configurationAction = new JMenuItem("IA configuration");
		editMenu.add(configurationAction);

		configurationAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("IA configuration");
				//open a pop up which contains algorithms of IA players.
				//IA players must are added in a game class
			}
		});
	}

	public void setVM(HexGuiVm vm) {
		this.vm=vm;
	}


/* Paint non serve più. Faccio il repaint solo del pannello */
//	public void paint(Graphics gg){
//
//
//		Graphics gb = buffer.getGraphics();
//		Graphics2D gb2 = (Graphics2D)gb;
//
//		for(Cell[] prow:polygons)
//			for(Cell c:prow){
//				gb2.setColor(c.getColor());
//				gb2.fill(c.getShape());
//			}
//		_drawLines(gb2);
//		if(isWin)
//			_drawPlayerWinString(gb2);
//		_drawPlayersName(gb2);
//
//		super.paint(gg);
//		mainpanel.getGraphics().drawImage(buffer, 0, 0, this);
//
//	}

	class ClickListener implements MouseListener {

		int x;
		int y;

		@Override
		public void mouseClicked(MouseEvent arg0) {

		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			x = arg0.getX();
			y = arg0.getY();
			System.out.println(x+";"+y); //test
			selected_cell.Selected(x,y);
			vm.OnSelectedCommand().execute(null);
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			

		}
	}


	@Override
	public void update(Observable arg0, Object arg1) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				repaint();
			}
		});

	}


	//when Player args win 
	@Override
	public void update(Player args) {
		isWin = true;
		playerWin = args;
		repaint();
	}

	private void _drawPlayerWinString(Graphics2D g){

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(new Color(0,255,0,200));
		g.fillRect(0, 50, 700, 200);
		Font font = Utility.getFont(100);    
		g.setStroke(new BasicStroke(2.0f)); // 2-pixel lines
		g.setFont(font);
		g.setColor(playerWin.getPlayerColor());
		g.drawString(playerWin.getName()+" win! ", 50, 200);

	}


	private Point2D intersect(Retta r1, Retta r2){
		double x=(r2.getN()-r1.getN())/(r1.getM()-r2.getM());
		Point2D p=new Point2D.Double(x,r1.y(x));
		return p;
	}
	private class Retta{
		double m,n;
		public double getM() {
			return m;
		}
		public void setM(double m) {
			this.m = m;
		}
		public double getN() {
			return n;
		}
		public void setN(double n) {
			this.n = n;
		}
		public Retta(double m, double n) {
			super();
			this.m = m;
			this.n = n;
		}
		public Retta(double x1, double x2, double y1, double y2) {
			super();
			m=(y2-y1)/(x2-x1);
			n = y1-x1*m;
		}
		public double y(double x){
			return x*m+n;
		}
	
		
	}
	//draw the lines that sorround the board
	private void _drawLines(Graphics2D g){
		double a = 5;
		int dy=0;
		int l = Utility.DEFAULT_SIDE;
		int r=vm.getNumbersOfRows()-1;
		int c=vm.getNumbersOfColumn()-1;
		double sin60=Math.sqrt(3)/2;
		double cos60=0.5;
		double y_1 = polygons[0][0].GetVertices()[1].getY()-a;
		double x_1 = polygons[0][0].GetVertices()[1].getX();
		double y_2 = polygons[0][c].GetVertices()[1].getY()-a;
		double x_2 = polygons[0][c].GetVertices()[1].getX();
		Retta retta1 = new Retta(x_1,x_2,y_1,y_2);
		
		y_1 = polygons[0][0].GetVertices()[5].getY();
		x_1 = polygons[0][0].GetVertices()[5].getX()-a;
		y_2 = polygons[r][0].GetVertices()[5].getY();
		x_2 = polygons[r][0].GetVertices()[5].getX()-a;
		Retta retta2= new Retta(x_1,x_2,y_1,y_2);
		
		double x1=intersect(retta1, retta2).getX();
		double y1=intersect(retta1, retta2).getY();
		
		y_1 = polygons[r][0].GetVertices()[4].getY()+a;
		x_1 = polygons[r][0].GetVertices()[4].getX();
		y_2 = polygons[r][c].GetVertices()[4].getY()+a;
		x_2 = polygons[r][c].GetVertices()[4].getX();
		Retta retta3= new Retta(x_1,x_2,y_1,y_2);
		

		
		y_1 = polygons[0][c].GetVertices()[2].getY();
		x_1 = polygons[0][c].GetVertices()[2].getX()+a;
		y_2 = polygons[r][c].GetVertices()[2].getY();
		x_2 = polygons[r][c].GetVertices()[2].getX()+a;
		Retta retta4= new Retta(x_1,x_2,y_1,y_2);
		
		double x2=intersect(retta2, retta3).getX();
		double y2=intersect(retta2, retta3).getY();
		
		double x3=intersect(retta3, retta4).getX();
		double y3=intersect(retta3, retta4).getY();
		
		
		double x4=intersect(retta1, retta4).getX();
		double y4=intersect(retta1, retta4).getY();
		
		
		Color[] colors = Costant.PLAYER_COLOR;
		
		Point2D p1 = new Point2D.Double(x1,y1);
		Point2D p2 = (new Point2D.Double(x2,y2));
		Point2D p3 = (new Point2D.Double(x3,y3));
		Point2D p4 = (new Point2D.Double(x4,y4));
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(colors[0]);
		
		
		g.setStroke(new BasicStroke(2.0f,                     // Line width
				BasicStroke.CAP_BUTT,    					  // End-cap style
				BasicStroke.JOIN_ROUND)); 					  // Vertex join style
		
//		Line2D.Double l1 = new Line2D.Double(p1,p2);
//		Line2D.Double l2 = new Line2D.Double(p2,p3);
//		Line2D.Double l3 = new Line2D.Double(p4,p3);
//		Line2D.Double l4 = new Line2D.Double(p4,p1);
//		g.draw(l1);
//		g.setColor(colors[1]);
//		g.draw(l2);
//		g.setColor(colors[0]);
//		g.draw(l3);
		
//		g.draw(l4);
		GeneralPath pat = new GeneralPath();
		pat.moveTo(p1.getX(), p1.getY());
		pat.lineTo(p2.getX(), p2.getY());
		pat.lineTo(polygons[r][0].GetCenter().getX(),polygons[r][0].GetCenter().getY());
		pat.lineTo(polygons[0][0].GetCenter().getX(),polygons[0][0].GetCenter().getY());
		pat.lineTo(p1.getX(), p1.getY());
		pat.closePath();
		g.fill(pat);
		g.setColor(colors[1]);
		pat = new GeneralPath();
		pat.moveTo(p1.getX(), p1.getY());
		pat.lineTo(p4.getX(), p4.getY());
		pat.lineTo(polygons[0][c].GetCenter().getX(),polygons[0][c].GetCenter().getY());
		pat.lineTo(polygons[0][0].GetCenter().getX(),polygons[0][0].GetCenter().getY());
		pat.lineTo(p1.getX(), p1.getY());
		pat.closePath();
		g.fill(pat);
		
		pat = new GeneralPath();
		pat.moveTo(p3.getX(), p3.getY());
		pat.lineTo(p2.getX(), p2.getY());
		pat.lineTo(polygons[r][0].GetCenter().getX(),polygons[r][0].GetCenter().getY());
		pat.lineTo(polygons[r][c].GetCenter().getX(),polygons[r][c].GetCenter().getY());
		pat.lineTo(p1.getX(), p1.getY());
		pat.closePath();
		g.fill(pat);
		
		g.setColor(colors[0]);
		pat = new GeneralPath();
		pat.moveTo(p3.getX(), p3.getY());
		pat.lineTo(p4.getX(), p4.getY());
		pat.lineTo(polygons[0][c].GetCenter().getX(),polygons[0][c].GetCenter().getY());
		pat.lineTo(polygons[r][c].GetCenter().getX(),polygons[r][c].GetCenter().getY());
		pat.lineTo(p1.getX(), p1.getY());
		pat.closePath();
		g.fill(pat);
	}

	private void _drawPlayersName(Graphics2D g){
		ArrayList<PlayerInfo> players = vm.getPlayersInfo();
		if(!players.isEmpty()){
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g.setColor(Color.WHITE);
			g.fillRect(0, Utility.DEFAULT_GUI_HEIGHT-105, Utility.DEFAULT_GUI_WIDTH, 110);
			g.setColor(Color.GREEN);
			g.fillRect(0, Utility.DEFAULT_GUI_HEIGHT-100, Utility.DEFAULT_GUI_WIDTH, 45);

			Font font = Utility.getFont(30);    
			g.setStroke(new BasicStroke(1.0f)); // 2-pixel lines
			g.setFont(font);
			g.setColor(players.get(0).getColor());
			g.drawString(players.get(0).getName(), 10 ,Utility.DEFAULT_GUI_HEIGHT-60);
			g.setColor(Color.WHITE);
			font = Utility.getFont(50);   
			g.setStroke(new BasicStroke(1.0f)); // 2-pixel lines
			g.setFont(font);
			g.drawString("VS", Utility.DEFAULT_GUI_WIDTH/2-15 ,Utility.DEFAULT_GUI_HEIGHT-60);
			font = Utility.getFont(30);   
			g.setStroke(new BasicStroke(1.0f)); // 2-pixel lines
			g.setFont(font);
			g.setColor(players.get(1).getColor());
			//Allineamento a destra
			FontMetrics fm = getFontMetrics( font);
			int width = fm.stringWidth(players.get(1).getName());
			g.drawString(players.get(1).getName(), Utility.DEFAULT_GUI_WIDTH-width-15 ,Utility.DEFAULT_GUI_HEIGHT-60);
			
		}
	}

	private class HexPanel extends JPanel {
		@Override
		public void paint(Graphics g){
			Graphics gb = buffer.getGraphics();
			Graphics2D gb2 = (Graphics2D)gb;

			_drawLines(gb2);
			
			gb2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			for(Cell[] prow:polygons)
				for(Cell c:prow){
					gb2.setColor(c.getColor());
					gb2.fill(c.getShape());
					gb2.setColor(Color.black);
					gb2.draw(c.getShape());
				}
			
			if(isWin)
				_drawPlayerWinString(gb2);
			_drawPlayersName(gb2);

			//super.paint(g);
			g.drawImage(buffer, 0, 0, this);
		}
	}
}
