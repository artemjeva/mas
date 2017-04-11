package mas;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.graph.DefaultWeightedEdge;

import jade.core.*;
import jade.wrapper.AgentController;
import jade.wrapper.PlatformController;

public class StartAgent extends Agent{
	public static List<RoadAgent> listRA = new ArrayList<RoadAgent>();
	
	
	protected void setup() {
		System.out.println("Starting...");
		City.DoCity();
		int i = 0;
		try {
            for (DefaultWeightedEdge e : City.edges) {
                // create a new agent
            	String localName = "e"+City.city.getEdgeSource(e)+"_"+City.city.getEdgeTarget(e);
            	PlatformController container = getContainerController();
            	Object[] args = new Object[3];           	
            	args[0] = City.city.getEdgeWeight(e);
            	args[1] = City.city.getEdgeWeight(e) * 2;//maxSize is weight (we mean time) * 2
            	args[2] = 0;//there are no cars on the roads
            	AgentController road = container.createNewAgent(localName, "mas.RoadAgent", args);
            	road.start();
            }
            
            Thread.sleep(2000);
            String localName = "car_0";
			PlatformController container = getContainerController();
			Object[] args = new Object[2];
        	args[0] = (Integer) 1;
        	args[1] = (Integer) 7;
			AgentController car = container.createNewAgent(localName, "mas.CarAgent", args);
        	car.start();
      	
        	localName = "car_1";
 			container = getContainerController();
 			args = new Object[2];
         	args[0] = (Integer) 1;
         	args[1] = (Integer) 7;
 			car = container.createNewAgent(localName, "mas.CarAgent", args);
         	car.start();
         	
         	localName = "car_2";
 			container = getContainerController();
 			args = new Object[2];
         	args[0] = (Integer) 1;
         	args[1] = (Integer) 7;
 			car = container.createNewAgent(localName, "mas.CarAgent", args);
         	car.start();
         	
         	localName = "car_3";
 			container = getContainerController();
 			args = new Object[2];
         	args[0] = (Integer) 1;
         	args[1] = (Integer) 7;
 			car = container.createNewAgent(localName, "mas.CarAgent", args);
         	car.start();
         	
         	localName = "car_4";
 			container = getContainerController();
 			args = new Object[2];
         	args[0] = (Integer) 1;
         	args[1] = (Integer) 7;
 			car = container.createNewAgent(localName, "mas.CarAgent", args);
         	car.start();
         	
         	localName = "car_5";
 			container = getContainerController();
 			args = new Object[2];
         	args[0] = (Integer) 1;
         	args[1] = (Integer) 7;
 			car = container.createNewAgent(localName, "mas.CarAgent", args);
         	car.start();
         	
         	localName = "car_6";
 			container = getContainerController();
 			args = new Object[2];
         	args[0] = (Integer) 1;
         	args[1] = (Integer) 7;
 			car = container.createNewAgent(localName, "mas.CarAgent", args);
         	car.start();
         	
         	localName = "car_7";
 			container = getContainerController();
 			args = new Object[2];
         	args[0] = (Integer) 1;
         	args[1] = (Integer) 7;
 			car = container.createNewAgent(localName, "mas.CarAgent", args);
         	car.start();
         	
         	localName = "car_8";
 			container = getContainerController();
 			args = new Object[2];
         	args[0] = (Integer) 1;
         	args[1] = (Integer) 7;
 			car = container.createNewAgent(localName, "mas.CarAgent", args);
         	car.start();
         	
         	localName = "car_9";
 			container = getContainerController();
 			args = new Object[2];
         	args[0] = (Integer) 1;
         	args[1] = (Integer) 7;
 			car = container.createNewAgent(localName, "mas.CarAgent", args);
         	car.start();
         	
         	localName = "car_10";
 			container = getContainerController();
 			args = new Object[2];
         	args[0] = (Integer) 1;
         	args[1] = (Integer) 7;
 			car = container.createNewAgent(localName, "mas.CarAgent", args);
         	car.start();
         	
         	localName = "car_11";
 			container = getContainerController();
 			args = new Object[2];
         	args[0] = (Integer) 1;
         	args[1] = (Integer) 7;
 			car = container.createNewAgent(localName, "mas.CarAgent", args);
         	car.start();
         	
         	localName = "car_12";
 			container = getContainerController();
 			args = new Object[2];
         	args[0] = (Integer) 1;
         	args[1] = (Integer) 7;
 			car = container.createNewAgent(localName, "mas.CarAgent", args);
         	car.start();
         	
         	localName = "car_13";
 			container = getContainerController();
 			args = new Object[2];
         	args[0] = (Integer) 1;
         	args[1] = (Integer) 7;
 			car = container.createNewAgent(localName, "mas.CarAgent", args);
         	car.start();
         	
        }
        catch (Exception e) {
            System.err.println( "Exception while adding roads: " + e );
            e.printStackTrace();
        }
	}
}
