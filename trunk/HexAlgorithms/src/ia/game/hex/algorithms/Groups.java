package ia.game.hex.algorithms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Groups{
	private Map<Integer,Integer> groups;
	public Groups(Groups g) {
		super();
		this.groups = new HashMap<Integer,Integer>(g.groups);
		this.nextGroup = g.nextGroup;
	}
	private int nextGroup;

	public Groups(){
		groups=new HashMap<Integer, Integer>();
		nextGroup=0;
	}
	

	public Groups copyGroup(){
		Groups g=new Groups(this);
		return g;
	}
	public boolean contains(int key){
		return groups.containsKey(key);
	}

	public int get(int key){
		return groups.get(key);
	}

	public void unify(int group1,int group2){
		for(Map.Entry<Integer, Integer> e :groups.entrySet())
		{
			if(e.getValue()==group1)
				e.setValue(group2);
		}
	}
	public void unify(List<Integer> g){
		for(Map.Entry<Integer, Integer> e :groups.entrySet())
		{
			if(g.contains(e.getValue()))
				e.setValue(g.get(0));
		}
	}
//	private boolean containsVector(int[] v, int e)
//	{
//		for (int el :v)
//			if (el==e)
//				return true;
//		return false;
//	}
	public void createGroup(int key){
		groups.put(key, nextGroup++);
	}
	
	public void addToGroup(int key, int group){		
		groups.put(key, group);
	}
	public boolean groupContains(int x,int y){
//		System.out.println("group.getX "+groups.get(x) );
//		System.out.println("group.getY "+groups.get(y) );
		if (groups.get(x)!=null && groups.get(y)!=null && groups.get(x) ==groups.get(y))
			return true;
		return false;
	}
	public void stamp(){
		System.out.println(groups);
	}
}
