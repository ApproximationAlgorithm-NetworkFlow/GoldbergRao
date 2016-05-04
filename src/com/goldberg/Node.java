package com.goldberg;

import java.util.ArrayList;

public class Node {
	int value;
	private boolean isBlocked = false;
	private ArrayList<FlowEdge> outEdges = new ArrayList<>();
	private ArrayList<FlowEdge> inEdges = new ArrayList<>();
	
	public Node(int value) {
		this.value = value;
	}

	
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public boolean isBlocked() {
		return isBlocked;
	}

	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	public ArrayList<FlowEdge> getOutEdges() {
		return outEdges;
	}


	public void setOutEdges(ArrayList<FlowEdge> outEdges) {
		this.outEdges = outEdges;
	}

	public ArrayList<FlowEdge> getInEdges() {
		return inEdges;
	}

	public void setInEdges(ArrayList<FlowEdge> inEdges) {
		this.inEdges = inEdges;
	}

	public void addOutEdge(FlowEdge flowEdge) {
		this.outEdges.add(flowEdge);
	}
	
	public void addInEdge(FlowEdge flowEdge) {
		this.inEdges.add(flowEdge);
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}
	
	@Override
	public String toString() {
		
		return String.valueOf(value);
	}
	
}
