package ia.game.hex.algorithms;

import java.util.ArrayList;
import java.util.List;

public class WinDetection {
	
	private Groups groups;
	private static final int PLAYER_HOR = 0;
	private static final int PLAYER_VERT = 1;
	private int nRows,nCols;
	private int player;
	ArrayList<Groups> GroupsList;
	private Board board;
	
	public WinDetection(int player, Board board){
		groups=new Groups();
		GroupsList=new ArrayList<Groups>();
		GroupsList.add(groups.copyGroup());
		this.player=player;
		this.nRows=board.GetRowsNumber();
		this.nCols=board.GetColumnsNumber();
		this.board=board;
	}

	public boolean isWin(){
		boolean win=_winDetect( board.getLastNodePlaced());
		GroupsList.add(groups.copyGroup());
		return win;
	}
	public boolean isWin_forArbiter(){
		return _winDetect( board.getLastNodePlaced());
	}
	
	public void undo(){
		groups=GroupsList.remove(GroupsList.size()-1);
	}
private boolean _winDetect(Node node){
	
		//Carico nelle variabili i e j le coordinate dell'ultima pedina inserita
		if(node==null)
			return false;
		int i=node.getX();
		int j=node.getY();

		
		//segs conterrà la lista dei gruppi che contengono una pedina adiacente a (i,j)
		List<Integer> segs=new ArrayList<Integer>();
		
		//ciclo su tutte le celle adiacenti a (i,j)
		for (Node n:node.getAdjacentNodes()){
			
			//se i e j sono nei range ammissibili
			if(n.getX()>=0 && n.getX()<nRows && n.getY()>=0 && n.getY()<nCols)
			{
				//ricavo la chiave con la procedura univoca Node2Int
				int key=Node.Node2Int(n, nRows);
				
				//Se la chiave è contenuta in un gruppo
				if(groups.contains(key)){
					//aggiungo il gruppo a cui appartiene alla lista segs
					segs.add(groups.get(key));
				}	
			}

		}
		int k=segs.size();
		
		// se non ci sono pedine adiacenti a (i,j) creo un nuovo gruppo
		if (k==0)
			groups.createGroup(Node.Node2Int(new Node(i,j), nRows));
		
		// se esiste una sola pedina adiacente a (i,j) aggiungo quest'ultima al suo gruppo
		else if (k==1)
			groups.addToGroup(Node.Node2Int(new Node(i,j), nRows), segs.get(0));
		
		// se esiste più di una pedina adiacente a (i,j) unifico i gruppi di queste pedine e aggiungo a questo gruppo (i,j)
		else{
			groups.unify(segs);
			//RICORDA: unify attribuisce a tutti i gruppi il valore segs.get(0)
			groups.addToGroup(Node.Node2Int(new Node(i,j), nRows), segs.get(0));
		}
		
		// se il turno è del giocatore verticale
		if(player==PLAYER_VERT)
		{
			//Controllo se esiste un gruppo che unisce una pedina dell'ultima riga con una pedina della prima riga
			for(int x=0; x<nRows; x++)
				for(int y=0;y<nRows;y++)
					if(groups.groupContains(Node.Node2Int(new Node(x, 0),nRows), Node.Node2Int(new Node(y, nCols-1),nRows)))
						return true;
		}
		
		//se il turno è del giocatore orizzontale
		else
		{
			//Controllo se esiste un gruppo che unisce una pedina della prima colonna con una pedina dell'ultima colonna
			for(int x=0; x<nCols; x++)
				for(int y=0;y<nCols;y++)
					if(groups.groupContains(Node.Node2Int(new Node(0, x),nRows), Node.Node2Int(new Node( nRows-1,y),nRows)))
						return true;
		}
		
		//Se non esiste alcun segmento vincente, il metodo ritorna false
		return false;

	}


}
