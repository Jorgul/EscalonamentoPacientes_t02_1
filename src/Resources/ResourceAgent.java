package Resources;
import java.util.ArrayList;

import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;

// classe do agente
public class ResourceAgent extends Agent {
	
	private Resource resourceInfo = new Resource();

   // classe do behaviour
   class ResourceBehaviour extends SimpleBehaviour {
      private int n = 0;
      private Resource resourceInfo = new Resource();
      private boolean replyToFlag = false;

      // construtor do behaviour
      public ResourceBehaviour(Agent a, Resource resourceInfo) {
         super(a);
         this.resourceInfo = resourceInfo;
         
         //TODO para testar
         ArrayList<ResourceArea> newCapabilities = new ArrayList<ResourceArea>();
         ResourceArea newResourceArea1 = new ResourceArea("Oncology", 120);
         ResourceArea newResourceArea2 = new ResourceArea("Diagnostics", 60);
         ResourceArea newResourceArea3 = new ResourceArea("General", 90);
         newCapabilities.add(newResourceArea1);
         newCapabilities.add(newResourceArea2);
         newCapabilities.add(newResourceArea3);
         this.resourceInfo.setCapabilities(newCapabilities);
         //------------------
      }
      
      public Resource getResourceInfo()
      {
    	  return this.resourceInfo;
      }

      // método action
      public void action() {
         ACLMessage msg = blockingReceive();
         if(msg.getPerformative() == ACLMessage.INFORM) {
            System.out.println(++n + " " + getLocalName() + ": recebi " + msg.getContent());
            // cria resposta
            ACLMessage reply = msg.createReply();
            // preenche conteúdo da mensagem
            
            switch(msg.getContent())  // processes INFORM messages received
            {
	            case "treatments": // Message received: "treatments"
	            {
	            	String treatments = "treatments: ";
	            	for(int i=0; i < this.resourceInfo.getCapabilities().size(); i++)
	            	{
	            		treatments += this.resourceInfo.getCapabilities().get(i).getExpertise()+"_,_";
	            	}
	            	reply.setContent(treatments);
	            	this.replyToFlag = true;
	            	break;
	            }
	            case "confirm":
	            {
	            	//TODO update the patient(being treated) stats
	            	break;
	            }
	            case "reject":
	            {
	            	break;
	            }
	            default: 
	            {
	            	/*
	            	if(msg.getContent().substring(0, 9).equals("subscribe"))
	            	{
	            		String expertiseWanted= msg.getContent().substring(10);
		            	String numMinutesExam = "numMinutesExam: ";
		            	for(int i=0; i < this.resourceInfo.getCapabilities().size(); i++)
		            	{
		            		if(this.resourceInfo.getCapabilities().get(i).getExpertise().equals(expertiseWanted))
		            		{
		            			numMinutesExam += this.resourceInfo.getCapabilities().get(i).getNumMinutesExam();
		            		}
		            	}
		            	reply.setContent(numMinutesExam);
	            		System.out.println("expertise subscribed: " + msg.getContent().substring(10));
	            		this.replyToFlag = true;
	            		break;
	            	}
	            	else */if(msg.getContent().substring(0, 13).equals("availableTime"))
	            	{
	            		System.out.println("availbleTime: " + msg.getContent().substring(14));
	            	}
	            	else if(msg.getContent().substring(0, 3).equals("bid"))
	            	{
	            		System.out.println("bid: " + msg.getContent().substring(4));
	            	}
	            	else if(msg.getContent().substring(0, 12).equals("startingTime"))
	            	{
	            		System.out.println("patientID: " + msg.getContent().substring(14));
	            	}
	            	break;
	            }
            }
            // envia mensagem
            if(this.replyToFlag)
            {
	            System.out.println(reply);
	            send(reply);
	            this.replyToFlag = false;
            }
         }
         else if(msg.getPerformative() == ACLMessage.SUBSCRIBE) {
        	 System.out.println(++n + " " + getLocalName() + ": recebi " + msg.getContent());
             // cria resposta
             ACLMessage reply = msg.createReply();
        	 if(msg.getContent().substring(0, 9).equals("subscribe")) {
        		 String expertiseWanted= msg.getContent().substring(10);
            	String numMinutesExam = "numMinutesExam: ";
            	for(int i=0; i < this.resourceInfo.getCapabilities().size(); i++)
            	{
            		if(this.resourceInfo.getCapabilities().get(i).getExpertise().equals(expertiseWanted))
            		{
            			numMinutesExam += this.resourceInfo.getCapabilities().get(i).getNumMinutesExam();
            		}
            	}
            	reply.setContent(numMinutesExam);
	     		System.out.println("expertise subscribed: " + msg.getContent().substring(10));
	     		this.replyToFlag = true;
         	 }
        	// envia mensagem
             if(this.replyToFlag)
             {
 	            System.out.println(reply);
 	            send(reply);
 	            this.replyToFlag = false;
             }
         }
         else if(msg.getPerformative() == ACLMessage.REQUEST) {
        	 System.out.println(++n + " " + getLocalName() + ": recebi " + msg.getContent());
             // cria resposta
             ACLMessage reply = msg.createReply();
             
             // envia mensagem
             if(this.replyToFlag)
             {
 	            System.out.println(reply);
 	            send(reply);
 	            this.replyToFlag = false;
             }
         }
      }

      // método done
      public boolean done() {
         return n==10;
      }

   }

   // método setup
   protected void setup() {
      String tipo = "";
      // obtém argumentos
      Object[] args = getArguments();
      if(args != null && args.length > 0) {
         tipo = (String) args[0];
      } else {
         System.out.println("Não especificou o tipo");
      }
      
      // regista agente no DF
      DFAgentDescription dfd = new DFAgentDescription();
      dfd.setName(getAID());
      ServiceDescription sd = new ServiceDescription();
      sd.setName(getName());
      sd.setType("Agente " + tipo);
      dfd.addServices(sd);
      try {
         DFService.register(this, dfd);
      } catch(FIPAException e) {
         e.printStackTrace();
      }

      // cria behaviour
      ResourceBehaviour b = new ResourceBehaviour(this, this.resourceInfo);
      addBehaviour(b);
      this.resourceInfo = b.getResourceInfo();
	  
      // toma a iniciativa se for agente "pong"
      if(tipo.equals("pong")) {
         // pesquisa DF por agentes "ping"
         DFAgentDescription template = new DFAgentDescription();
         ServiceDescription sd1 = new ServiceDescription();
         sd1.setType("Agente ping");
         template.addServices(sd1);
         try {
            DFAgentDescription[] result = DFService.search(this, template);
            // envia mensagem "pong" inicial a todos os agentes "ping"
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            for(int i=0; i<result.length; ++i)
               msg.addReceiver(result[i].getName());
            msg.setContent("pong");
            send(msg);
         } catch(FIPAException e) { e.printStackTrace(); }
      }

   }   // fim do metodo setup

   // método takeDown
   protected void takeDown() {
      // retira registo no DF
      try {
         DFService.deregister(this);  
      } catch(FIPAException e) {
         e.printStackTrace();
      }
   }

}

