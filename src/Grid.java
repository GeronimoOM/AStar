import java.util.ArrayList;

public class Grid {
	OrientedGraph graph;
	int sizeX;
	int sizeY;
	private boolean blocked[];
	
	public Grid(int x, int y){
		sizeX = x;
		sizeY = y;
		graph = new OrientedGraph(sizeX * sizeY);
		for(int i = 0; i < sizeX; i++){
			for(int j = 0; j < sizeY; j++){
				if(i > 0)
					graph.addEdge(sizeX * j + i, sizeX * j + i - 1);
				if(i < sizeX - 1)
					graph.addEdge(sizeX * j + i, sizeX * j + i + 1);
				if(j > 0)
					graph.addEdge(sizeX * j + i, sizeX * (j - 1) + i);
				if(j < sizeY - 1)
					graph.addEdge(sizeX * j + i, sizeX * (j + 1) + i);
			}
		}
		blocked = new boolean[sizeX * sizeY];
	}
	
	public int xCoord(int v){
		return v % sizeX;
	}
	
	public int yCoord(int v){
		return v / sizeX;
	}
	
	public boolean isOrth(int v, int w){
		int vX = xCoord(v);
		int vY = yCoord(v);
		int wX = xCoord(w);
		int wY = yCoord(w);
		if(vX == wX){
			if(vY == wY + 1) return true;
			if(vY == wY - 1) return true;
		}
		if(vY == wY){
			if(vX == wX + 1) return true;
			if(vX == wX - 1) return true;
		}
		return false;
	}
	
	void blockVertex(int v){
		ArrayList<Integer> orth = new ArrayList<Integer>();
		for(int w: graph.adj(v)){
			graph.removeEdge(v, w);
			graph.removeEdge(w, v);
			if(isOrth(v, w)) orth.add(w);
		}
		blocked[v] = true;
	}
	
	public boolean isBlocked(int v){
		return blocked[v];
	}
	
	public Iterable<Integer> getBlocked(){
		ArrayList<Integer> res = new ArrayList<Integer>();
		for(int i = 0; i < blocked.length; i++)
			if(blocked[i]) res.add(i);
		return res;
	}
	
	public int manhattan(int v, int w){
		int vX = xCoord(v);
		int vY = yCoord(v);
		int wX = xCoord(w);
		int wY = yCoord(w);
		return Math.abs(vX - wX) + Math.abs(vY - wY);
	}
	
}
