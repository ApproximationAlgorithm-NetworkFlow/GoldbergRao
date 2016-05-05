package com.goldberg;	

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class FlowNetwork {
	private int V;
	private int E;
	private int U = Integer.MIN_VALUE;
	private Bag<FlowEdge>[] adj;
	//SrcNode to Edge Map
	private HashMap<Node, FlowEdge> edgeToIdMap = new HashMap<Node, FlowEdge>();
	private HashSet<Node> nodes = new HashSet<Node>();
	private Node sourceNode;
	private Node sinkNode;

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
	/*public FlowNetwork(int V, int E) {
		this(V);
		for (int i = 0; i < E; i++) {
			int v = StdRandom.uniform(V);
			int w = StdRandom.uniform(V);
			double capacity = StdRandom.uniform(100);
			addEdge(new FlowEdge(v, w, capacity));
		}
	}
*/
	public void addEdge(FlowEdge e) throws IllegalArgumentException {
		E++;
		nodes.add(e.getFromNode());
		nodes.add(e.getToNode());
		e.getFromNode().addOutEdge(e);
		e.getToNode().addInEdge(e);
		edgeToIdMap.put(e.getFromNode(), e);
		
		if(e.getCapacity() > this.U) {
			this.U = e.getCapacity();
		}

	}
	/*public FlowEdge getEdge(Node fromNode, Node toNode ){
		
		if(edgeToIdMap.get(fromNode) != null) {
			for (FlowEdge edge : edgeToIdMap.get(fromNode)) {
				
			}
		}
		return null;
	}*/
	// public int E() { return E; }
	public Iterable<FlowEdge> adj(int v)
	{ 
		return adj[v]; 
	}
	//return number of vertices and edges
	public int getV(){
		return nodes.size();
	}
	
	public int E() { 
		return E; 
	}
	// return list of all edges - excludes self loops
	public Collection<FlowEdge> edges() {
		ArrayList<FlowEdge> list = new ArrayList<FlowEdge>();
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

	public Collection<Node> getNodes() {
		return nodes;
	}
	
	public int getU() {
		return this.U;
	}
	
	public Node getSourceNode() {
		return sourceNode;
	}

	public void setSourceNode(Node sourceNode) {
		this.sourceNode = sourceNode;
		nodes.add(sourceNode);
	}

	public Node getSinkNode() {
		return sinkNode;
	}

	public void setSinkNode(Node sinkNode) {
		this.sinkNode = sinkNode;
		nodes.add(sinkNode);
	}

	
	public Node getNode(int value){
		for (Node node : this.nodes) {
			if(node.value == value) {
				return node;
			}
		}
		return null;
	}
	
	
} 

