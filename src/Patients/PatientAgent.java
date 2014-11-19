package Patients;

import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;

// classe do agente
public class PatientAgent extends Agent {
	
	private Patient patientInfo = new Patient("Diagnostics");

   // classe do behaviour
   class PatientBehaviour extends SimpleBehaviour {
      private int n = 0;
      private Patient patientInfo = new Patient();
      private boolean replyToFlag = false;

      // construtor do behaviour
      public PatientBehaviour(Agent a, Patient patientInfo) {
         super(a);
         this.patientInfo = patientInfo;
      }
      
      public Patient getPatientInfo()
      {
    	  return this.patientInfo;
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
            
	            case "startAuction":
	        	{
	        		reply.setContent("startingTime:" + this.patientInfo.getPatientID() );
	        		this.replyToFlag = true;
	            	break;
	        	}
	        	
	            case "bidCall":
	        	{
	        		reply.setContent("bid:" + this.patientInfo.getBid() + ":" + this.patientInfo.getPatientID());
	        		this.replyToFlag = true;
	            	break;
	        	}
            
            	case "winner":
            	{
            		//if() quer confirmar
            			reply.setContent("confirm");
            			this.replyToFlag = true;
            		//else if() quer rejeitar
            			//reply.setContent("reject");
	            	break;
            	}
	            
	            default: 
	            {
	            	
	            	if(msg.getContent().substring(0, 12).equals("treatments: "))
	            	{
	            		String[] expertises;
	            		System.out.println("treatments: " + msg.getContent().substring(12));
	            		if (msg.getContent().substring(12).contains("_,_")) {
	            			expertises=msg.getContent().substring(12).split("_,_");
	            			for(int i=0; i < expertises.length; i++)
	            			{
	            				if(expertises[i].equals(this.patientInfo.getPathology()))
	            				{
		            				reply.setContent("subscribe " + this.patientInfo.getPathology());
		            				this.replyToFlag = true;
		        	            	break;
	            				}
	            			}
	            		} else {
	            		    throw new IllegalArgumentException("Resource has no expertises");
	            		}
	            	}
	            	else if(msg.getContent().substring(0, 16).equals("numMinutesExam: "))
	            	{
	            		
	            	}
	            	/*else if(msg.getContent().substring(0, 3).equals("bid"))
	            	{
	            		System.out.println("bid: " + msg.getContent().substring(4));
	            	}*/
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
      PatientBehaviour b = new PatientBehaviour(this, this.patientInfo);
      addBehaviour(b);
      this.patientInfo = b.getPatientInfo();
	  
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

