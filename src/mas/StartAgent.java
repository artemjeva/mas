package mas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jgrapht.graph.DefaultWeightedEdge;

import jade.core.*;
import jade.core.behaviours.TickerBehaviour;
import jade.wrapper.AgentController;
import jade.wrapper.PlatformController;

public class StartAgent extends Agent {
	public static List<RoadAgent> listRA = new ArrayList<RoadAgent>();

	protected void setup() {
		System.out.println("Starting...");
		City.DoCity();
		try {
			for (DefaultWeightedEdge e : City.edges) {
				// create a new agent
				String localName = "e" + City.city.getEdgeSource(e) + "_" + City.city.getEdgeTarget(e);
				PlatformController container = getContainerController();
				Object[] args = new Object[3];
				args[0] = City.city.getEdgeWeight(e);
				args[1] = City.city.getEdgeWeight(e) * 2;// maxSize is weight
															// (we mean time) *
															// 2
				args[2] = 0;// there are no cars on the roads
				AgentController road = container.createNewAgent(localName, "mas.RoadAgent", args);
				road.start();
			}

			Thread.sleep(2000);

			int allCars = 30;
			for (int j = 0; j < allCars; j++) {
				String localName = "car" + j;
				PlatformController container = getContainerController();
				Object[] args = new Object[2];
				args[0] = (Integer) 1;
				args[1] = (Integer) 16;
				AgentController car = container.createNewAgent(localName, "mas.CarAgent", args);
				car.start();
				Thread.sleep(1000);
			}

			addBehaviour(new TickerBehaviour(this, 5000) {
				protected void onTick() {
					System.out.println("Amount of finished cars: " + Statistics.stat.size());

					if (Statistics.stat.size() == allCars) {
						HashMap<String, Long> sortedMap = Statistics.sortByValues(Statistics.stat);
						long min = sortedMap.get(sortedMap.keySet().toArray()[0]);
						String minCarName = sortedMap.keySet().toArray()[0].toString();
						long max = sortedMap.get(sortedMap.keySet().toArray()[sortedMap.size() - 1]);
						String maxCarName = sortedMap.keySet().toArray()[sortedMap.size() - 1].toString();
						long sum = 0;
						for (int i = 0; i < sortedMap.size(); i++) {
							sum += sortedMap.get(sortedMap.keySet().toArray()[i]);
						}

						System.out.println("Statictics:");
						System.out.println("Min time: " + min + ", car: " + minCarName);
						System.out.println("Max time: " + max + ", car: " + maxCarName);
						System.out.println("Total time: " + sum + ", All cars: " + allCars);

						for (int i = 0; i < allCars; i++) {
							System.out.println("Car: " + sortedMap.keySet().toArray()[i].toString() + ", time: "
									+ sortedMap.get(sortedMap.keySet().toArray()[i]).toString());
						}

						doDelete();
					}
				}

			});
		} catch (Exception e) {
			System.err.println("Exception while adding roads: " + e);
			e.printStackTrace();
		}
	}
}
