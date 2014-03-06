
public class ScrollActionByRow implements ScrollAction {

	private int rows;
	private int columns;
	
	public ScrollActionByRow(int rows,int columns){
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
		return i == rows;
	}

	
	public void increment(){
		if(j==columns-1){
			j = 0;
			i++;
		}else
			j++;
	}

	@Override
	public void reset() {
		i = 0;j = 0;
		
	}
	

	
	

}
