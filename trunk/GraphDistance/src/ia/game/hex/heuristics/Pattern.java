package ia.game.hex.heuristics;



import java.util.ArrayList;
import java.util.Iterator;

public class Pattern  {
	private ArrayList<Node> emptyNodes;
	private ArrayList<Node> ownedNodes;
	public Pattern(){
		emptyNodes = new ArrayList<Node>();
		ownedNodes = new ArrayList<Node>();
	}



	public ArrayList<Node> getEmptyNodes() {
		return emptyNodes;
	}



	public void setEmptyNodes(ArrayList<Node> emptyNodes) {
		this.emptyNodes = emptyNodes;
	}



	public ArrayList<Node> getOwnedNodes() {
		return ownedNodes;
	}



	public void setOwnedNodes(ArrayList<Node> ownedNodes) {
		this.ownedNodes = ownedNodes;
	}



	public Pattern(ArrayList<Node> emptyNodes, ArrayList<Node> ownedNodes) {
		super();
		this.emptyNodes = emptyNodes;
		this.ownedNodes = ownedNodes;
	}



}