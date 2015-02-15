import java.util.ArrayList;

public class OrientedGraph {

	private final int V;
	private final boolean adj[][];
	
	public OrientedGraph(int V){
		this.V = V;
		adj = new boolean[V][V];
	}
	
	public void addEdge(int v, int w)	{
		adj[v][w] = true;
	}
	
	public Iterable<Integer> adj(int v){ 
		ArrayList<Integer> res = new ArrayList<Integer>();
		for(int i = 0; i < V; i++)
			if(adj[v][i])
				res.add(i);
		return res;
	}
	
	
	public int degree(int v){
		int res = 0;
		for(int i = 0; i < V; i++)
			if(adj[v][i])
				res++;
		return res;
	}
	
	public boolean hasEdge(int v, int w){
		return adj[v][w];
	}
	
	public void removeEdge(int v, int w){
		adj[v][w] = false;
	}
	
	public int V(){
		return V;
	}
}
