package organicFarming.graph;

import java.util.Map.Entry;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import organicFarming.Simulation;
import organicFarming.roles.Certifier;
import organicFarming.roles.Inspector;
import organicFarming.roles.Operator;

public class GraphGenerator {
	
	public static final String WEIGHT_LAYOUT = "layout.weight";
	
	public static final int WEIGHT_INSPECTOR = 50;
	
	public static final int WEIGHT_OPERATOR = 40;
	
	public static final int EDGE_WEIGHT_INSPECTOR = 20;
	
	public static final int EDGE_WEIGHT_OPERATOR = 30;
	
	public static final String STYLE_KEY = "ui.style";
	
	public static final String COLOR_RED = "fill-color: rgb(255,0,0);";
	public static final String COLOR_GREEN = "fill-color: rgb(0,255,0);";
	public static final String COLOR_BLUE = "fill-color: rgb(0,0,255);";
	
	public GraphGenerator() {
		
	}

	/**
	 * Generates graph for given input simulation.
	 * @param sim
	 * @return
	 */
	public Graph generate(Simulation sim) {
		
		Graph graph = new SingleGraph("Organic Trading Network");
		
		graph.addAttribute("layout.stabilization-limit", 0.5);
		graph.addAttribute("layout.quality", 0);
		
		System.out.println("Generating graph of network " + graph.getId());
		
		for (Entry<String, Certifier> entry: sim.certifierDirectory.entrySet()) {
			String certifierName = entry.getKey();
			Node certNode = graph.addNode(certifierName);
			certNode.addAttribute(STYLE_KEY, COLOR_RED);
			for (Operator entry2: (entry.getValue()).managedOperators) {
				String operatorName = entry2.me();
				Node operNode = graph.addNode(operatorName);
				operNode.addAttribute(WEIGHT_LAYOUT, WEIGHT_OPERATOR);
				operNode.addAttribute(STYLE_KEY, COLOR_GREEN);
				// Link between certifier and operator
				Edge edge = graph.addEdge(new StringBuffer(certifierName).append("<->").append(operatorName).toString(), certNode, operNode);
				edge.addAttribute(WEIGHT_LAYOUT, EDGE_WEIGHT_OPERATOR);
			}
		}
		
		for (Entry<String, Inspector> entry: sim.inspectorDirectory.entrySet()) {
			String inspectorName = entry.getKey();
			Node inspNode = graph.addNode(inspectorName);
			inspNode.addAttribute(WEIGHT_LAYOUT, WEIGHT_INSPECTOR);
			inspNode.addAttribute(STYLE_KEY, COLOR_BLUE);
			Inspector inspector = (Inspector) entry.getValue();
			for (Certifier certifier: inspector.associatedCertifiers) {
				// Link between operator and certifier
				Edge edge = graph.addEdge(new StringBuffer(certifier.me()).append("<->").append(inspectorName).toString(), 
						inspNode, graph.getNode(certifier.me()));
				edge.addAttribute(WEIGHT_LAYOUT, EDGE_WEIGHT_INSPECTOR);
			}
		}
		
		return graph;	
	}
	
}
