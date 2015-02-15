import java.util.HashMap;
import java.util.HashSet;


public class AStarPathFinder {
    final OrientedGraph graph;
	IndexMinPQ<Integer> openedList;
	HashSet<Integer> closedList;
	HashMap<Integer, Integer> gScore;
	HashMap<Integer, Integer> hScore; 
	HashMap<Integer, Integer> parent;
	int start;
	int dest;
	int current;
	boolean completed = false;
	int i = 0;

	public AStarPathFinder(OrientedGraph graph, int start, int dest){
		this.graph = graph;
		this.start = start;
		this.dest = dest;
		int v = Main.grid.graph.V();
		gScore = new HashMap<Integer, Integer>();
		hScore = new HashMap<Integer, Integer>();
		parent = new HashMap<Integer, Integer>();
		openedList = new IndexMinPQ<Integer>(v);
		closedList = new HashSet<Integer>();
		parent.put(start, null);
		gScore.put(start, 0);
		hScore.put(start, Main.grid.manhattan(start, dest));
		openedList.insert(start, hScore.get(start));
	}
	
	public void nextStep(){
		current = openedList.delMin();
		for(int v: graph.adj(current)){
			if(!closedList.contains(v)){
				if(!openedList.contains(v)){
					parent.put(v, current);
					gScore.put(v, gScore.get(current) + 1);
					hScore.put(v, Main.grid.manhattan(v, dest));
					openedList.insert(v, gScore.get(v) + hScore.get(v));
				}
				else if(gScore.get(current) + 1 < gScore.get(v)){
					parent.put(v, current);
					gScore.put(v, gScore.get(current) + 1);
					openedList.changeKey(v, gScore.get(v) + hScore.get(v));
				}
			}
		}
		closedList.add(current);
		if(openedList.isEmpty()){
			completed = true;
		}
		if(current == dest) completed = true;
	}
	
	public Iterable<Integer> getPath(){
		if(current != dest) return null;
		LinkedStack<Integer> res = new LinkedStack<Integer>();
		for(int x = current; x != start; x = parent.get(x))
			res.push(x);
		res.push(start);
		return res;
	}
	
}
