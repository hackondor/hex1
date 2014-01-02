package controller;

/**
 * Binding objects
 * @author Nich
 *
 */
public class SelectedCell {
	
	private int x;
	private int y;
	
	public SelectedCell(){
		x = -1;
		y = -1;
	}
	
	public void Selected(int x1,int y1){
		x = x1;
		y = y1;
	}
	
	public int getSelectedX(){
		return x;
	}
	
	public int getSelectedY(){
		return y;
	} 

}
