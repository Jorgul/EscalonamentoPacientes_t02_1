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

      // m�todo action
      public void action() {
         ACLMessage msg = blockingReceive();
         if(msg.getPerformative() == ACLMessage.INFORM) {
            System.out.println(++n + " " + getLocalName() + ": recebi " + msg.getContent());
            // cria resposta
            ACLMessage reply = msg.createReply();
            // preenche conte�do da mensagem
            
            switch(msg.getContent())  // processes INFORM messages received
            {
            
	            case "startAuction":
	        	{
	        		reply.setPerformative( ACLMessage.INFORM );
	        		reply.setContent("startingTime:" + this.patientInfo.getPatientID() );
	        		this.replyToFlag = true;
	            	break;
	        	}
	        	
	            case "bidCall":
	        	{
	        		reply.setPerformative( ACLMessage.INFORM );
	        		reply.setContent("bid:" + this.patientInfo.getBid() + ":" + this.patientInfo.getPatientID());
	        		this.replyToFlag = true;
	            	break;
	        	}
            
            	case "winner":
            	{
            		reply.setPerformative( ACLMessage.INFORM );
            		//if() quer confirmar
            			reply.setContent("confirm");
            			this.replyToFlag = true;
            		/*else if() quer rejeitar
            			reply.setContent("reject");*/
	            	break;
            	}
	            
	            default: 
	            {
	            	String[] msgContents = msg.getContent().split(":");
	            	if(msgContents[0].equals("treatments"))
	            	{
	            		String[] expertises;
	            		System.out.println("treatments: " + msgContents[1]);
	            		if (msgContents[1].contains(";")) {
	            			expertises=msgContents[1].split(";");
	            			for(int i=0; i < expertises.length; i++)
	            			{
	            				if(expertises[i].equals(this.patientInfo.getPathology()))
	            				{
	            					reply.setPerformative( ACLMessage.SUBSCRIBE );
		            				reply.setContent("subscribe:" + this.patientInfo.getPathology());
		            				this.replyToFlag = true;
		        	            	break;
	            				}
	            			}
	            		} else {
	            		    throw new IllegalArgumentException("Resource has no expertises");
	            		}
	            	}
	            	else if(msgContents[0].equals("numMinutesExam"))
	            	{
	            		
	            	}
	            	/*else if(msgContents[0].equals("bid"))
	            	{
	            		System.out.println("bid: " + msgContents[1]);
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
         else if(msg.getPerformative() == ACLMessage.REQUEST) {
        	 System.out.println(++n + " " + getLocalName() + ": recebi " + msg.getContent());
             // cria resposta
             ACLMessage reply = msg.createReply();
             
             if(msg.getContent().equals("bidCall"))
             {
            	String bid = "bid: ";
            	//TODO fun��o que calcula a aposta do paciente
            	bid += this.patientInfo.calculateBid();
            	reply.setPerformative( ACLMessage.INFORM );
            	reply.setContent(bid);
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
      }

      // m�todo done
      public boolean done() {
         return n==10;
      }

   }

   // m�todo setup
   protected void setup() {
      String tipo = "";
      // obt�m argumentos
      Object[] args = getArguments();
      if(args != null && args.length > 0) {
         tipo = (String) args[0];
      } else {
         System.out.println("N�o especificou o tipo");
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

   // m�todo takeDown
   protected void takeDown() {
      // retira registo no DF
      try {
         DFService.deregister(this);  
      } catch(FIPAException e) {
         e.printStackTrace();
      }
   }

}

