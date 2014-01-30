package ia.game.hex.gui.view;

import ia.game.hex.algorithms.Costant;
import ia.game.hex.algorithms.GameListener;
import ia.game.hex.algorithms.Player;
import ia.game.hex.gui.controller.Cell;
import ia.game.hex.gui.controller.HexGuiVm;
import ia.game.hex.gui.controller.SelectedCell;
import ia.game.hex.gui.model.Utility;

import java.awt.BasicStroke;
import java.awt.Color;
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



@SuppressWarnings("serial")
public class HexGui extends JFrame implements Observer,GameListener  {

	private HexGuiVm vm;					//view model of the gui
	private Cell[][] polygons;       		//board
	private JPanel mainpanel;
	private SelectedCell selected_cell;
	private BufferedImage buffer;
	
	public HexGui(HexGuiVm vm){
		setSize(700,700);
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
		drawLine(gb2);
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
		
		//Graphics g = this.getContentPane().getGraphics();

		//Graphics2D g2 =(Graphics2D)g;
		
		gb2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		for(Cell[] prow:polygons)
			for(Cell c:prow){
				gb2.setColor(c.getColor());
			   gb2.fill(c.getShape());
			}
		
		drawLine(gb2);
		main=true;
		repaint();
	}
	private boolean main = false;

	
	//when Player args win 
	@Override
	public void update(Player args) {
		//disegna una scritta	
	}

	
	private void drawLine(Graphics2D g){
		System.out.print("ciao"); //test
		double x1 = Utility.XCENTER - 30;
		double y1 = Utility.YCENTER - 50;
		
		double y2 = vm.getNumbersOfRows()*Utility.DEFAULT_SIDE*2+Utility.MARGIN*2;
		double x2 = x1;
		
		double x3 = (vm.getNumbersOfColumn()-1)*Utility.DEFAULT_SIDE*2+Utility.MARGIN*2;
		double y3 = y2 + 9*Utility.DEFAULT_SIDE;
		
		double x4 = x3;
		double y4 = 9*Utility.DEFAULT_SIDE;;
		Color[] colors = Costant.PLAYER_COLOR;
		Point2D p1 = vm.pointRotation(new Point2D.Double(x1,y1));
		Point2D p2 = vm.pointRotation(new Point2D.Double(x2,y2));
		Point2D p3 = vm.pointRotation(new Point2D.Double(x3,y3));
		Point2D p4 = vm.pointRotation(new Point2D.Double(x3,y4));
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(colors[1]);
		
		g.setStroke(new BasicStroke(5.0f,                     // Line width
                BasicStroke.CAP_ROUND,    // End-cap style
                BasicStroke.JOIN_ROUND)); // Vertex join style
		g.draw(new Line2D.Double(p1,p2));
		g.setColor(colors[0]);
		g.draw(new Line2D.Double(p2,p3));
		g.setColor(colors[1]);
		g.draw(new Line2D.Double(p3,p4));
		g.setColor(colors[0]);
		g.draw(new Line2D.Double(p4,p1));
		

		
	}
	
	

}
