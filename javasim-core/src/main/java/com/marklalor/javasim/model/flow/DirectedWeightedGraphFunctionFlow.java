package com.marklalor.javasim.model.flow;

import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import com.marklalor.javasim.model.flow.edge.FlowEdge;
import com.marklalor.javasim.model.flow.node.DataNode;

public class DirectedWeightedGraphFunctionFlow implements GraphFunctionFlow {
	private final SimpleDirectedWeightedGraph<DataNode, FlowEdge> graph;
	
	public DirectedWeightedGraphFunctionFlow(SimpleDirectedWeightedGraph<DataNode, FlowEdge> graph) {
		this.graph = graph;
	}
	
	@Override
	public Graph<DataNode, FlowEdge> getGraph() {
		return this.graph;
	}

}
