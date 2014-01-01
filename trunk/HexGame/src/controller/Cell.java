package controller;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.Observable;
import java.util.Observer;

/**
 * Binding objects
 * @author Nich
 *
 */
public class Cell extends Observable  {

	private Point2D[] vertices;
	private Color color;
	private Point2D center;
	private GeneralPath shape;
	
	public Cell(){
		color =Color.WHITE;
		center = new Point2D.Double(0.F,0.F);
		shape = null;
		vertices = null;
	}
	
	public void SetCenter(Point2D point) {
		// TODO Auto-generated method stub
		center = point;
		setChanged();
		notifyObservers();
	}
	
	public Point2D GetCenter(){
		return center;
	}
	
	public void SetVertices(Point2D[] points) {
		// TODO Auto-generated method stub
		vertices = points;
		setChanged();
		notifyObservers();
	}
	
	public Point2D[] GetVertices(){
		return vertices;
	}
	
	public void setShape(GeneralPath shape) {
		this.shape = shape;
		setChanged();
		notifyObservers();
		
	}
	public GeneralPath getShape() {
		return shape;
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
		setChanged();
		notifyObservers();
	}

	
}
