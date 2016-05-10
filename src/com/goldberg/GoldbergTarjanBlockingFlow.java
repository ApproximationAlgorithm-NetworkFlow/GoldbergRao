package com.goldberg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class GoldbergTarjanBlockingFlow {

	private static LinkedList<Node> fifoQueue = new LinkedList<Node>();
	private static final int Infinite = Integer.MAX_VALUE;
	public GoldbergTarjanBlockingFlow(Node source){
		//fifoQueue.add(source);
	}
	public static void push(FlowEdge edge){
		int flowAmt = Math.min(edge.getFromNode().getExcess(), edge.getResidualCapacity());
		edge.getToNode().setExcess(edge.getToNode().getExcess()+flowAmt);
		edge.getFromNode().setExcess(edge.getFromNode().getExcess()-flowAmt);
		//Update flow does current flow + flow Amt and then updatesResidualCapacity()
		edge.updateFlow(flowAmt);
		//edge.setResidualCapacity(edge.getCapacity()-edge.getFlow());
		//return flowAmt;
	}
	public static void relabel(Node node){
		int minLabel = -1;
		ArrayList<FlowEdge> neighbors=node.getOutEdges();
		for (FlowEdge e : neighbors) {

			if (e.getResidualCapacity() > 0)
			// && e.getFromNode().getDist() <= e.getToNode().getDist())
			{
				if (minLabel > e.getToNode().getDist() || minLabel == -1) {
					minLabel = e.getToNode().getDist();
				}
			}
		}
		if(minLabel != -1){
			node.setDist(minLabel+1);
		} 
	//	node.setLabel(minLabel+1);
	}
	public static void pushRelabel(Node v, HashMap<Node,Integer> nodeToCurrentEdgePointer){
		int currentEdge = nodeToCurrentEdgePointer.get(v);
		ArrayList<FlowEdge> edgeList = v.getOutEdges();
		
		//TODO Verify
		//while(true) {
			FlowEdge edge = edgeList.get(currentEdge);
			Node u = edge.getToNode();
			if( v.getExcess() > 0 && edge.getResidualCapacity() >0 && v.getDist() == (u.getDist() + 1)) {
				push(edge);
			} else {
				if (currentEdge != (edgeList.size() -1) ) {
					currentEdge++;
				} else {
					currentEdge = 0;
					if(v.getExcess() > 0) {
						relabel(v);
					}
			//		break;
				}
				nodeToCurrentEdgePointer.put(v, currentEdge);
				
			}
		//}
		
		
		
		/*for(FlowEdge e: v.getOutEdges()){
			count++;
			Node u = e.getToNode();
			if(u.getExcess()>0 && v.getDist() == (u.getDist() + 1)){
				push(e);
			}
			//check if e is the last edge
			else {
				if(count != v.getOutEdges().size())
					continue;
				else
					relabel(v);
				break;
			}
		}*/

	}
	public void discharge(FlowNetwork flowNetwork){
		
		for (FlowEdge edge : flowNetwork.edges()) {
			//Saturate edges from source. Update the rest of the edges to be 0.
			if(edge.getFromNode() == flowNetwork.getSourceNode()) {
				int capacity = edge.getCapacity();
				edge.updateFlow(capacity);
				edge.getToNode().setExcess(capacity);
				int excessOfSource = flowNetwork.getSourceNode().getExcess() - capacity;
				flowNetwork.getSourceNode().setExcess(excessOfSource);
				fifoQueue.add(edge.getToNode());
			} else {
				edge.updateFlow(0);
			}
		}	
		HashMap<Node, Integer> nodeToCurrentEdgePointer = new HashMap<Node, Integer>();
		for (Node node : flowNetwork.getNodes()) {
			nodeToCurrentEdgePointer.put(node, 0);
		}


		while (fifoQueue.isEmpty() == false) {

			Node v = fifoQueue.pop();
			int dist = v.getDist();
			do {
				dist = v.getDist();
				int currentPointer = nodeToCurrentEdgePointer.get(v);
				FlowEdge edge = v.getOutEdges().get(currentPointer);
				int oldResCapacity = edge.getResidualCapacity();
				
				pushRelabel(v, nodeToCurrentEdgePointer);
				Node w = edge.getToNode();
				//for (FlowEdge e : v.getOutEdges()) {
				//Node w = e.getToNode();
				//Checks if push was performed and if  w is active aftet the push
				if (edge.getResidualCapacity() != oldResCapacity && w.getExcess() > 0) {
					fifoQueue.add(w);
				}
				//}

			} while (v.getExcess() != 0 && v.getDist() < dist);

			if (v.getExcess() > 0) {
				fifoQueue.add(v);
			}
		}
	}
}
