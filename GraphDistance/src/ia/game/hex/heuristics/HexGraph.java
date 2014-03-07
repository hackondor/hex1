package ia.game.hex.heuristics;

public interface HexGraph {
	
	public final static int INF = 999999;
	public final static int EMPTY_CELL = -1;
	public final static int CONNECT_PLAYER = 1;
	public final static int CUT_PLAYER = 0;
	public final static int PLAYER_VERT = 1;
	public final static int PLAYER_HOR = 0;
	
	public void placePiece(int i,int j,int player);
	public void removePiece(int i,int j);
	public int getDistance();
	public int getMaxFlow();
	public boolean isBusy(int i,int j);
}
