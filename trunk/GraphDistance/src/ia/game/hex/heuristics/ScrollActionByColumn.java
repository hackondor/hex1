package ia.game.hex.heuristics;

public class ScrollActionByColumn implements ScrollAction {

	private int rows;
	private int columns;
	
	public ScrollActionByColumn(int rows,int columns){
		this.rows = rows;
		this.columns = columns;
	}
	
	private int i = 0;
	private int j = 0;
	@Override
	public int getFirstIndex() {
		
		return i;
	}

	@Override
	public int getSecondIndex() {
		// TODO Auto-generated method stub
		return j;
	}
	
	public boolean isEndCycle(){
		return j==columns;
	}

	
	public void increment(){
		if(i==rows-1){
			i= 0;
			j++;
		}else
			i++;
	}
	
	@Override
	public void reset() {
		i = 0;j = 0;
		
	}
	

	
	

}
