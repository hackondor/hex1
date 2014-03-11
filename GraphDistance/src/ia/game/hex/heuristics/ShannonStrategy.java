package ia.game.hex.heuristics;

import ia.game.hex.algorithms.AlgorithmsDefinition;
import ia.game.hex.algorithms.InvalidPlacementException;
import ia.game.hex.algorithms.MultipleActionExeption;

public class ShannonStrategy extends AlgorithmsDefinition {

	private int[][] pairs = null;
	boolean handicapOccupate = true;


	public ShannonStrategy(String name) {
		super(name);
	}

	@Override
	public void run() {


		if(pairs == null){
			pairs = new int[getRowsNumber()-1][getColumnsNumber()];
			int j = 0;
			int i = 0;
			int k = 1;
			int l=2;

			while(getColumnsNumber()-l>=0){
				j= getColumnsNumber()-l;
				i=0;
				while(j>=0)
					pairs[i++][j--] = k++; 
				l++;
			}

			k=1;
			i=1;
			j = getColumnsNumber()-1;
			l = 0;
			while(l<=getRowsNumber()-2){
				j= getColumnsNumber()-1;
				i=l;
				while(i<=getRowsNumber()-2)
					pairs[i++][j--] = k++; 
				l++;
			}
		}


		String[] s = new String[getRowsNumber()];

		for(int i = 0;i<getRowsNumber()-1;i++)
			s[i] = "";

		for(int i = 0;i<getRowsNumber()-1;i++)
			for(int j=0;j<getColumnsNumber();j++)
				s[i] += pairs[i][j]+ " ";

		for(int i = 0;i<getRowsNumber()-1;i++)
			System.out.println(s[i]);


		int moveX = 0;
		int moveY = 0;
		int h1x = getRowsNumber()-2;
		int h1y = getColumnsNumber()-3;
		
		if(!handicapOccupate){

			



			if(!isBusy(h1x, h1y)){
				moveX = h1x;
				moveY = h1y;
		
			}else if(!isBusy(h1x,1)){
				moveX = h1x;
				moveY = 1;
			}
			

		}else{


			if(getLastNodePlaced().getX()!=getRowsNumber()-1){

				int opponent = pairs[getLastNodePlaced().getX()][getLastNodePlaced().getY()];



				for(int i=0;i<getRowsNumber()-1;i++)
					for(int j=0;j<getColumnsNumber();j++)
						if(pairs[i][j] == opponent && (i!=getLastNodePlaced().getX() || j!=getLastNodePlaced().getY())){
							moveX = i;
							moveY = j;
						}
			}else{
				moveX = getLastNodePlaced().getX();
				if(getLastNodePlaced().getY()<getColumnsNumber()-1)
					moveY = getLastNodePlaced().getY()+1;
				else
					moveY = getLastNodePlaced().getY()-1;
			}
		}

		try {
			System.out.println(moveX+" "+moveY);
			placePiece(moveX, moveY);
		} catch (InvalidPlacementException | MultipleActionExeption e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(isBusy(h1x, h1y) && isBusy(h1x,1) )
			handicapOccupate = true;

	}

}
