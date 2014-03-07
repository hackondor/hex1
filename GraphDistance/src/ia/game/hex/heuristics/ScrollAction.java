package ia.game.hex.heuristics;

public interface ScrollAction {
	
	public int getFirstIndex();
	public int getSecondIndex();
	public boolean isEndCycle();
	public void increment();
	public void reset();

}
