package organicFarming.launchers.instances;

import java.util.ArrayList;

import org.christopherfrantz.parallelLauncher.util.parameterHandling.ParameterExtractor;
import org.christopherfrantz.parallelLauncher.util.parameterHandling.ParameterExtractorException;

import organicFarming.launchers.SimulationMetaLauncher;
import organicFarming.launchers.parameterGenerators.ParameterGenerator;
import organicFarming.SimulationSetup;

public class SimulationInstance {
	
	public static void main(String[] args) {
        SimulationSetup.batchRun = true;
        
        SimulationSetup.setupSimulation();
        
        ArrayList<ParameterExtractorException> extractorExceptions = new ArrayList<>();
        
        // Actual parameter parsing
       
        /*try {
        	Integer[] nbrArray = ParameterExtractor.
                    getParameterValueFromParameterArrayByParameterName(SimulationMetaLauncher.PARAMETER_FILE, ParameterGenerator.PARAM_CERTS_INSP_OPS_DISTRIBUTION, 
                            args, Integer[].class);
        	
        	SimulationSetup.NUMBER_OF_CERTIFIERS = nbrArray[0];
        	SimulationSetup.NUMBER_OF_INSPECTORS = nbrArray[1];
        	SimulationSetup.NUMBER_OF_OPERATORS = nbrArray[2];
        } catch (ParameterExtractorException e) {
            extractorExceptions.add(e);
        }*/
        
        // Certifiers
        try {
        	SimulationSetup.NUMBER_OF_CERTIFIERS = ParameterExtractor.
                getParameterValueFromParameterArrayByParameterName(SimulationMetaLauncher.PARAMETER_FILE, ParameterGenerator.PARAM_CERTIFIERS, 
                        args, Integer.class);
	    } catch (ParameterExtractorException e) {
	    	extractorExceptions.add(e);
	    }
        
        try {
        	SimulationSetup.NUMBER_OF_CERTIFIERS *= ParameterExtractor.
                getParameterValueFromParameterArrayByParameterName(SimulationMetaLauncher.PARAMETER_FILE, ParameterGenerator.PARAM_CERTIFIERS_MULTIPLIER, 
                        args, Float.class);
        	SimulationSetup.NUMBER_OF_CERTIFIERS = Math.round(SimulationSetup.NUMBER_OF_CERTIFIERS);
	    } catch (ParameterExtractorException e) {
	    	extractorExceptions.add(e);
	    }
        
        // Inspectors
        try {
        	SimulationSetup.NUMBER_OF_INSPECTORS = ParameterExtractor.
                getParameterValueFromParameterArrayByParameterName(SimulationMetaLauncher.PARAMETER_FILE, ParameterGenerator.PARAM_INSPECTORS, 
                        args, Integer.class);
	    } catch (ParameterExtractorException e) {
	    	extractorExceptions.add(e);
	    }
        
        try {
        	SimulationSetup.NUMBER_OF_INSPECTORS *= ParameterExtractor.
                getParameterValueFromParameterArrayByParameterName(SimulationMetaLauncher.PARAMETER_FILE, ParameterGenerator.PARAM_INSPECTORS_MULTIPLIER, 
                        args, Float.class);
        	SimulationSetup.NUMBER_OF_INSPECTORS = Math.round(SimulationSetup.NUMBER_OF_INSPECTORS);
	    } catch (ParameterExtractorException e) {
	    	extractorExceptions.add(e);
	    }
        
        // Operators
        try {
        	SimulationSetup.NUMBER_OF_OPERATORS = ParameterExtractor.
                getParameterValueFromParameterArrayByParameterName(SimulationMetaLauncher.PARAMETER_FILE, ParameterGenerator.PARAM_OPERATORS, 
                        args, Integer.class);
	    } catch (ParameterExtractorException e) {
	    	extractorExceptions.add(e);
	    }
        
        try {
        	SimulationSetup.NUMBER_OF_OPERATORS *= ParameterExtractor.
                getParameterValueFromParameterArrayByParameterName(SimulationMetaLauncher.PARAMETER_FILE, ParameterGenerator.PARAM_OPERATORS_MULTIPLIER, 
                        args, Float.class);
        	SimulationSetup.NUMBER_OF_OPERATORS = Math.round(SimulationSetup.NUMBER_OF_OPERATORS);
	    } catch (ParameterExtractorException e) {
	    	extractorExceptions.add(e);
	    }
        
        /*try {
        	SimulationSetup.PROBABILITY_CHEATING = ParameterExtractor.
                getParameterValueFromParameterArrayByParameterName(SimulationMetaLauncher.PARAMETER_FILE, ParameterGenerator.PARAM_PROBABILITY_CHEATING, 
                        args, Float.class);
	    } catch (ParameterExtractorException e) {
            extractorExceptions.add(e);
	    }*/
        
        try {
        	Float[] svoArray = ParameterExtractor.
                    getParameterValueFromParameterArrayByParameterName(SimulationMetaLauncher.PARAMETER_FILE, ParameterGenerator.PARAM_SVO_DISTRIBUTION, 
                            args, Float[].class);
        	
        	SimulationSetup.SVO_INDIVIDUALISTIC_FRACTION = svoArray[0];
        	SimulationSetup.SVO_COMPETITIVE_FRACTION = svoArray[1];
        	SimulationSetup.SVO_PROSOCIAL_FRACTION = svoArray[2];
        } catch (ParameterExtractorException e) {
            extractorExceptions.add(e);
        }
        
        try {
        	Float[] sncArray = ParameterExtractor.
                    getParameterValueFromParameterArrayByParameterName(SimulationMetaLauncher.PARAMETER_FILE, ParameterGenerator.PARAM_SANCTIONING_DISTRIBUTION, 
                            args, Float[].class);
        	
        	SimulationSetup.CERTIFIER_FRACTION_SANCTIONING_SOFT = sncArray[0];
        	SimulationSetup.CERTIFIER_FRACTION_SANCTIONING_MEDIUM = sncArray[1];
        	SimulationSetup.CERTIFIER_FRACTION_SANCTIONING_HARD = sncArray[2];
        } catch (ParameterExtractorException e) {
            extractorExceptions.add(e);
        }
        
        try {
        	SimulationSetup.MONITORING_NUMBER = ParameterExtractor.
                getParameterValueFromParameterArrayByParameterName(SimulationMetaLauncher.PARAMETER_FILE, ParameterGenerator.PARAM_NUMBER_OF_MONITORING_PER_ROUND, 
                        args, Integer.class);
	    } catch (ParameterExtractorException e) {
            extractorExceptions.add(e);
	    }
        
        /*try {
        	SimulationSetup.PROBABILITY_MONITORING_BY_INSPECTORS = ParameterExtractor.
                getParameterValueFromParameterArrayByParameterName(SimulationMetaLauncher.PARAMETER_FILE, ParameterGenerator.PARAM_PROBABILITY_MONITORING, 
                        args, Float.class);
	    } catch (ParameterExtractorException e) {
	    	extractorExceptions.add(e);
	    }*/
        
        try {
        	SimulationSetup.MONITORING_BY_INSPECTORS = ParameterExtractor.
                getParameterValueFromParameterArrayByParameterName(SimulationMetaLauncher.PARAMETER_FILE, ParameterGenerator.PARAM_MONITORING_BY_INSPECTORS, 
                        args, Boolean.class);
	    } catch (ParameterExtractorException e) {
	    	extractorExceptions.add(e);
	    }
        
        try {
        	SimulationSetup.MONITORING_BY_PEERS = ParameterExtractor.
                getParameterValueFromParameterArrayByParameterName(SimulationMetaLauncher.PARAMETER_FILE, ParameterGenerator.PARAM_MONITORING_BY_PEERS, 
                        args, Boolean.class);
	    } catch (ParameterExtractorException e) {
	    	extractorExceptions.add(e);
	    }
        
        /*try {
        	SimulationSetup.MEAN_INSPECTORS_PER_CERTIFIER = ParameterExtractor.
                getParameterValueFromParameterArrayByParameterName(SimulationMetaLauncher.PARAMETER_FILE, ParameterGenerator.PARAM_MEAN_INSPECTORS_PER_CERTIFIER, 
                        args, Float.class);
	    } catch (ParameterExtractorException e) {
	    	extractorExceptions.add(e);
	    }*/
        
        try {
        	SimulationSetup.PROBABILITY_APPLICATION_APPROVAL = ParameterExtractor.
                getParameterValueFromParameterArrayByParameterName(SimulationMetaLauncher.PARAMETER_FILE, ParameterGenerator.PARAM_PROBABILITY_APPLICATION_APPROVAL, 
                        args, Float.class);
	    } catch (ParameterExtractorException e) {
	    	extractorExceptions.add(e);
	    }
        
        try {
        	SimulationSetup.PROBABILITY_ACCIDENTAL_NON_COMPLIANCE = ParameterExtractor.
                getParameterValueFromParameterArrayByParameterName(SimulationMetaLauncher.PARAMETER_FILE, ParameterGenerator.PARAM_PROBABILITY_ACCIDENTAL_NON_COMPLIANCE, 
                        args, Float.class);
	    } catch (ParameterExtractorException e) {
	    	extractorExceptions.add(e);
	    }
        
        try {
        	SimulationSetup.MEMORY_MAX_ENTRIES = ParameterExtractor.
                getParameterValueFromParameterArrayByParameterName(SimulationMetaLauncher.PARAMETER_FILE, ParameterGenerator.PARAM_MEMORY_LENGTH, 
                        args, Integer.class);
	    } catch (ParameterExtractorException e) {
	    	extractorExceptions.add(e);
	    }
        
        try {
        	SimulationSetup.EXPERIMENTAL_ROUNDS = ParameterExtractor.
                getParameterValueFromParameterArrayByParameterName(SimulationMetaLauncher.PARAMETER_FILE, ParameterGenerator.PARAM_SIMULATION_ROUNDS, 
                        args, Integer.class);
	    } catch (ParameterExtractorException e) {
	    	extractorExceptions.add(e);
	    }
        
        try {
        	SimulationSetup.SEED = ParameterExtractor.
                getParameterValueFromParameterArrayByParameterName(SimulationMetaLauncher.PARAMETER_FILE, ParameterGenerator.PARAM_SEED, 
                        args, Long.class);
	    } catch (ParameterExtractorException e) {
            extractorExceptions.add(e);
	    }
        
        // Print exceptions
        if (!extractorExceptions.isEmpty()) {
        	StringBuffer buffer = new StringBuffer();
        	for (int i = 0; i < extractorExceptions.size(); i++) {
        		buffer.append(extractorExceptions.get(i)).append(System.getProperty("line.separator"));
        	}
        	throw new RuntimeException(buffer.toString());
        }
        	
        
        // Prepare and run simulation
        
        // Don't update charts
        SimulationSetup.sim.getStatistics().updateStatsGraphs(false);
        
        SimulationSetup.setupPlatform();
               
        SimulationSetup.start();
        
        // Writes confirmation of simulation run completion to log file
        SimulationSetup.writeCompletedParameterLog("completedParametersLog.txt", args, SimulationSetup.sim.getStatistics().getGlobalSubfolderForOutput());
        
        System.exit(0);
	}

}
