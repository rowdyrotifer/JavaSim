package com.marklalor.javasim.model.flow;

import org.jgrapht.Graph;

import com.marklalor.javasim.model.flow.edge.FlowEdge;
import com.marklalor.javasim.model.flow.node.DataNode;

public interface GraphFunctionFlow extends FunctionFlow {

	public Graph<DataNode, FlowEdge> getGraph();

}
