package mas;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.jgrapht.graph.DefaultWeightedEdge;

import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class RoadAgent extends Agent{
	private double weight = 0;
	private Queue<AID> queue = new PriorityQueue<AID>();
	private List<AID> carsRequest = new ArrayList<AID>();
//	public String edge;
	
	
	protected void setup() {
		System.out.println("Hello! Road " + getAID().getLocalName() + " is ready!");
		
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			weight = (Double) args[0];
		//	edge = (String) args[1];
			
			//Регистрация агентов
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());
			ServiceDescription sd = new ServiceDescription();
			sd.setType("traffic");
			sd.setName("MAS-Traffic");
			dfd.addServices(sd);
			try {
				DFService.register(this, dfd);
			}
			catch (FIPAException fe) {
				fe.printStackTrace();
			}
			
			addBehaviour(new CyclicBehaviourForRoad());			
			
		}
		else {
			// Make the agent terminate
			System.out.println("No arguments.");
			doDelete();
		}
	}
	
	private class CyclicBehaviourForRoad extends CyclicBehaviour{

		@Override
		public void action() {
			ACLMessage msg = receive();
			if (msg != null) {
				//если получили запрос от машины
				if(msg.getPerformative() == ACLMessage.REQUEST)
				{
					//первая дуга в пути
					if(msg.getContent().equals("start")){
						//принятие
						System.out.println(getAID().getLocalName()+": first");
						Accept(msg.getSender(), weight);
						queue.add(msg.getSender());
					}
					else{	
						//дорога не загружена
						if(queue.size()<2)
		                {
							carsRequest.add(msg.getSender());
		            	    // запрос к другой дороге, чтобы "забрать" машину
		            	   	DFAgentDescription template = new DFAgentDescription();
			           		ServiceDescription sd = new ServiceDescription();
			           		sd.setType("traffic");
			           		template.addServices(sd);
			           		try {
			           			DFAgentDescription[] result = DFService.search(this.myAgent, template); 
			           			for (int i = 0; i < result.length; ++i) {
			           				if(result[i].getName().getLocalName().equals(msg.getContent())){
			           					
			           					//посылаем сообщение
			           					System.out.println(getAID().getLocalName()+": Propose for " + result[i].getName().getLocalName());
			                        	ACLMessage req = new ACLMessage( ACLMessage.PROPOSE);
			           					req.setContent(msg.getSender().getLocalName());
			           				    req.addReceiver(result[i].getName());
			           				    send(req);
		           						
			           					break;
		           					}
		           				}
			           		}	
			           		catch (FIPAException fe) {
			           			fe.printStackTrace();
			           		}
		                }
						else //дорога загружена
		            	   Reject(msg.getSender());
					}
					}
					//запрос от дороги
					if(msg.getPerformative() == ACLMessage.PROPOSE){
						System.out.println(getAID().getLocalName()+": Propose from " + msg.getSender().getLocalName());
						for(AID agent : queue){
							if(agent.getLocalName().equals(msg.getContent()))
							{
								System.out.println(getAID().getLocalName()+": Accept for " + agent.getLocalName());
								ACLMessage accept = new ACLMessage( ACLMessage.ACCEPT_PROPOSAL);
		       					accept.setContent(msg.getContent());
		       				    accept.addReceiver(msg.getSender());
		       				    send(accept);
		       				    queue.remove(agent);
		       				    break;
							}
						}
					}
					//ответ машине
					if(msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL){
						AID agent = null;
						for(AID a : carsRequest){
							if(a.getLocalName().equals(msg.getContent())){
								agent = a;
								break;
							}
						}
						carsRequest.remove(agent);
						queue.add(agent);
						Accept(agent, weight);						
					}
					if(msg.getPerformative() == ACLMessage.INFORM){
						queue.remove(msg.getSender());
					}
			}
			
            else {
                // if no message is arrived, block the behaviour
                block();
            }
	}
	public void Reject(AID receiver){
		System.out.println(getAID().getLocalName()+": Reject for " + receiver.getLocalName() + ", size of queue = " + queue.size());
		ACLMessage reject = new ACLMessage( ACLMessage.REJECT_PROPOSAL );
		reject.setContent("wait");
    	reject.addReceiver(receiver);
		send(reject);
	}
	
	public void Accept(AID receiver, double value){
		ACLMessage accept = new ACLMessage( ACLMessage.ACCEPT_PROPOSAL );
		accept.setContent(""+((int) value*1000));
    	accept.addReceiver(receiver);
		send(accept);
	}
}
}


