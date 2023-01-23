package organicFarming;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.nzdis.micro.AbstractRole;
import org.nzdis.micro.MTConnector;
import org.nzdis.micro.SystemAgentLoader;
import org.sofosim.environment.GridSim;
import org.sofosim.environment.annotations.SimulationParam;
import org.sofosim.environment.stats.Statistics;
import org.sofosim.util.RandomHelper;

import organicFarming.artefacts.SanctioningType;
import organicFarming.artefacts.SvoTypes;
import organicFarming.graph.GraphGenerator;
import organicFarming.roles.Certifier;
import organicFarming.roles.Inspector;
import organicFarming.roles.Operator;
import organicFarming.roles.OrganicTradingRole;
import organicFarming.roles.Roles;
import sim.engine.RandomSequence;
import sim.engine.Steppable;

public class Simulation extends GridSim {
    
    /**
     * Reference to simulation configuration
     */
    @SimulationParam
    public SimulationSetup setup = new SimulationSetup();
    

    public Simulation(Statistics stats, long seed) {
        super(stats, seed);
        MTConnector.setAnonymousAgentNamePrefix("Agent_");
    }
    
    /**
     * Sequences of Mason-scheduled agents;
     */
    RandomSequence randomSequence = null;
    
    /**
     * Directory of all agents
     */
    public LinkedHashMap<String, OrganicTradingRole> agentDirectory = new LinkedHashMap<>();
    
    /**
     * Certifier directory (for quick use)
     */
    public LinkedHashMap<String, Certifier> certifierDirectory = new LinkedHashMap<>();
   
    /**
     * Operator directory (for quick use)
     */
    public LinkedHashMap<String, Operator> operatorDirectory = new LinkedHashMap<>();
    
    /**
     * Inspector directory (for quick use)
     */
    public LinkedHashMap<String, Inspector> inspectorDirectory = new LinkedHashMap<>();
    
    /**
     * Spawns a given number of new agents.
     * @param numberOfAgents
     */
    public void spawnNewAgents(int numberOfAgents, Class clazz){
        System.out.println("Spawning " + numberOfAgents + " agents of type " + clazz.getName());
        ArrayList<Steppable> allAgents = new ArrayList<>();
        
        if (SimulationSetup.CHEATING_FIRST_ORDER) {
        	System.out.println("Cheater fraction: " + SimulationSetup.CHEATER_FRACTION 
        			+ ", Probability: " + SimulationSetup.PROBABILITY_CHEATING
        			//+ ", 2nd-order cheating: " +  SimulationSetup.CHEATING_SECOND_ORDER
        			);
        } else {
        	System.out.println("No cheating");
        }
        
        int cheaterCt = 0;
        
		Boolean cheater = false;
		
		CertifierRNG certifierRng = new CertifierRNG();
		InspectorRNG inspectorRng = new InspectorRNG();
		RandomHelper.setRNG(random());
		Certifier certifier = null;
		AbstractRole actor = null;
        Constructor<?> c = null;
        
        for (int x = 0; x < numberOfAgents; x++) {
        	
        	// Determine cheating and other aspects per individual
        	if (SimulationSetup.CHEATING_FIRST_ORDER) {
            	cheater = random().nextBoolean(SimulationSetup.CHEATER_FRACTION);
            	if (cheater) {
            		System.out.println("Cheater");
            		cheaterCt++;
            	}
            }
        	
        	certifier = null;
    		actor = null;
            c = null;
            
            // Find constructor and instantiate agents based on input class
            
            try {
            
	            switch (clazz.getSimpleName()) {
		            case Roles.ROLE_CERTIFIER:
	                	
	                	// Determine number of clients
	                	Integer maxOperators = certifierRng.nextInt(random());
	                    
	                	c = clazz.getDeclaredConstructor(this.getClass(), cheater.getClass(), maxOperators.getClass());
	                    c.setAccessible(true);
	                    actor = (AbstractRole) c.newInstance(new Object[] {this, cheater, maxOperators});
	                	
	                    SystemAgentLoader.newAgent(actor, clazz.getSimpleName() + "_", true);
	                    agentDirectory.put(actor.me(), (OrganicTradingRole) actor);
	                	certifierDirectory.put(actor.me(), (Certifier) actor);
	                    break;
	                case Roles.ROLE_OPERATOR:
	                		                	
	                	boolean exceeding = true;
	                	int itCt = 0;
	                	while (exceeding) {
	                		certifier = (Certifier) RandomHelper.getRandomElement(certifierDirectory.values());
	                		if (certifier.getPreassignedOperators().size() < certifier.getMaxOperators()) {
	                			exceeding = false;
	                			break; // to prevent unnecessary capturing by ct count
	                		}
	                		itCt++;
	                		if (itCt > 30) {
	                			System.err.println("Could not find certifier with sufficient capacity for operator within 30 rounds.");
	                		}
	                	}
	                	
	                	if (certifier == null) {
	                		throw new RuntimeException("Certifier should never be null at this stage.");
	                	}
	                	
                    	//System.out.println(DataStructurePrettyPrinter.decomposeRecursively(clazz.getConstructors(), null));
                        c = clazz.getDeclaredConstructor(this.getClass(), cheater.getClass(), certifier.getClass());
                        c.setAccessible(true);
                        actor = (AbstractRole) c.newInstance(new Object[] {this, cheater, certifier});
	                    
                        SystemAgentLoader.newAgent(actor, clazz.getSimpleName() + "_", true);
                        agentDirectory.put(actor.me(), (OrganicTradingRole) actor);
                        operatorDirectory.put(actor.me(), (Operator) actor);
	                    
                        // Assign operator to certifier
	                    certifier.addPreassignedOperator((Operator) actor);	                    
	                    break;
	                case Roles.ROLE_INSPECTOR:
	                	
	                	c = clazz.getDeclaredConstructor(this.getClass(), cheater.getClass());
                        c.setAccessible(true);
                        actor = (AbstractRole) c.newInstance(new Object[] {this, cheater});
	                	
                        SystemAgentLoader.newAgent(actor, clazz.getSimpleName() + "_", true);
                        agentDirectory.put(actor.me(), (OrganicTradingRole) actor);
	                    inspectorDirectory.put(actor.me(), (Inspector) actor);
	                    
	                    int numberOfCertifiers = 0;
	                    // Assign inspectors to certifier
	                    if (setup.MEAN_INSPECTORS_PER_CERTIFIER != -1) {
	                    	System.out.println("Using randomised Certifier-Inspector ratio");
	                    	RandomHelper.setRNG(random());
	                    	// Random call around mean value
	                    	RandomHelper.getRandomNormal(0, setup.MEAN_INSPECTORS_PER_CERTIFIER * 2);
	                    } else {
	                    	System.out.println("Using empirical Certifier-Inspector distribution");
	                    	// Distribution based on Saba's mail from 20181031
	                    	numberOfCertifiers = inspectorRng.nextDiscreteInt(random());
	                    }
	                    		//Math.round(RandomHelper.getRandomNormal(1f, setup.MEAN_INSPECTORS_PER_CERTIFIER * 2f - 1f));
	                    for (int i = 0; i < numberOfCertifiers; i++) {
	                    	// Get random certifier (without duplicates)
	                    	boolean alreadyAssignedToCertifier = true;
	                    	while (alreadyAssignedToCertifier) {
	                    		certifier = (Certifier) RandomHelper.getRandomElement(new ArrayList<Certifier>(certifierDirectory.values()), 
	                    			((Inspector)actor).associatedCertifiers);
	                    		alreadyAssignedToCertifier = certifier.getPreassignedInspectors().contains((Inspector) actor);
	                    	}
	                    	// Add both inspector to certifier and vice versa
	                    	((Inspector)actor).addPreassignedCertifier(certifier);
	                    	certifier.addPreassignedInspector((Inspector) actor);
	                    }
	                    break;    
	                default: throw new RuntimeException("Input class " + clazz.getSimpleName() + " not found.");
	            }
                    
            } catch (NoSuchMethodException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            // Include agent in scheduling
            allAgents.add((Steppable) actor);
        }
        if (randomSequence == null) {
            randomSequence = new RandomSequence(allAgents.toArray(new Steppable[allAgents.size()]), true);
            //not reliable when removing agents - avoid if removing agents at runtime!
            //randomSequence.setUsesSets(true);
            schedule.scheduleRepeating(randomSequence);
        } else {
            randomSequence.addSteppables(allAgents);
        }
        System.err.println("Number of cheaters: " + cheaterCt);
    }
    
    /**
     * Deregisters a given agent from the directory.
     * @param agent
     */
    public void deregisterAgent(String agent){
        if (randomSequence != null) {
            randomSequence.removeSteppable((Steppable)agentDirectory.remove(agent));
            operatorDirectory.remove(agent);
            certifierDirectory.remove(agent);
        }
    }
    
    @Override
    public void start() {
        super.start();
        
        // Initialising helper mechanisms
        RandomHelper.setRNG(random());
        //SimulationSetup.load();
        
        // Start scenario       
        spawnNewAgents(SimulationSetup.NUMBER_OF_CERTIFIERS, Certifier.class);
        spawnNewAgents(SimulationSetup.NUMBER_OF_OPERATORS, Operator.class);
        spawnNewAgents(SimulationSetup.NUMBER_OF_INSPECTORS, Inspector.class);
        
        // Assign SVO
        // Distributions proportional to survey results
        for (OrganicTradingRole role: agentDirectory.values()) {
        	role.setSVO(RandomHelper.getRandomElementWithWeightedInput(SvoTypes.SVOs, 
        			setup.SVO_INDIVIDUALISTIC_FRACTION, 
        			setup.SVO_PROSOCIAL_FRACTION, 
        			setup.SVO_COMPETITIVE_FRACTION));
        }
        
        // Assign Sanctioning patterns
        for (Certifier role: certifierDirectory.values()) {
        	role.setSanctioningType(RandomHelper.getRandomElementWithWeightedInput(SanctioningType.sanctioningTypes, 
        			setup.CERTIFIER_FRACTION_SANCTIONING_SOFT, 
        			setup.CERTIFIER_FRACTION_SANCTIONING_MEDIUM, 
        			setup.CERTIFIER_FRACTION_SANCTIONING_HARD));
        }
        
        getStatistics().step(this);
        
        new GraphGenerator().generate(this).display();
    }
    
    @Override
    public void finish() {
        // TODO Auto-generated method stub
        super.finish();
    }

}

