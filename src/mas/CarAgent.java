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
import jade.core.behaviours.ReceiverBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.core.behaviours.ReceiverBehaviour.NotYetReady;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import mas.CarAgent.SeqBehaviour;

public class CarAgent extends Agent{
	private int start = 0, finish = 0, start_ = 0, finish_ = 0;
	private AID currentP;// = new AID();
	private List<AID> roadAgents = new ArrayList<AID>();
	private long startTime, finishTime;
	private int timeRide = 0;
	public SeqBehaviour sb;
	
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
			sb = new SeqBehaviour(this);
			addBehaviour(sb);
			
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
	
	private class WaitBehaviour extends WakerBehaviour{

		public WaitBehaviour(Agent a, long period) {
			super(a, period);
			// TODO Auto-generated constructor stub
		}
		protected void onWake() {
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

	private class BeforeRideBehaviour extends OneShotBehaviour{
		private ReceiverBehaviour.Handle h;
		private ACLMessage msg;
		public BeforeRideBehaviour(ReceiverBehaviour.Handle h_){
			h=h_;
		}
		@Override
		public void action() {
			try{
				msg = h.getMessage();
		    }
		    catch(ReceiverBehaviour.TimedOut rbte) {System.out.println("Recieve Timeout");} 
		    catch (NotYetReady e) { }
			if(msg!=null)
			{
				System.out.println(getAID().getLocalName()+": Accept from road " + msg.getSender().getLocalName());
            	currentP = msg.getSender();
            	timeRide = Integer.parseInt(msg.getContent());
            	System.out.println(getAID().getLocalName()+": I will ride " + timeRide);
				sb.addSubBehaviour(new RideBehaviour(myAgent, timeRide, msg));
			}
		}
	}
	
	private class RideBehaviour extends WakerBehaviour{
		private ACLMessage msg;;
		public RideBehaviour(Agent a, long timeout, ACLMessage m) {
			super(a, timeout);
			msg = m;			
		}	
		protected void handleElapsedTimeout()
		{	
		  //  reset(timeRide);
		    System.out.println(myAgent.getLocalName() + ": Ride.. "+timeRide+"ms");
		    if(roadAgents.size() > 1)
	            {
	            	start_ = Integer.parseInt(currentP.getLocalName().split("_")[1]);
	            	System.out.println(getAID().getLocalName()+": next vertex is " + start_);
	            	addBehaviour(new SearchBehaviour());
	            	//Request(currentP+1);
	            }	
	            else 
	            {
	            	ACLMessage inform = new ACLMessage( ACLMessage.INFORM);
	    			inform.setContent("bye");
	    	    	inform.addReceiver(currentP);
	    			send(inform);
	    			
	    			finishTime = System.currentTimeMillis()-startTime;
	    			Statistics.stat.put(getAID().getLocalName(), finishTime);
					
					System.out.println(getAID().getLocalName()+": Goodbye! Time: "+(finishTime)+ " ms");
					myAgent.doDelete();
					super.onEnd();
	            }
		}
	}
	public class SeqBehaviour extends SequentialBehaviour{
		private ACLMessage msg;
		public SeqBehaviour(Agent a){			
			super(a);

			ReceiverBehaviour.Handle h = ReceiverBehaviour.newHandle();
			
			addSubBehaviour(new ReceiverBehaviour(myAgent, h, 1000, MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL)));
			addSubBehaviour(new BeforeRideBehaviour(h));
		}
		public int onEnd() {
		    reset();
		    sb = new SeqBehaviour(myAgent);
		    myAgent.addBehaviour(sb);
		    return super.onEnd();
		  }	
	}
}
