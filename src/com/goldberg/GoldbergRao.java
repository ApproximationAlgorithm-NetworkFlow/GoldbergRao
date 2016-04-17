package com.goldberg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
		try {
			br = new BufferedReader(new FileReader(inputFile));
			//Skipping comment
			br.readLine();
			int noOfTopologies = Integer.parseInt(br.readLine());
			List<FlowNetwork> flowNetworks = new ArrayList<FlowNetwork>(); 
			
			for (int i = 0; i < noOfTopologies; i++) {
				FlowNetwork flowNetwork = new FlowNetwork();
				
				//Read and construct graphs
				int noOfEdges = Integer.parseInt(br.readLine());
				for (int j = 0; j < noOfEdges; j++) {
					String edge[] = br.readLine().trim().split(" ");
					if(edge.length != 3) {
						System.out.println("Skipping Edge. Reason: Edge should have format 'FromNode ToNode Capacity");
						continue;
					}
					
					int fromNode = Integer.parseInt(edge[0]);
					int toNode = Integer.parseInt(edge[1]);
					int capacity = Integer.parseInt(edge[2]);
					FlowEdge flowEdge = new FlowEdge(fromNode, toNode, capacity); 
					try{
						flowNetwork.addEdge(flowEdge);
					} catch (Exception e) {
						System.out.println("Cannot Add Edge " + e.toString()
								+ " to the Graph. Reason: " + e.getMessage());
					}
					
				}
				
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
