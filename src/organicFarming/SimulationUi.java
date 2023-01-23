package organicFarming;

import sim.display.GUIState;
import sim.engine.SimState;

/**
 * Main entry point for single simulation run.
 * Manual parameterization for individual runs occurs via SimulationSetup.
 * For full analysis, use launchers.SimulationLauncher (or where different machines are involved,
 * launchers.SimulationMetaLauncher), which rely on Parameter combinations generated using
 * launchers.parameterGenerators.ParameterGenerator and input seeds (in root directory).
 */
public class SimulationUi extends GUIState {

	public SimulationUi(SimState state) {
		super(state);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		SimulationSetup.batchRun = false;
		SimulationSetup.setupPlatform();
		SimulationSetup.setupSimulation();
		SimulationSetup.start();
	}

}
