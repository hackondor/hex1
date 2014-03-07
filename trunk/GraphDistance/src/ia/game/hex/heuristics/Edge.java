package ia.game.hex.heuristics;

public class Edge {
	private int id;
	private int capacity;
	
	public Edge(int id,int capacity){
		this.id = id;
		this.capacity = capacity;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public String toString(){
		return capacity+"";
	}
	
	

}
