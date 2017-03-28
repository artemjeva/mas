package mas;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import com.jgraph.*;

public class City {
	
	public static SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge> city = new SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge>(DefaultWeightedEdge.class);
	public static List<DefaultWeightedEdge> edges = new ArrayList<DefaultWeightedEdge>();
	
	public static void DoCity()
	{
		city.addVertex(1);
		city.addVertex(2);
		city.addVertex(3);
		city.addVertex(4);
		city.addVertex(5);
		city.addVertex(6);
		city.addVertex(7);
		
		//Добавляем ребра
		DefaultWeightedEdge e1_2 = city.addEdge(1, 2);
		city.setEdgeWeight(e1_2, 3);
		edges.add(e1_2);
		
		DefaultWeightedEdge e1_3 = city.addEdge(1, 3);
		city.setEdgeWeight(e1_3, 2);
		edges.add(e1_3);
		
		DefaultWeightedEdge e2_1 = city.addEdge(2, 1);
		city.setEdgeWeight(e2_1, 3);
		edges.add(e2_1);
		
		DefaultWeightedEdge e2_4 = city.addEdge(2, 4);
		city.setEdgeWeight(e2_4, 4);
		edges.add(e2_4);
		
		DefaultWeightedEdge e2_5 = city.addEdge(2, 5);
		city.setEdgeWeight(e2_5, 6);
		edges.add(e2_5);
		
		DefaultWeightedEdge e3_1 = city.addEdge(3, 1);
		city.setEdgeWeight(e3_1, 2);
		edges.add(e3_1);
		
		DefaultWeightedEdge e3_4 = city.addEdge(3, 4);
		city.setEdgeWeight(e3_4, 3);
		edges.add(e3_4);
		
		DefaultWeightedEdge e3_6 = city.addEdge(3, 6);
		city.setEdgeWeight(e3_6, 5);
		edges.add(e3_6);
		
		DefaultWeightedEdge e4_2 = city.addEdge(4, 2);
		city.setEdgeWeight(e4_2, 4);
		edges.add(e4_2);
		
		DefaultWeightedEdge e4_3 = city.addEdge(4, 3);
		city.setEdgeWeight(e4_3, 3);
		edges.add(e4_3);
		
		DefaultWeightedEdge e4_5 = city.addEdge(4, 5);
		city.setEdgeWeight(e4_5, 7);
		edges.add(e4_5);
		
		DefaultWeightedEdge e4_6 = city.addEdge(4, 6);
		city.setEdgeWeight(e4_6, 1);
		edges.add(e4_6);
		
		DefaultWeightedEdge e5_2 = city.addEdge(5, 2);
		city.setEdgeWeight(e5_2, 6);
		edges.add(e5_2);
		
		DefaultWeightedEdge e5_4 = city.addEdge(5, 4);
		city.setEdgeWeight(e5_4, 7);
		edges.add(e5_4);
		
		DefaultWeightedEdge e5_7 = city.addEdge(5, 7);
		city.setEdgeWeight(e5_7, 5);
		edges.add(e5_7);
		
		DefaultWeightedEdge e6_3 = city.addEdge(6, 3);
		city.setEdgeWeight(e6_3, 5);
		edges.add(e6_3);
		
		DefaultWeightedEdge e6_4 = city.addEdge(6, 4);
		city.setEdgeWeight(e6_4, 1);
		edges.add(e6_4);
		
		DefaultWeightedEdge e6_7 = city.addEdge(6, 7);
		city.setEdgeWeight(e6_7, 2);
		edges.add(e6_7);
		
		DefaultWeightedEdge e7_5 = city.addEdge(7, 5);
		city.setEdgeWeight(e7_5, 5);
		edges.add(e7_5);
		
		DefaultWeightedEdge e7_6 = city.addEdge(7, 6);
		city.setEdgeWeight(e7_6, 2);
		edges.add(e7_6);
	}
}
