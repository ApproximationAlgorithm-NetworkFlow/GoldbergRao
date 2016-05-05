package com.goldberg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GoldbergRao {

	private double F;
	private double U;
	private double carrat;
	private double delta;

	public static void main(String args[]) {

		URL url = GoldbergRao.class.getResource("inputFile.txt");
		File inputFile = new File(url.getPath());
	//	File inputFile = new File("inputFile.txt");
		BufferedReader br = null;
		List<FlowNetwork> flowNetworks = new ArrayList<FlowNetwork>(); 
		try {
			br = new BufferedReader(new FileReader(inputFile));
			//Skipping comment
			br.readLine();
			int noOfTopologies = Integer.parseInt(br.readLine());
			
			for (int i = 0; i < noOfTopologies; i++) {
				FlowNetwork flowNetwork = new FlowNetwork();	
				
				//Read and construct graphs
				String graph[] = br.readLine().trim().split(" ");
				int noOfEdges = Integer.parseInt(graph[0]);
				Node sourceNode = new Node(Integer.parseInt(graph[1]));
				Node sinkNode = new Node(Integer.parseInt(graph[2]));
				flowNetwork.setSourceNode(sourceNode);
				flowNetwork.setSinkNode(sinkNode);
		
				
				for (int j = 0; j < noOfEdges; j++) {				
					String edge[] = br.readLine().trim().split(" ");
					if(edge.length != 3) {
						System.out.println("Skipping Edge. Reason: Edge should have format 'FromNode ToNode Capacity");
						continue;
					}
					
					Node fromNode = flowNetwork.getNode(Integer.parseInt(edge[0]));
					if(fromNode == null ){
						fromNode = new Node(Integer.parseInt(edge[0]));
					}
					
					
					Node toNode = flowNetwork.getNode(Integer.parseInt(edge[1]));
					if (toNode == null){
						toNode = new Node(Integer.parseInt(edge[1]));
					}
					int capacity = Integer.parseInt(edge[2]);
					
					//isStandAloneVertice()
					
					
					
					FlowEdge flowEdge = new FlowEdge(fromNode, toNode, capacity); 
					try{
						flowNetwork.addEdge(flowEdge);
					} catch (Exception e) {
						System.out.println("Cannot Add Edge " + e.toString()
								+ " to the Graph. Reason: " + e.getMessage());
					}
					
				}
				flowNetworks.toString();
				flowNetworks.add(flowNetwork);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			if (br != null) {
				br.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		//At this point we would have read all the data from the file and constructed graph details.
		//Now for all the graphs provided, we will calculate max flow for Goldberg-Rao.
		
		for (FlowNetwork flowNetwork : flowNetworks) {
			FlowNetwork maxFlow = goldbergRao(flowNetwork); 
			
		}
	}




	private static FlowNetwork goldbergRao(FlowNetwork flowNetwork) {
		
		int m = flowNetwork.getU();
		int n = flowNetwork.getV();
		double carat = Math.min(Math.pow(m, 2/3 ), Math.pow(n, 0.5));
		double F = flowNetwork.getV() * flowNetwork.getU();
		
		while(F> 1) {
			double delta = Math.ceil(F/carat);
		
			golbergTarjanBlockingFlow(flowNetwork);
		}
		return null;
	}


/*	We have a value i for the current distance label we are assigning to nodes.
	We initialize i to 0. We keep two queues. One with nodes we should assign
	i to called 'the current-queue' and one with nodes we should assign i + 1 to
	called 'the next-queue'. Whenever we process a node v, we look at all its
	incoming edges. If the edge (w; v) has positive residual capacity we calculate
	the length of the edge using `. If the length of (w; v) is zero we add node
	w to the current-queue, otherwise we add it to the next-queue. Whenever
	a node has been fully processed we get the next node to process from the
	current-queue. When the current-queue is empty we increase i by one and
	use the next-queue as the the current-queue and vice versa. We start this
	method by processing the target node, and are done when both queues are
	empty.
	To prevent us from processing the same node several times we reset the
	distance label of each node in the graph to 1 before we process them. Then
	we can recognize whether a node has been processed by looking at its label.
*/	
	private static void updateDistanceLabels(double delta, FlowNetwork flowNetwork) {
		int i = 0;
		LinkedList<Node> currentQueue = new LinkedList<Node>();
		LinkedList<Node> nextQueue = new LinkedList<Node>();
		
		flowNetwork.getSinkNode().setDist(0);
		currentQueue.add(flowNetwork.getSinkNode());
		
		//TODO Verify condition
		while(currentQueue.isEmpty() && nextQueue.isEmpty()) {
			while (currentQueue.isEmpty() == false) {
				Node v = currentQueue.pop();
				v.setDist(i);
				for (FlowEdge incomingEdge : v.getInEdges()) {
					Node w = incomingEdge.getFromNode();
					if(incomingEdge.getResidualCapacity() > 0){
						int l = binaryLength(w, v, delta, flowNetwork);
						if (l == 0) {
							currentQueue.add(w);
						} else {
							nextQueue.add(w);
						}
					}
				}
			}
			
			if(currentQueue.isEmpty() && nextQueue.isEmpty() == false) {
				i++;
				currentQueue = nextQueue;
			} 
		}
		
		
		
		while (currentQueue.isEmpty() == false ) {
			Node v = currentQueue.getFirst();
			for (FlowEdge incomingEdge : v.getInEdges()) {
				Node w = incomingEdge.getFromNode();
				if(incomingEdge.getResidualCapacity() > 0){
					int l = binaryLength(w, v, delta, flowNetwork);
					if (l == 0) {
						currentQueue.add(w);
					} else {
						nextQueue.add(w);
					}
				}
			}
			if(currentQueue.isEmpty()) {
				i++;
				currentQueue = nextQueue;
			}
		}
		
		
	}

	private static FlowNetwork golbergTarjanBlockingFlow(FlowNetwork flowNetwork) {
		
		for (FlowEdge edge : flowNetwork.edges()) {
			edge.updateFlow(0);
		}
		
		
		ArrayList<Node> initialList = topologicalSort(flowNetwork);
		LinkedList<ArrayList<Node>> L = new LinkedList<ArrayList<Node>>();
		L.add(initialList);
		
		
		for (FlowEdge edgesFromS : flowNetwork.getSourceNode().getOutEdges()) {
			
			//TODO Transfer maximum capacity flow through (s,v) and update (v,s) accordingly
			//TODO Update excess of v
			System.out.println(edgesFromS.toString());
		}
 
		// TODO while(node is active) ==> Discharge
		
		// TODO Update tree
		
		return null;
	}


	private static void dfs(FlowNetwork flowNetwork, HashMap<Node, Boolean> markedNodesMap, ArrayList<Node> sortedList, Node node) {
	    //used[u] = true;
	    markedNodesMap.remove(node);
	    markedNodesMap.put(node, true);
	    for (FlowEdge edge : node.getOutEdges())
	      if (markedNodesMap.get(edge.getToNode()) == false)
	        dfs(flowNetwork, markedNodesMap, sortedList, edge.getToNode());
	    sortedList.add(node);
	  }



	private static ArrayList<Node> topologicalSort(FlowNetwork flowNetwork) {

		HashMap<Node, Boolean> markedNodesMap = new HashMap<Node, Boolean>();
	    for(Node node : flowNetwork.getNodes()) {
	    	markedNodesMap.put(node, false);
	    }
	    
	    
	    ArrayList<Node> sortedList = new ArrayList<Node>();
	    for(Node node : flowNetwork.getNodes()) {
	    	if (markedNodesMap.get(node) == false) {
	    		dfs(flowNetwork, markedNodesMap, sortedList, node);
	    	}
	    }
	    Collections.reverse(sortedList);
	    return sortedList;
	}




	private static int binaryLength(Node fromNode, Node toNode, double delta, FlowNetwork flowNetwork) throws IllegalArgumentException {
		//FlowEdge edge = flowNetwork.getEdge(from, to);
		
		FlowEdge flowEdge = null;
		for (FlowEdge edge : fromNode.getOutEdges()) {
			if(edge.getToNode() == toNode) {
				flowEdge = edge;
				break;
			}
		}
		

		if (flowEdge.getResidualCapacity() >= 3*delta || isSpecialEdge(flowEdge, delta)) {
			return 0;
		} else {
			return 1;
		}


	}




	private static boolean isSpecialEdge(FlowEdge flowEdge, double delta) {
		
		boolean flag = false;
		if(2*delta <= flowEdge.getResidualCapacity() &&  flowEdge.getResidualCapacity() > 3*delta) {
			flag = true;
		}
		
		FlowEdge backEdge = null;
		
		Node v = flowEdge.getFromNode();
		Node w = flowEdge.getToNode();
		
		for (FlowEdge edge : w.getOutEdges()) {
			if(edge.getToNode() == v) {
				backEdge = edge;
				break;
			}
		}
		
		if (backEdge.getResidualCapacity() >= 3 * delta) {
			flag = true;
		}
		
		//TODO :d(v) == d(w) check 
		//TODO: Are all the conditions and/or 
		
		
		return flag;
	}
}
