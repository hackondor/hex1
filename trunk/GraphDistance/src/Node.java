
	/**
	 * Informazioni di un nodo
	 * @author Nich
	 *
	 */
	public class Node{
		private int row = -1;
		private int column = -1;
		private int player = -1;		//-1:empty cell
		
		public Node(int row, int colum) {
			this.row = row;
			this.column = colum;
		}
		
		public Node(int row, int colum,int player) {
			this.row = row;
			this.column = colum;
			this.player = player;
		}
		
		public int getPlayer() {
			return player;
		}

		public void setPlayer(int player) {
			this.player = player;
		}
		

		public int getRow() {
			return row;
		}
		public void setRow(int row) {
			this.row = row;
		}
		public int getColumn() {
			return column;
		}
		public void setColum(int colum) {
			this.column = colum;
		}
		
		public String toString(){
			return row+" ; "+column;
		}
		
	}