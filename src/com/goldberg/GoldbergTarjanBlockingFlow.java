package com.goldberg;

import java.util.ArrayList;

public class GoldbergTarjanBlockingFlow {

	private static Queue<Node> fifoQueue = new Queue<Node>();
	private static final int Infinite = Integer.MAX_VALUE;
	public GoldbergTarjanBlockingFlow(Node source){
		fifoQueue.enqueue(source);
	}
	public static void push(FlowEdge edge)
	{

			int flowAmt = Math.min(edge.getFromNode().getExcess(), edge.getCapacity()-edge.getFlow());
			edge.getToNode().setExcess(edge.getToNode().getExcess()+flowAmt);
			edge.getToNode().setExcess(edge.getToNode().getExcess()-flowAmt);
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
			if(e.getResidualCapacity()>0)
			{
				if(minLabel > e.getToNode().getLabel())
				{
					minLabel =  e.getToNode().getLabel();
				}
			}
		}
		node.setLabel(minLabel+1);
	}
	public static void pushRelabel(Node v)
	{
		int count =0;
		for(FlowEdge e: v.getOutEdges())
		{
			count++;
			if(e.getToNode().getExcess()>0)
			{
				push(e);
			}
			//check if e is the last edge
			else 
			{
				if(count != v.getOutEdges().size())
					continue;
				else
					break;
			}
		}
		relabel(v);
	}
	public void discharge()
	{
		Node v= fifoQueue.dequeue();
		int dist = v.getDist();
		do
		{
			pushRelabel(v);
			for(FlowEdge e:v.getOutEdges())
			{
				if(e.getToNode().getExcess()>0)
				{
					fifoQueue.enqueue(e.getToNode());
				}
			}
			
		}while(v.getExcess()==0 || v.getDist()<dist);
		if(v.getExcess() > 0)
		{
			fifoQueue.enqueue(v);
		}
	}
}
