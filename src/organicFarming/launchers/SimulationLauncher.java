package organicFarming.launchers;

import org.christopherfrantz.parallelLauncher.ParallelLauncher;
import org.christopherfrantz.parallelLauncher.util.parameterHandling.ParameterExtractor;

import organicFarming.launchers.instances.SimulationInstance;

/**
 * Runs simulations with parameters provided by parameter file.
 * Relies on prior generation of parameter set (using ParameterGenerator and Seeds.txt)
 */
public class SimulationLauncher extends ParallelLauncher {
	
	public static void main(String[] args) {
        //maxNumberOfActiveParallelLaunchers = 1;
        //maxNumberOfRunningLaunchedProcesses = 1;
        
        addClassToBeLaunched(SimulationInstance.class);
                
        setArgumentsToBePassedToLaunchedClasses(ParameterExtractor.
                getParameterArrayFromParameterFile(SimulationMetaLauncher.PARAMETER_FILE));
        
        startBatchProcessesMinimized = true;
        startInputOutputMonitorMinimized = true;
        
        setQueueCheckingFrequency(0.5);
        
        keepWindowOpen = false;
        
        start(args);
    }

}
