package mas;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jgrapht.graph.DefaultWeightedEdge;

import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class RoadAgent extends Agent {
	private double weight = 0;
	private double startWeight = 0;
	private double maxSize = 0;
	private double currentSize = 0;
	int[] verteces = new int[2];
	private Queue<AID> queue = new PriorityQueue<AID>();
	private List<AID> carsRequest = new ArrayList<AID>();

	protected void setup() {
		System.out.println("Hello! Road " + getAID().getLocalName() + " is ready!");

		Object[] args = getArguments();
		if (args != null && args.length == 3) {
			weight = (Double) args[0];
			startWeight = (Double) args[0];
			maxSize = (Double) args[1];
			currentSize = (Integer) args[2];
			verteces = parseEdgeName(getAID().getLocalName());

			// Registration
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());
			ServiceDescription sd = new ServiceDescription();
			sd.setType("traffic");
			sd.setName("MAS-Traffic");
			dfd.addServices(sd);
			try {
				DFService.register(this, dfd);
			} catch (FIPAException fe) {
				fe.printStackTrace();
			}

			// Request from car
			addBehaviour(new CyclicBehaviour() {
				public void action() {
					ACLMessage msg = receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
					if (msg != null) {
						System.out.println("***" + getAID().getLocalName() + " : " + maxSize + "***");
						if (queue.size() < maxSize) {
							// first edge in path
							if (msg.getContent().equals("start")) {
								System.out.println(getAID().getLocalName() + ": first");
								weight = startWeight + Math.pow(((startWeight / maxSize) * currentSize), 2);
								City.city.setEdgeWeight(City.city.getEdge(verteces[0], verteces[1]), weight);
								Accept(msg.getSender(),
										City.city.getEdgeWeight(City.city.getEdge(verteces[0], verteces[1])));
								queue.add(msg.getSender());
								currentSize = queue.size();
							} else {
								carsRequest.add(msg.getSender());
								// Propose to another road
								DFAgentDescription template = new DFAgentDescription();
								ServiceDescription sd = new ServiceDescription();
								sd.setType("traffic");
								template.addServices(sd);
								try {
									DFAgentDescription[] result = DFService.search(this.myAgent, template);
									for (int i = 0; i < result.length; ++i) {
										if (result[i].getName().getLocalName().equals(msg.getContent())) {
											// send message
											System.out.println(getAID().getLocalName() + ": Propose for "
													+ result[i].getName().getLocalName() + " about "
													+ msg.getSender().getLocalName());
											ACLMessage req = new ACLMessage(ACLMessage.PROPOSE);
											req.setContent(msg.getSender().getLocalName());
											req.addReceiver(result[i].getName());
											send(req);
											break;
										}
									}
								} catch (FIPAException fe) {
									fe.printStackTrace();
								}
							}
						} else // path is NOT free
							Reject(msg.getSender());
					} else {
						// if no message is arrived, block the behaviour
						block();
					}
				}
			});
			// Propose from road
			addBehaviour(new CyclicBehaviour() {
				public void action() {
					ACLMessage msg = receive(MessageTemplate.MatchPerformative(ACLMessage.PROPOSE));
					if (msg != null) {
						// System.out.println(getAID().getLocalName()+": Propose
						// from " + msg.getSender().getLocalName());
						for (AID agent : queue) {
							if (agent.getLocalName().equals(msg.getContent())) {
								System.out.println(
										getAID().getLocalName() + ": Send agreement for " + agent.getLocalName());
								ACLMessage accept = new ACLMessage(ACLMessage.AGREE);
								accept.setContent(msg.getContent());
								accept.addReceiver(msg.getSender());
								send(accept);
								queue.remove(agent);
								currentSize = queue.size();
								weight = startWeight + Math.pow(((startWeight / maxSize) * currentSize), 2);
								City.city.setEdgeWeight(City.city.getEdge(verteces[0], verteces[1]), weight);
								break;
							}
						}
					} else {
						// if no message is arrived, block the behaviour
						block();
					}
				}
			});
			// Agreement from road
			addBehaviour(new CyclicBehaviour() {
				public void action() {
					ACLMessage msg = receive(MessageTemplate.MatchPerformative(ACLMessage.AGREE));
					if (msg != null) {
						AID agent = null;
						for (AID a : carsRequest) {
							if (a.getLocalName().equals(msg.getContent())) {
								agent = a;
								break;
							}
						}
						carsRequest.remove(agent);
						queue.add(agent);
						currentSize = queue.size();
						weight = startWeight + Math.pow(((startWeight / maxSize) * currentSize), 2);
						City.city.setEdgeWeight(City.city.getEdge(verteces[0], verteces[1]), weight);
						Accept(agent, City.city.getEdgeWeight(City.city.getEdge(verteces[0], verteces[1])));

					} else {
						// if no message is arrived, block the behaviour
						block();
					}
				}
			});
			// Information from car
			addBehaviour(new CyclicBehaviour() {
				public void action() {
					ACLMessage msg = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
					if (msg != null) {
						queue.remove(msg.getSender());
						currentSize = queue.size();
						weight = startWeight + Math.pow(((startWeight / maxSize) * currentSize), 2);
						City.city.setEdgeWeight(City.city.getEdge(verteces[0], verteces[1]), weight);
					} else {
						// if no message is arrived, block the behaviour
						block();
					}
				}
			});
		} else {
			// Make the agent terminate
			System.out.println("No arguments.");
			doDelete();
		}
	}

	public void Reject(AID receiver) {
		System.out.println(getAID().getLocalName() + ": Reject for " + receiver.getLocalName() + ", size of queue = "
				+ queue.size());
		ACLMessage reject = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
		reject.setContent("wait");
		reject.addReceiver(receiver);
		send(reject);
	}

	public void Accept(AID receiver, double value) {
		ACLMessage accept = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
		accept.setContent("" + ((int) value * 1000));
		accept.addReceiver(receiver);
		send(accept);
	}

	private int[] parseEdgeName(String edgeName) {
		int i = 0;
		int[] verteces = new int[2];
		Pattern p = Pattern.compile("[0-9]+");
		Matcher m = p.matcher(edgeName);
		while (m.find()) {
			verteces[i] = Integer.parseInt(m.group());
			i++;
		}

		return verteces;

	}
}
