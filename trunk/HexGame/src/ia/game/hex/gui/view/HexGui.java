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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}
		players = vm.getPlayersInfo();
		Utility.createFont();
		setSize(Utility.DEFAULT_GUI_WIDTH,Utility.DEFAULT_GUI_HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainpanel = new JPanel();
		mainpanel.setSize(this.getSize());
		mainpanel.addMouseListener(new ClickListener());
		add(mainpanel);
		this.vm=vm;
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
        
        radioAction1.addActionListener(new ActionListener() {
  
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Human VS IA selected");
				//restart game
				//use a Vm method. Vm use a setMode method of Game class. Delegate(observer) is an idea
			}
        });
        
        radioAction2.addActionListener(new ActionListener() {

        	@Override
        	public void actionPerformed(ActionEvent arg0) {
        		System.out.println("IA VS IA selected");
        		//restart game
        		//use a Vm method. Vm use a setMode method of Game class. Delegate is an idea
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
	

	
	public void paint(Graphics gg){
		if (!main)
		super.paint(gg);
//		Graphics g = mainpanel.getGraphics();
//		gg.drawImage(buffer, 0, 0,this);
//		mainpanel.repaint();
		main=false;
		Graphics gb = buffer.getGraphics();
		Graphics2D gb2 = (Graphics2D)gb;
		
		//Graphics g = this.getContentPane().getGraphics();

		//Graphics2D g2 =(Graphics2D)g;
		
		//gb2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		for(Cell[] prow:polygons)
			for(Cell c:prow){
				gb2.setColor(c.getColor());
			   gb2.fill(c.getShape());
			}
		_drawLines(gb2);
		if(isWin)
			_drawPlayerWinString(gb2);
		_drawPlayersName(gb2);
		mainpanel.getGraphics().drawImage(buffer, 0, 0, this);
		}
	
	 class ClickListener implements MouseListener {
		
		int x;
		int y;
		
		@Override
		public void mouseClicked(MouseEvent arg0) {
			x = arg0.getX();
			y = arg0.getY();
			System.out.println(x+";"+y); //test
			selected_cell.Selected(x,y);
			vm.OnSelectedCommand().execute(null);
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
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	}

	 
	@Override
	public void update(Observable arg0, Object arg1) {
	
		Graphics gb = buffer.getGraphics();
		Graphics2D gb2 = (Graphics2D)gb;
		

		gb2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		for(Cell[] prow:polygons)
			for(Cell c:prow){
				gb2.setColor(c.getColor());
			   gb2.fill(c.getShape());
			}
		
		_drawLines(gb2);
		_drawPlayersName(gb2);
		main=true;
		repaint();
	}
	private boolean main = false;

	
	//when Player args win 
	@Override
	public void update(Player args) {
		isWin = true;
		playerWin = args;
	}
	
	private void _drawPlayerWinString(Graphics2D g){
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		        RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.GREEN);
		g.fillRect(0, 50, 700, 200);
		

		Font font = Utility.getFont(100);    
		g.setStroke(new BasicStroke(2.0f)); // 2-pixel lines
	    g.setFont(font);
	    g.setColor(playerWin.getPlayerColor());
		g.drawString(playerWin.getName()+" win! ", 50, 200);
		
	}

	//draw the lines that sorround the board
	private void _drawLines(Graphics2D g){
		double x1 = Utility.XCENTER - 30;
		double y1 = Utility.YCENTER - 50;
		
		double y2 = vm.getNumbersOfRows()*Utility.DEFAULT_SIDE*2+Utility.MARGIN*2;
		double x2 = x1;
		
		double x3 = (vm.getNumbersOfColumn()-1)*Utility.DEFAULT_SIDE*2+Utility.MARGIN*2;
		double y3 = y2 + 9*Utility.DEFAULT_SIDE;
		
		double x4 = x3;
		double y4 = 9*Utility.DEFAULT_SIDE;
		
		Color[] colors = Costant.PLAYER_COLOR;
		Point2D p1 = vm.pointRotation(new Point2D.Double(x1,y1));
		Point2D p2 = vm.pointRotation(new Point2D.Double(x2,y2));
		Point2D p3 = vm.pointRotation(new Point2D.Double(x3,y3));
		Point2D p4 = vm.pointRotation(new Point2D.Double(x4,y4));
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(colors[1]);
		
		g.setStroke(new BasicStroke(5.0f,                     // Line width
                BasicStroke.CAP_ROUND,    					  // End-cap style
                BasicStroke.JOIN_ROUND)); 					  // Vertex join style
		g.draw(new Line2D.Double(p1,p2));
		g.setColor(colors[0]);
		g.draw(new Line2D.Double(p2,p3));
		g.setColor(colors[1]);
		g.draw(new Line2D.Double(p3,p4));
		g.setColor(colors[0]);
		g.draw(new Line2D.Double(p4,p1));
		
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
			g.drawString(players.get(1).getName(), Utility.DEFAULT_GUI_WIDTH-90 ,Utility.DEFAULT_GUI_HEIGHT-60);
			System.out.println("test");//test
		}
	}
	
	

}
