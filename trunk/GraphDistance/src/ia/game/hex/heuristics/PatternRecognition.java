package ia.game.hex.heuristics;


import java.util.ArrayList;

import edu.uci.ics.jung.graph.Graph;

public class PatternRecognition {

	private Node[][] b;
	private int player, dim;
	private Node s,t;

	public PatternRecognition(Node[][] b, int player, int dim, Node s, Node t) {
		super();
		this.b = b;
		this.player = player;
		this.dim = dim;
		this.s = s;
		this.t = t;
	}
	public ArrayList<Pattern> detectPattern1(Node n){
		ArrayList<Pattern> list = new ArrayList<Pattern>();
		int i = n.getRow();
		int j = n.getColumn();

		if (b[i][j].getPlayer()==player)
		{
			Node node=b[i][j];
			for(Pattern p:pattern1Positions(node)){

				if(p.getOwnedNodes().get(0)==s || p.getOwnedNodes().get(0)==t )
				{


					boolean empty=true;
					for(Node n2:p.getEmptyNodes())
						if(n2.getPlayer()!=-1)
							empty=false;
					if(empty)
					{
						p.getOwnedNodes().add(node);
						list.add(p);
					}
				}
				else{
					int x=p.getOwnedNodes().get(0).getRow();
					int y=p.getOwnedNodes().get(0).getColumn();
					if(b[x][y].getPlayer()==player){

						boolean empty=true;
						for(Node n2:p.getEmptyNodes())
							if(n2.getPlayer()!=-1)
								empty=false;
						if(empty)
						{
							p.getOwnedNodes().add(node);
							list.add(p);
						}
					}
				}
			}
		}
		return list;
	}


	private ArrayList<Pattern> pattern1Positions(Node n){
		ArrayList<Pattern> a = new ArrayList<Pattern>();

		int i = n.getRow();
		int j = n.getColumn();

		if(i+1<dim && j+1<dim){
			Pattern p = new Pattern();
			p.getOwnedNodes().add(new Node(i+1,j+1));
			p.getEmptyNodes().add(new Node(i,j+1));
			p.getEmptyNodes().add(new Node(i+1,j));
			a.add(p);
		}
		
		if(i>0 && j>0){
			Pattern p = new Pattern();
			p.getOwnedNodes().add(new Node(i-1,j-1));
			p.getEmptyNodes().add(new Node(i,j-1));
			p.getEmptyNodes().add(new Node(i-1,j));
			a.add(p);
		}
		
		if(i>1 && j+1<dim){
			Pattern p = new Pattern();
			p.getOwnedNodes().add(new Node(i-2,j+1));
			p.getEmptyNodes().add(new Node(i-1,j+1));
			p.getEmptyNodes().add(new Node(i-1,j));
			a.add(p);
		}
		
		if(i>0 && j+2<dim){
			Pattern p = new Pattern();
			p.getOwnedNodes().add(new Node(i-1,j+2));
			p.getEmptyNodes().add(new Node(i-1,j+1));
			p.getEmptyNodes().add(new Node(i,j+1));
			a.add(p);
		}
		
		
		if(j-2>=0 && i+1<dim){
			Pattern p = new Pattern();
			p.getOwnedNodes().add(new Node(i+1,j-2));
			p.getEmptyNodes().add(new Node(i+1,j-1));
			p.getEmptyNodes().add(new Node(i,j-1));
			a.add(p);
		}
		if(i+2<dim && j-1>=0){
			Pattern p = new Pattern();
			p.getOwnedNodes().add(new Node(i+2,j-1));
			p.getEmptyNodes().add(new Node(i+1,j-1));
			p.getEmptyNodes().add(new Node(i+1,j));
			a.add(p);
		}
		if(player == HexGraph.PLAYER_HOR){
			if(j==1 && i+1<dim){
				Pattern p = new Pattern();
				p.getOwnedNodes().add(s);
				p.getEmptyNodes().add(new Node(i,j-1));
				p.getEmptyNodes().add(new Node(i+1,j-1));
				a.add(p);
			}
			if(j==dim-2 && i-1>=0){
				Pattern p = new Pattern();
				p.getOwnedNodes().add(t);
				p.getEmptyNodes().add(new Node(i-1,j+1));
				p.getEmptyNodes().add(new Node(i,j+1));
				a.add(p);
			}
		}

		else{

			if(i==1 && j+1<dim){
				Pattern p = new Pattern();
				p.getOwnedNodes().add(s);
				p.getEmptyNodes().add(new Node(i-1,j));
				p.getEmptyNodes().add(new Node(i-1,j+1));
				a.add(p);
			}
			if(i==dim-2 && j-1>=0){
				Pattern p = new Pattern();
				p.getOwnedNodes().add(t);
				p.getEmptyNodes().add(new Node(i+1,j));
				p.getEmptyNodes().add(new Node(i+1,j+1));
				a.add(p);
			}
		}



		return a;
	}
}