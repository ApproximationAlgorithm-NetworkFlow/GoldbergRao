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
		// TODO Auto-generated method stub
		int nodes = flowNetwork.getNodes().size();
	    
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




	public int binaryLength(int from, int to, double delta, FlowNetwork G) throws IllegalArgumentException {
		FlowEdge edge = G.getEdge(from, to);
		if (edge != null) {
			if (edge.getResidualCapacity() >= delta) {
				return 1;
			} 
			return 0;
		} else {
			throw new IllegalArgumentException();
		}

	}
}
