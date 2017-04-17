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
		
		
		DefaultWeightedEdge e1_2 = city.addEdge(1, 2);
		city.setEdgeWeight(e1_2, 5);
		edges.add(e1_2);
		
		DefaultWeightedEdge e2_1 = city.addEdge(2, 1);
		city.setEdgeWeight(e2_1, 5);
		edges.add(e2_1);
		
		DefaultWeightedEdge e2_3 = city.addEdge(2, 3);
		city.setEdgeWeight(e2_3, 4);
		edges.add(e2_3);
		
		DefaultWeightedEdge e3_2 = city.addEdge(3, 2);
		city.setEdgeWeight(e3_2, 4);
		edges.add(e3_2);
		
		DefaultWeightedEdge e3_16 = city.addEdge(3, 16);
		city.setEdgeWeight(e3_16, 5);
		edges.add(e3_16);
		
		DefaultWeightedEdge e16_3 = city.addEdge(16, 3);
		city.setEdgeWeight(e16_3, 5);
		edges.add(e16_3);
		
		DefaultWeightedEdge e1_4 = city.addEdge(1, 4);
		city.setEdgeWeight(e1_4, 7);
		edges.add(e1_4);
		
		DefaultWeightedEdge e4_1 = city.addEdge(4, 1);
		city.setEdgeWeight(e4_1, 7);
		edges.add(e4_1);
		
		DefaultWeightedEdge e4_5 = city.addEdge(4, 5);
		city.setEdgeWeight(e4_5, 6);
		edges.add(e4_5);
		
		DefaultWeightedEdge e5_4 = city.addEdge(5, 4);
		city.setEdgeWeight(e5_4, 6);
		edges.add(e5_4);
		
		DefaultWeightedEdge e5_6 = city.addEdge(5, 6);
		city.setEdgeWeight(e5_6, 6);
		edges.add(e5_6);
		
		DefaultWeightedEdge e6_5 = city.addEdge(6, 5);
		city.setEdgeWeight(e6_5, 6);
		edges.add(e6_5);
		
		DefaultWeightedEdge e6_16 = city.addEdge(6, 16);
		city.setEdgeWeight(e6_16, 3);
		edges.add(e6_16);
		
		DefaultWeightedEdge e1_7 = city.addEdge(1, 7);
		city.setEdgeWeight(e1_7, 8);
		edges.add(e1_7);
		
		DefaultWeightedEdge e7_1 = city.addEdge(7, 1);
		city.setEdgeWeight(e7_1, 8);
		edges.add(e7_1);
		
		DefaultWeightedEdge e7_8 = city.addEdge(7, 8);
		city.setEdgeWeight(e7_8, 6);
		edges.add(e7_8);
		
		DefaultWeightedEdge e8_7 = city.addEdge(8, 7);
		city.setEdgeWeight(e8_7, 6);
		edges.add(e8_7);
		
		DefaultWeightedEdge e8_9 = city.addEdge(8, 9);
		city.setEdgeWeight(e8_9, 5);
		edges.add(e8_9);
		
		DefaultWeightedEdge e9_8 = city.addEdge(9, 8);
		city.setEdgeWeight(e9_8, 5);
		edges.add(e9_8);
		
		DefaultWeightedEdge e9_10 = city.addEdge(9, 10);
		city.setEdgeWeight(e9_10, 2);
		edges.add(e9_10);
		
		DefaultWeightedEdge e10_9 = city.addEdge(10, 9);
		city.setEdgeWeight(e10_9, 2);
		edges.add(e10_9);
		
		DefaultWeightedEdge e10_16 = city.addEdge(10, 16);
		city.setEdgeWeight(e10_16, 4);
		edges.add(e10_16);
		
		DefaultWeightedEdge e16_10 = city.addEdge(16, 10);
		city.setEdgeWeight(e16_10, 4);
		edges.add(e16_10);
		
		DefaultWeightedEdge e1_11 = city.addEdge(1, 11);
		city.setEdgeWeight(e1_11, 10);
		edges.add(e1_11);
		
		DefaultWeightedEdge e11_1 = city.addEdge(11, 1);
		city.setEdgeWeight(e11_1, 10);
		edges.add(e11_1);
		
		DefaultWeightedEdge e11_12 = city.addEdge(11, 12);
		city.setEdgeWeight(e11_12, 5);
		edges.add(e11_12);
		
		DefaultWeightedEdge e12_11 = city.addEdge(12, 11);
		city.setEdgeWeight(e12_11, 5);
		edges.add(e12_11);
		
		DefaultWeightedEdge e12_13 = city.addEdge(12, 13);
		city.setEdgeWeight(e12_13, 4);
		edges.add(e12_13);
		
		DefaultWeightedEdge e13_12 = city.addEdge(13, 12);
		city.setEdgeWeight(e13_12, 4);
		edges.add(e13_12);
		
		DefaultWeightedEdge e13_14 = city.addEdge(13, 14);
		city.setEdgeWeight(e13_14, 6);
		edges.add(e13_14);
		
		DefaultWeightedEdge e14_15 = city.addEdge(14, 15);
		city.setEdgeWeight(e14_15, 3);
		edges.add(e14_15);
		
		DefaultWeightedEdge e15_14 = city.addEdge(15, 14);
		city.setEdgeWeight(e15_14, 3);
		edges.add(e15_14);
		
		DefaultWeightedEdge e15_16 = city.addEdge(15, 16);
		city.setEdgeWeight(e15_16, 3);
		edges.add(e15_16);
		
		DefaultWeightedEdge e16_15 = city.addEdge(16, 15);
		city.setEdgeWeight(e16_15, 3);
		edges.add(e16_15);
		
		/*DefaultWeightedEdge e1_2 = city.addEdge(1, 2);
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
		edges.add(e7_6);*/
	}
}
