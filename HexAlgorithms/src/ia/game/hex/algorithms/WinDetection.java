package ia.game.hex.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
/**
 * WinDetection è una classe che monitora lo stato del gioco per riconoscere la vittoria di
 * un singolo giocatore.
 * Per funzionare ha bisogno di "Osservare" la Board del gioco, in quanto ad ogni modifica
 * della board, si aggiorneranno le strutture dati interne.
 * Per utilizzarla all'interno degli algoritmi di IA, in cui vengono eplorate tutte le
 * possibili mosse è necessario:
 * - creare 2 istanze di tale classe assegnarle una al giocatore corrente
 * 	 e l'altra al giocatore avversario
 * - registrare la classe SIA alla Board del gioco SIA alla Board copia utilizzata nell'algoritmo;
 * - avere cura di invocare il metodo Board.resetPosition(i,j) sull'ultima pedina posizionata in modo
 *   che l'algoritmo ripristini lo stato precedente alla mossa
 * - 
 * @author Pietro
 *
 */
public class WinDetection implements Observer {

	private Groups groups;
	private static final int PLAYER_VERT = 1;
	private int nRows,nCols;
	private int player;
	ArrayList<Groups> GroupsList;
	private int f;
	public WinDetection(int player, int nRows, int nCols){
		groups=new Groups();
		GroupsList=new ArrayList<Groups>();
		this.player=player;
		this.nRows=nRows;
		this.nCols=nCols;
		f=0;
	}

	public boolean isWin(){

		boolean win = _winDetect();

		return win;

	}
	//	public boolean isWin_forArbiter(){
	//		Node last=board.getLastNodePlaced();
	//		if(board.belongTo(last.getX(), last.getY())!=player 
	//				|| 
	//				groups.contains(Node.Node2Int(last,nRows)))
	//			return win;
	//		win=_winDetect( board.getLastNodePlaced());
	//		return win;
	//	}

	private void undo(){
		groups=GroupsList.remove(GroupsList.size()-1);

	}
	private boolean _winDetect(){

		//Carico nelle variabili i e j le coordinate dell'ultima pedina inserita


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




	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub

		BoardEvent e=(BoardEvent)arg1;
		int i=e.getX();
		int j=e.getY();
		if(e.getAction()==BoardEvent.SET && player==e.getPlayer()){

			Node node=new Node(i,j);
			GroupsList.add(groups.copyGroup());
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
		}
		else{
			if(player==e.getPlayer()){
				//System.out.println("undo: "+i+" "+j);
				undo();
			}

		}
	}

	public int getGroupsListSize(){
		return GroupsList.size();
	}

}
