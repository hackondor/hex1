package ia.game.hex.algorithms;

public class BoardEvent {

	private int x;
	private int y;
	private int action;
	private int player;
	public static final int SET=0;
	public static final int UNSET=1;
	public static final int STEAL = 2;
	public static final int RESET_STEAL = 3;
	
	public BoardEvent(int x,int y,int action, int player){
		this.x = x;
		this.y = y;
		this.action=action;
		this.player=player;
	}

	public int getPlayer() {
		return player;
	}

	public void setPlayer(int player) {
		this.player = player;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
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
	
	
	
}
