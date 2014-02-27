package ia.game.hex.algorithms;

public class Node{
	private int x;
	private int y;
	
	public Node(int x,int y){
		this.x = x;
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public static int Node2Int (Node n, int nRows){
		return n.getX()*nRows+n.getY();
	}
	public static Node Int2Node (int i, int nRows){
		return new Node(i/nRows,i%nRows);
	}
	public Node[] getAdjacentNodes(){
		Node[] adjacents= new Node[6];
		adjacents[0]=new Node(x-1,y);
		adjacents[1]=new Node(x,y-1);
		adjacents[2]=new Node(x-1,y+1);
		adjacents[3]=new Node(x+1,y-1);
		adjacents[4]=new Node(x,y+1);
		adjacents[5]=new Node(x+1,y);
		return adjacents;
	}
	
	
	
}