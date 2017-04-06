package mas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class CarAgent extends Agent{
	private int start = 0, finish = 0, start_ = 0, finish_ = 0;
	private AID currentP;// = new AID();
	private List<AID> roadAgents = new ArrayList<AID>();
	private long startTime, finishTime;
	
	protected void setup() {
		System.out.println("Hello! Car " + getAID().getLocalName() + " is ready to go!");
		
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			start = (Integer) args[0];
			finish = (Integer) args[1];
			System.out.println(getAID().getLocalName() + ": Start is " + start + " vertex, finish is " + finish + " vertex.");
			
			start_ = start;
			finish_ = finish;
			startTime = System.currentTimeMillis();
			
			//Searching path
			addBehaviour(new SearchBehaviour());
			
			// Accept from road
			addBehaviour(new CyclicBehaviour(){
				public void action() {
					ACLMessage msg = receive(MessageTemplate.MatchPerformative( ACLMessage.ACCEPT_PROPOSAL));

		            if (msg != null) {
		                	System.out.println(getAID().getLocalName()+": Accept from road " + msg.getSender().getLocalName());
		                	currentP = msg.getSender();
		                	int time = Integer.parseInt(msg.getContent());
		                	
		                	//TODO: add behaviour
		                	//addBehaviour(new RideBehaviour(myAgent, time));
		                	//doWait or wakerBehaviour?
		                	
		                	System.out.println(myAgent.getLocalName()+": Ride... " + (time/1000) + "s");
		                	doWait(time);
		                	
		                    if(roadAgents.size() > 1)
		                    {
		                    	start_ = Integer.parseInt(msg.getSender().getLocalName().split("_")[1]);
		                    	System.out.println(getAID().getLocalName()+": next vertex is " + start_);
		                    	addBehaviour(new SearchBehaviour());
		                    	//Request(currentP+1);
		                    	//»щем новый путь
		                    }	
		                    else 
		                    {
		                    	ACLMessage inform = new ACLMessage( ACLMessage.INFORM);
		            			inform.setContent("bye");
		            	    	inform.addReceiver(msg.getSender());
		            			send(inform);
		            			
		            			finishTime = System.currentTimeMillis()-startTime;
		    					
		    					System.out.println(getAID().getLocalName()+": Goodbye! Time: "+(finishTime)+ " ms");
		    					myAgent.doDelete();
		    					super.onEnd();
		                    }
		            }
		            else {
		                // if no message is arrived, block the behaviour
		                block();
		            }
				}
			});
			//Reject from road
			addBehaviour(new CyclicBehaviour(){
				public void action(){
					ACLMessage msg = receive(MessageTemplate.MatchPerformative( ACLMessage.REJECT_PROPOSAL));

		            if (msg != null) {
		               // System.out.println(getAID().getLocalName()+": Reject from road " + msg.getSender().getLocalName());
		                this.myAgent.addBehaviour(new WaitBehaviour(this.myAgent, 1000));
		                
		            }
		            else {
		                // if no message is arrived, block the behaviour
		                block();
		            }
				}
			});
			
		}
		else {
			// Make the agent terminate
			System.out.println("No start and finish vertices.");
			doDelete();
		}
	}
	
	private void Request(int numberRoad)
	{
		//System.out.println(getAID().getLocalName()+": Request to " + roadAgents.get(numberRoad).getLocalName());
		ACLMessage req = new ACLMessage( ACLMessage.REQUEST );
		if(currentP == null){
			System.out.println(getAID().getLocalName()+": Request to " + roadAgents.get(numberRoad).getLocalName());
	        req.setContent("start");
	        req.addReceiver(roadAgents.get(numberRoad));
	        send(req);
		}
		else {
			req.setContent(currentP.getLocalName());
	        req.addReceiver(roadAgents.get(numberRoad));
	        send(req);
		}
	}
	
	private class WaitBehaviour extends TickerBehaviour{

		public WaitBehaviour(Agent a, long period) {
			super(a, period);
			// TODO Auto-generated constructor stub
		}
		protected void onTick() {
	        System.out.println(myAgent.getLocalName()+": Repeated request...");
	        Request(0);
	      } 
	}
	
	private class SearchBehaviour extends OneShotBehaviour{
		public void action()
		{
			System.out.println(getAID().getLocalName()+": Search the shortest path...");
			GraphPath<Integer, DefaultWeightedEdge> gp = DijkstraShortestPath.findPathBetween(City.city, start_, finish_);
			System.out.println(getAID().getLocalName()+": Shortest path found.");
			List<DefaultWeightedEdge> listPath = gp.getEdgeList();
			
			//Searching agents
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType("traffic");
			template.addServices(sd);
			try {
				DFAgentDescription[] result = DFService.search(myAgent, template); 
				//System.out.println("Found the following roads:");
				//roadAgents = new AID[listPath.size()];
				roadAgents.clear();
				int j = 0;
				String res = "";
				for(DefaultWeightedEdge e : listPath){
					for (int i = 0; i < result.length; ++i) {
						if(result[i].getName().getLocalName().equals("e"+City.city.getEdgeSource(e)+"_"+City.city.getEdgeTarget(e))){
							roadAgents.add(result[i].getName());
							res+= " -> " + result[i].getName().getLocalName();
							j++;
							break;
						}
					}
				}
				System.out.println(getAID().getLocalName()+": Searched road agents from path" + res);
				
				Request(0);
			}
			catch (FIPAException fe) {
				fe.printStackTrace();
			}
		}
	}
/*	
	private class RideBehaviour extends WakerBehaviour{
		public RideBehaviour(Agent a, long timeout) {
			super(a, timeout);
			doWait(timeout);
			//System.out.println(myAgent.getLocalName()+": Ride...");
		}
		protected void handleElapsedTimeout() {
	        System.out.println(myAgent.getLocalName()+": Ride...");
	      } 
	}
*/
}
