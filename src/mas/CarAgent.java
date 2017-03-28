package mas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class CarAgent extends Agent{
	private int start = 0, finish = 0;
	private int currentP = -1;
	private Map<Integer, String> hashMap = new HashMap<Integer, String>();
	private AID[] roadAgents;
	private long startTime, finishTime;
	
	protected void setup() {
		System.out.println("Hello! Car " + getAID().getLocalName() + " is ready to go!");
		
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			start = (Integer) args[0];
			finish = (Integer) args[1];
			System.out.println(getAID().getLocalName() + ": Start is " + start + " vertex, finish is " + finish + " vertex.");
			
			
			addBehaviour(new OneShotBehaviour(){
				public void action()
				{
					//»щем короткий путь
					System.out.println(getAID().getLocalName()+": Search the shortest path...");
					GraphPath<Integer, DefaultWeightedEdge> gp = DijkstraShortestPath.findPathBetween(City.city, start, finish);
					System.out.println(getAID().getLocalName()+": Shortest path found.");
					List<DefaultWeightedEdge> listPath = gp.getEdgeList();
					
					//»щем агентов с такими же именами, как дуги в кратчайшем пути
					DFAgentDescription template = new DFAgentDescription();
					ServiceDescription sd = new ServiceDescription();
					sd.setType("traffic");
					template.addServices(sd);
					try {
						DFAgentDescription[] result = DFService.search(myAgent, template); 
						//System.out.println("Found the following roads:");
						roadAgents = new AID[listPath.size()];
						int j = 0;
						String res = "";
						for(DefaultWeightedEdge e : listPath){
							for (int i = 0; i < result.length; ++i) {
								if(result[i].getName().getLocalName().equals("e"+City.city.getEdgeSource(e)+"_"+City.city.getEdgeTarget(e))){
									roadAgents[j] = result[i].getName();
									res+= " -> " + result[i].getName().getLocalName();
									j++;
									break;
								}
							}
						}
						System.out.println(getAID().getLocalName()+": Searched road agents from path" + res);
						startTime = System.currentTimeMillis();
						Request(0);
					}
					catch (FIPAException fe) {
						fe.printStackTrace();
					}
				}
			});
			
			addBehaviour(new CyclicBehaviourForCar());
			
		}
		else {
			// Make the agent terminate
			System.out.println("No start and finish vertices.");
			doDelete();
		}
	}
	
	private void Request(int numberRoad)
	{
		System.out.println(getAID().getLocalName()+": Request, " + numberRoad);
		ACLMessage req = new ACLMessage( ACLMessage.REQUEST );
		if(numberRoad < 1){
	        req.setContent("start");
	        req.addReceiver(roadAgents[numberRoad]);
	        send(req);
		}
		else {
			req.setContent(roadAgents[numberRoad-1].getLocalName());
	        req.addReceiver(roadAgents[numberRoad]);
	        send(req);
		}
	}
	
	
	private class CyclicBehaviourForCar extends CyclicBehaviour{

		@Override
		public void action() {
			ACLMessage msg = receive();

            if (msg != null) {
                if (msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                	System.out.println(getAID().getLocalName()+": Accept from road " + msg.getSender().getLocalName());
                	currentP++;
                	System.out.println(getAID().getLocalName()+": " + msg.getContent());
                	int time = Integer.parseInt(msg.getContent());
                    try {
						Thread.sleep(time);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    if(currentP < roadAgents.length-1)
                    	Request(currentP+1);
                    else 
                    {
                    	ACLMessage inform = new ACLMessage( ACLMessage.INFORM);
            			inform.setContent("bye");
            	    	inform.addReceiver(msg.getSender());
            			send(inform);
                    	end();
                    }
                }
                else if (msg.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
                	System.out.println(getAID().getLocalName()+": Reject from road " + msg.getSender().getLocalName());
                	//block(1000);
                	try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    Request(currentP+1);
                }
                else {
                    System.out.println("Unexpected message: " + msg );
                }
            }
            else {
                // if no message is arrived, block the behaviour
                block();
            }
			
		}
		
		public int end()
		{
			//TODO: вывод статистики
			finishTime = System.currentTimeMillis()-startTime;
			
			System.out.println(getAID().getLocalName()+": Goodbye! Time: "+(finishTime)+ " ms");
			myAgent.doDelete();
			return super.onEnd();
		}
	}
}
