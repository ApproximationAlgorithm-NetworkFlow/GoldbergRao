package com.goldberg;	

import java.util.HashMap;

public class FlowNetwork {
	private int V;
	private int E;
	private Bag<FlowEdge>[] adj;
	private HashMap<String, FlowEdge> edgeToIdMap = new HashMap<String, FlowEdge>();

	// Empty Graph
	public FlowNetwork() {
		V = 1;
		E = 0;
	}

	//empty graph with V edges
	public FlowNetwork(int V)
	{
		this.V = V;
		E=0;
		//adj = (Bag<FlowEdge>[]) new Bag[V];
		for (int v = 0; v < V; v++)
			adj[v] = new Bag<FlowEdge>();
	}
	// random graph with V vertices and E edges
	public FlowNetwork(int V, int E) {
		this(V);
		for (int i = 0; i < E; i++) {
			int v = StdRandom.uniform(V);
			int w = StdRandom.uniform(V);
			double capacity = StdRandom.uniform(100);
			addEdge(new FlowEdge(v, w, capacity));
		}
	}

	public void addEdge(FlowEdge e) throws IllegalArgumentException {
		E++;
		// adj[v].add(e);
		// adj[w].add(e);
		String key = String.valueOf(e.getFromNode()) + "-"
				+ String.valueOf(e.getToNode());
		if (edgeToIdMap.get(key) != null) {
			throw new IllegalArgumentException("Edge already exists");
		}
		edgeToIdMap.put(key, e);

	}
	public FlowEdge getEdge(int s, int t)
	{
		String key = String.valueOf(s) + "-" + String.valueOf(t);
		if(edgeToIdMap.get(key) != null) {
			return edgeToIdMap.get(key);
		}
		return null;
	}
	// public int E() { return E; }
	public Iterable<FlowEdge> adj(int v)
	{ 
		return adj[v]; 
	}
	//return number of vertices and edges
	public int V()
	{
		return V;
	}
	public int E() 
	{ 
		return E; 
	}
	// return list of all edges - excludes self loops
	public Iterable<FlowEdge> edges() {
		Bag<FlowEdge> list = new Bag<FlowEdge>();
		for(FlowEdge flowEdge : edgeToIdMap.values()){
			list.add(flowEdge);
		}

		/*     for (int v = 0; v < V; v++)
         for (FlowEdge e : adj(v)) {
             if (e.to() != v)
                 list.add(e);
                      }*/
		return list;
	}

} 