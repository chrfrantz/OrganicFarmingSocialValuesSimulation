package organicFarming.launchers;

import org.christopherfrantz.parallelLauncher.Launcher;
import org.christopherfrantz.parallelLauncher.util.listeners.DynamicLauncherInstanceCounter;
import org.christopherfrantz.parallelLauncher.MetaLauncher;
import org.christopherfrantz.parallelLauncher.util.parameterHandling.ParameterExtractor;
import org.christopherfrantz.parallelLauncher.util.parameterHandling.ParameterFileGenerator;

import organicFarming.launchers.parameterGenerators.ParameterGenerator;

/**
 * Used for running selected parameter sets across different machines (Machines identified via MachineID).
 */
public class SimulationMetaLauncher extends MetaLauncher {
		
		/**
		 * Indicator whether machine-specific parameters should be used
		 */
		public static boolean multiMachineSetup = true;
		
		/**
		 * Parameter file dynamically generated based on machine index 
		 * (which is read from config file MetaLauncher_MachineID).
		 */
		public static String PARAMETER_FILE = multiMachineSetup ? 
				ParameterFileGenerator.getParameterFilenameForIndex(
						ParameterGenerator.PARAMETER_FILE, 
						Launcher.initializeMachineIdFromConfigFile()) : 
				ParameterGenerator.PARAMETER_FILE;
		
		public static void main(String args[]) {
			
			DynamicLauncherInstanceCounter launcher = new DynamicLauncherInstanceCounter() {
				
				@Override
				public int totalNumberOfInstancesToBeLaunched() {
					return ParameterExtractor.countNumberOfValidParameterArrays(PARAMETER_FILE);
				}
				
				@Override
				public int remainingNumberOfInstancesToBeLaunched() {
					return ParameterExtractor.countNumberOfValidRemainingParameterArrays(PARAMETER_FILE);
				}
				
			};
			
			addLaunchersToBeLaunched(SimulationLauncher.class, launcher);
			maxNumberOfQueuedOrRunningLaunchersAtOneTime = 1;
			//maxNumberOfQueuedOrRunningLaunchersAtOneTime = 1;
			
			setQueueCheckingFrequency(1);
			
			start();
		}

}
