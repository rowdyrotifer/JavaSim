package com.marklalor.javasim.model.flow;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.builder.DirectedWeightedGraphBuilderBase;

import com.marklalor.javasim.functions.identifier.single.DataIdentifier;
import com.marklalor.javasim.model.flow.edge.FlowEdge;
import com.marklalor.javasim.model.flow.node.DataNode;

public class FunctionFlowBuilder {
	private final Set<DataIdentifier> inputs;
	private final List<DataNode> currentBundles;
	private final List<DataBundleNodeLinker> linkedBundles;
	
	public FunctionFlowBuilder() {
		this.bundles = new ArrayList<>();
	}
	
	public DirectedWeightedGraphFunctionFlow buildGraphFlow() {
		DirectedWeightedGraphBuilderBase<Object, FlowEdge, ? extends SimpleDirectedWeightedGraph<Object, FlowEdge>, ?> builder = SimpleDirectedWeightedGraph.builder(FlowEdge.class);

	}
}
