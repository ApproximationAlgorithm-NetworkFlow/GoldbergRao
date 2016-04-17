package com.goldberg;

public class FlowEdge {
	private int fromNode, toNode; // from and to
	private double capacity; // capacity is final as it will not change once initialized
	private double flow; // flow

	public FlowEdge(int v, int w, double capacity) {
		if (this.capacity < 0)
			throw new RuntimeException("Negative edge capacity");
		this.fromNode = v;
		this.toNode = w;
		this.capacity = capacity;
		this.flow = 0;
	}

	public FlowEdge(int v, int w, double capacity, double flow) {
		if (capacity < 0)
			throw new RuntimeException("Negative edge capacity");
		this.fromNode = v;
		this.toNode = w;
		this.capacity = capacity;
		this.flow = flow;
	}
	/*
	public int other(int vertex) {
		if (vertex == fromNode)
			return toNode;
		else if (vertex == toNode)
			return fromNode;
		else
			throw new RuntimeException("endpoint not correct");
	}

	public double residualCapacityTo(int vertex) {
		if (vertex == fromNode)
			return flow;
		else if (vertex == toNode)
			return capacity - flow;
		else
			throw new IllegalArgumentException();
	}

	public void addResidualFlowTo(int vertex, double delta) {
		if (vertex == fromNode)
			flow -= delta;
		else if (vertex == toNode)
			flow += delta;
		else
			throw new IllegalArgumentException();
	}
	 */
	public double getResidualCapacity() {
		return capacity - flow;
	}

	public int getFromNode() {
		return fromNode;
	}

	public int getToNode() {
		return toNode;
	}

	public double getCapacity() {
		return capacity;
	}

	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	public double getFlow() {
		return flow;
	}

	public void setFlow(double flow) {
		this.flow = flow;
	}
	@Override
	public String toString() {
		StringBuilder buider = new StringBuilder();
		buider.append("From-Node: ").append(getFromNode());
		buider.append("  To-Node: ").append(getToNode());
		buider.append("  Capacity: ").append(getCapacity());
		if(flow != 0){
			buider.append("  Flow: ").append(getFlow());
			buider.append("  Residual-Flow: ").append(getResidualCapacity());
		}
		return buider.toString();
	
	}

}