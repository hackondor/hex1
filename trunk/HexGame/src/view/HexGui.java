package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
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

import model.Utility;
import controller.Cell;
import controller.HexGuiVm;
import controller.SelectedCell;



@SuppressWarnings("serial")
public class HexGui extends JFrame implements Observer  {

	private HexGuiVm vm;					//view model of the gui
	private Cell[][] polygons;       		//board
	private JPanel mainpanel;
	private SelectedCell selected_cell;
	
	public HexGui(HexGuiVm vm){
		setSize(800,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainpanel = new JPanel();
		this.setBackground(Color.ORANGE);
		mainpanel.setBackground(Color.ORANGE);
		mainpanel.addMouseListener(new ClickListener());
		add(mainpanel);
		this.vm=vm;
		polygons = vm.getShapes(); //binding method
		for(Cell[] crow:polygons)
			for(Cell c:crow)
			c.addObserver(this);
		selected_cell = new SelectedCell();//binding object
		vm.setSelectedCell(selected_cell);
		
	
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
			}
        });
        
        radioAction2.addActionListener(new ActionListener() {

        	@Override
        	public void actionPerformed(ActionEvent arg0) {
        		System.out.println("IA VS IA selected");
        		//restart game
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
		super.paint(gg);
		Graphics g = mainpanel.getGraphics();
		Graphics2D g2 =(Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		for(Cell[] prow:polygons)
			for(Cell c:prow){
				g2.setColor(c.getColor());
			   g2.fill(c.getShape());
			}
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
			vm.OnSelectedCommand().execute();
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
		this.repaint();
	}

	

}
