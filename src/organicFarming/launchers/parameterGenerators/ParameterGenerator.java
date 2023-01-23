package organicFarming.launchers.parameterGenerators;

import org.christopherfrantz.parallelLauncher.util.parameterHandling.ParameterFileGenerator;
import org.christopherfrantz.parallelLauncher.util.parameterHandling.SeedExtractor;

public class ParameterGenerator extends ParameterFileGenerator {

    /**
     * Parameter file name (used in combination with machine index specification in MetaLauncher)
     * Do not reference directly from here.
     */
    public static final String PARAMETER_FILE = "Parameters.txt";
    
    /**
     * Number of certifiers
     */
    public static final String PARAM_CERTIFIERS = "CERTIFIERS";
    
    /**
     * Number of inspectors
     */
    public static final String PARAM_INSPECTORS = "INSPECTORS";
    
    /**
     * Number of operators
     */
    public static final String PARAM_OPERATORS = "OPERATORS";
    
    /**
     * Multiplier for certifiers
     */
    public static final String PARAM_CERTIFIERS_MULTIPLIER = "CERTIFIERS_MULTIPLIER";
    
    /**
     * Multiplier for inspectors
     */
    public static final String PARAM_INSPECTORS_MULTIPLIER = "INSPECTORS_MULTIPLIER";
    
    /**
     * Multiplier for operators
     */
    public static final String PARAM_OPERATORS_MULTIPLIER = "OPERATORS_MULTIPLIER";
    
    /**
     * Distribution of certifiers, inspectors and operators
     */
    public static final String PARAM_CERTS_INSP_OPS_DISTRIBUTION = "Cert/Insp/Ops Distribution";
    
    /**
     * Cheater probability
     */
    public static final String PARAM_PROBABILITY_CHEATING = "Cheating probability";
    
    /**
     * Indicator whether monitoring by inspectors is switched on
     */
    public static final String PARAM_MONITORING_BY_INSPECTORS = "Monitoring by inspectors";
    
    /**
     * Indicator whether monitoring by peers is switched on
     */
    public static final String PARAM_MONITORING_BY_PEERS = "Monitoring by peers";
    
    /**
     * Probability of monitoring
     */
    public static final String PARAM_PROBABILITY_MONITORING = "Probability for monitoring";
    
    /**
     * Probability of application approval
     */
    public static final String PARAM_PROBABILITY_APPLICATION_APPROVAL = "Probability for approval";
    
    /**
     * SVO distribution
     */
    public static final String PARAM_SVO_DISTRIBUTION = "SVO distribution (Order: I,C,P)";
    
    /**
     * Sanctioning distribution
     */
    public static final String PARAM_SANCTIONING_DISTRIBUTION = "Sanctioning distribution (Order: Soft, Medium, Hard)";
    
    /**
     * Number of monitored individuals per round
     */
    public static final String PARAM_NUMBER_OF_MONITORING_PER_ROUND = "Number of agents monitored per round";
    
    /**
     * Mean number of inspectors per certifier
     */
    public static final String PARAM_MEAN_INSPECTORS_PER_CERTIFIER = "Mean number of inspectors per certifier";
    
    /**
     * Probability for accidental non-compliance
     */
    public static final String PARAM_PROBABILITY_ACCIDENTAL_NON_COMPLIANCE = "Probability for accidental non-compliance";
    
    /**
     * Memory length (number of entries)
     */
    public static final String PARAM_MEMORY_LENGTH = "MEMORY LENGTH";
    
    /**
     * Number of simulation rounds
     */
    public static final String PARAM_SIMULATION_ROUNDS = "ROUNDS";
    
    /**
     * Key for seed
     */
    public static final String PARAM_SEED = "Seed";
    
    
    public static void main(String[] args) {
    	
    	
        /*addParameterRange(PARAM_CERTIFIERS, new float[]{50f, 200f, 50f});
        addParameterRange(PARAM_INSPECTORS, new float[]{50f, 200f, 50f});
        addParameterRange(PARAM_OPERATORS, new float[]{50f, 200f, 50f});*/
    	
    	// Base values
    	int NUMBER_OF_CERTIFIERS = 80;
        int NUMBER_OF_INSPECTORS = 400;
        int NUMBER_OF_OPERATORS = 10000;
    	
        float start = 0.5f;
    	float end = 1f;
    	float step = 0.25f;
    	
    	addParameter(PARAM_CERTIFIERS, NUMBER_OF_CERTIFIERS);
    	addParameter(PARAM_INSPECTORS, NUMBER_OF_INSPECTORS);
    	addParameter(PARAM_OPERATORS, NUMBER_OF_OPERATORS);
    	
    	addParameterRange(PARAM_CERTIFIERS_MULTIPLIER, new float[]{start, end, step});
    	addParameterRange(PARAM_INSPECTORS_MULTIPLIER, new float[]{start, end, step});
    	addParameterRange(PARAM_OPERATORS_MULTIPLIER, new float[]{start, end, step});
    	
    	/*
    	addParameterList(PARAM_CERTS_INSP_OPS_DISTRIBUTION, 
    			new int[]{Math.round(NUMBER_OF_CERTIFIERS * scale0), 
    					Math.round(NUMBER_OF_INSPECTORS * scale0), 
    					Math.round(NUMBER_OF_OPERATORS * scale0)},
    			new int[]{Math.round(NUMBER_OF_CERTIFIERS * scale1), 
    					Math.round(NUMBER_OF_INSPECTORS * scale0), 
    					Math.round(NUMBER_OF_OPERATORS * scale0)},
    			new int[]{Math.round(NUMBER_OF_CERTIFIERS * scale0), 
    					Math.round(NUMBER_OF_INSPECTORS * scale1), 
    					Math.round(NUMBER_OF_OPERATORS * scale0)},
    			new int[]{Math.round(NUMBER_OF_CERTIFIERS * scale0), 
    					Math.round(NUMBER_OF_INSPECTORS * scale0), 
    					Math.round(NUMBER_OF_OPERATORS * scale1)},
    			new int[]{Math.round(NUMBER_OF_CERTIFIERS * scale1), 
    					Math.round(NUMBER_OF_INSPECTORS * scale1), 
    					Math.round(NUMBER_OF_OPERATORS * scale1)}
    			);*/
        
        //addParameterRange(PARAM_PROBABILITY_CHEATING, new float[]{0.1f, 0.5f, 0.1f});
        addParameterList(PARAM_SVO_DISTRIBUTION, // SVO distribution: individualist, competitive, prosocial 
        		new float[]{0.33f, 0.33f, 0.33f}, 
        		
        		new float[]{0.5f, 0.25f, 0.25f},
        		new float[]{1f, 0f, 0f},
        		new float[]{0.25f, 0.5f, 0.25f},
        		new float[]{0f, 1f, 0f},
        		new float[]{0.25f, 0.25f, 0.5f},
        		new float[]{0f, 0f, 1f},
        		
        		new float[]{0.75f, 0.125f, 0.125f},
        		new float[]{0.125f, 0.75f, 0.125f},
        		new float[]{0.125f, 0.125f, 0.75f}
        		);
        
        addParameterList(PARAM_SANCTIONING_DISTRIBUTION, // Sanctioning distribution: soft, medium, hard 
        		new float[]{0.33f, 0.33f, 0.33f}, 
        		
        		new float[]{0.5f, 0.25f, 0.25f},
        		new float[]{1f, 0f, 0f},
        		new float[]{0.25f, 0.5f, 0.25f},
        		new float[]{0f, 1f, 0f},
        		new float[]{0.25f, 0.25f, 0.5f},
        		new float[]{0f, 0f, 1f},
        		
        		new float[]{0.75f, 0.125f, 0.125f},
        		new float[]{0.125f, 0.75f, 0.125f},
        		new float[]{0.125f, 0.125f, 0.75f}
        		);
        
        //addParameterList(PARAM_PROBABILITY_MONITORING, 0.5f);
        addParameterList(PARAM_NUMBER_OF_MONITORING_PER_ROUND, 1); // applies only to peer-based review; inspectors controlled via sanctioning
        
        addParameterList(PARAM_MONITORING_BY_INSPECTORS, true, false);
        addParameterList(PARAM_MONITORING_BY_PEERS, true, false);
        
        //addParameterRange(PARAM_MEAN_INSPECTORS_PER_CERTIFIER, new float[]{1f, 5f, 2f});
        
        addParameterList(PARAM_PROBABILITY_APPLICATION_APPROVAL, 0.75f);
        addParameterList(PARAM_PROBABILITY_ACCIDENTAL_NON_COMPLIANCE, 0.0f);
        
        addParameterList(PARAM_MEMORY_LENGTH, 100);
        
        addParameterList(PARAM_SIMULATION_ROUNDS, 10001);
        
        // Indicate number of seeds to be run
        addParameterList(PARAM_SEED, SeedExtractor.getSimulationSeeds("Seeds.txt").subList(0, 1));

        // Generate file for multiple machine configuration
        generateParameterFile(PARAMETER_FILE, 6, false);
    }
}