import java.util.ArrayList;

import Patients.PatientAgent;
import Resources.ResourceAgent;

import jade.core.Runtime;
import jade.core.Profile; 
import jade.core.ProfileImpl; 
import jade.wrapper.*;


public class Main {

	/**
	 * @param args
	 */
	
	public static ArrayList<ResourceAgent> resources =  new ArrayList<ResourceAgent>();
	public static ArrayList<PatientAgent> patients =  new ArrayList<PatientAgent>();
	
	public static void main( String arg[] ) {

		  Runtime rt = Runtime.instance(); 

		  Profile p = new ProfileImpl();
		  ContainerController cc = rt.createMainContainer(p);
		  
		  //TODO final
		  /*
		  	for(int i=0; i< 6;i++) {
		    	resources.add(new Resource());
		    }
		    
		    for(int i=0; i< 6;i++) {
		    	patients.add(new Patient());
		    }
		  */
		  
		  ResourceAgent r1=new ResourceAgent();
		  
		  //Object[] para o terceiro argumento de "createNewAgent" -> corresponde aos argumentos que queremos passar no momento da criação do agente

		  try {
			  AgentController rma = cc.createNewAgent("rma", "jade.tools.rma.rma", null);
			  
			  AgentController p1 = cc.createNewAgent("p1", "Patients.PatientAgent", null);
			  
			  AgentController f1 = cc.acceptNewAgent("r1", r1);
			   
			   rma.start();
			   p1.start();
	           f1.start();
	           
	           /*
	           	ArrayList<AgentController> allControllers = new ArrayList<AgentController>();
	           	
		        for(int i=0; i< resources.size();i++) {
		            allControllers.add(cc.acceptNewAgent("r"+i, resources.get(i)));
		            allControllers.get(i).start();
		        }
		        
		        for(int i=0 ; i< patients.size();i++) {
		            allControllers.add(cc.acceptNewAgent("p"+i, patients.get(i)));
		            allControllers.get(i).start();
		        }
	           */
	           
		  } catch (StaleProxyException e) {
			   e.printStackTrace();
			  }
		       /*
		  try {
		   //AgentController dummy = cc.createNewAgent("sfdds", "agents.AgvAgent", null);
		   AgentController rma = cc.createNewAgent("rma", "jade.tools.rma.rma", null);
		   AgentController f1 = cc.acceptNewAgent("main", master);
		   AgentController p1 = cc.acceptNewAgent("player", player);

		            //AgentController p2 = cc.acceptNewAgent("playerRandom", player);

		            rma.start();
		            f1.start();
		            p1.start();

		            ArrayList<AgentController> allControllers = new ArrayList<AgentController>();
		            for(int i =0 ; i< experts.size();i++) {
		                allControllers.add(cc.acceptNewAgent("expert"+i, experts.get(i)));
		                allControllers.get(i).start();
		            }

		   
		  } catch (StaleProxyException e) {
		   e.printStackTrace();
		  } 
		  // Fire up the a*/
		  
		 }
		}

