package com.goldberg;

import java.util.ArrayList;
import java.util.LinkedList;

public class GoldbergTarjanBlockingFlow {

	private static LinkedList<Node> fifoQueue = new LinkedList<Node>();
	private static final int Infinite = Integer.MAX_VALUE;
	public GoldbergTarjanBlockingFlow(Node source){
		fifoQueue.add(source);
	}
	public static void push(FlowEdge edge){
			int flowAmt = Math.min(edge.getFromNode().getExcess(), edge.getCapacity()-edge.getFlow());
			edge.getToNode().setExcess(edge.getToNode().getExcess()-flowAmt);
			edge.getFromNode().setExcess(edge.getToNode().getExcess()+flowAmt);
			//Update flow does current flow + flow Amt and then updatesResidualCapacity()
			edge.updateFlow(flowAmt);
			//edge.setResidualCapacity(edge.getCapacity()-edge.getFlow());
			//return flowAmt;
	}
	public static void relabel(Node node)
	{
		int minLabel = Infinite;
		ArrayList<FlowEdge> neighbors=node.getOutEdges();
		for(FlowEdge e:neighbors)
		{
			if(e.getResidualCapacity()>0 && e.getFromNode().getDist() <= e.getToNode().getDist())
			{
				if(minLabel > e.getToNode().getLabel())
				{
					minLabel =  e.getToNode().getLabel();
				}
			}
		}
		node.setLabel(minLabel+1);
	}
	public static void pushRelabel(Node v){
		int count = 0;
		for(FlowEdge e: v.getOutEdges()){
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
		}
		
	}
	public void discharge()
	{
		Node v = fifoQueue.pop();
		int dist = v.getDist();
		do
		{
			pushRelabel(v);
			for(FlowEdge e:v.getOutEdges())
			{
				if(e.getToNode().getExcess()>0)
				{
					fifoQueue.add(e.getToNode());
				}
			}
			
		}while(v.getExcess()==0 || v.getDist()<dist);
		if(v.getExcess() > 0)
		{
			fifoQueue.add(v);
		}
	}
}
