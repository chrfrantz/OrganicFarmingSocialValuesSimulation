# OrganicFarmingSocialValuesSimulation
Simulation of organic farming certification process under consideration of diverse participation motivations. The simulation is associated with an article.

# How to run the simulation

The simulation relies on OpenJDK 8 ([recommended download](https://adoptium.net/temurin/releases/?version=8)), and is best run from an Integrated Development Environment (IDE), such as [IntelliJ IDEA](https://www.jetbrains.com/idea/). The project contains the corresponding project file that configures the project for execution. It is important to install the JDK prior to opening the IDE. Otherwise the project may need to be reconfigured to use OpenJDK 8 (under 'File' --> 'Project Structure').

If using a different IDE, ensure to use the correct JDK, and import the libraries on which the simulation relies (contained in the 'libs' subfolder).

* First steps

  * To run the simulation manually, with individually parameterized values, launch 'src/organicFarming/SimulationUI.java', which runs based on the configuration provided in the SimulationSetup.java. Adjust the values in order to explore different variations.

  * Output is generated in an automatically generated 'results' directory.

* Comprehensive simulation runs

  * To run the simulation under consideration of all parameter variations, use 'src/organicFarming/launchers/SimulationLauncher.java', which runs one instance for each parameter configuration provided in input files (see Parameters_ files in root directory). 

  * To coordinate the run of launchers across different machines, 'src/organicFarming/launchers/MetaLauncher.java' offers the opportunity to separate simulation runs for different machines (based on Machine IDs).

  * Parameter combinations are produced using 'src/organicFarming/launchers/parameterGenerators/ParameterGenerator.java', which contains the parameter values/ranges and is used to produce all possible combinations. All combinations are then run for all provided random number generator seeds using ParallelLauncher instances (which are coordinated using MetaLauncher instances). Examples of generated parameter configurations as well as random number generation seeds (File 'Seeds.txt') are provided in the root directory.

# Dependencies

The simulation relies on a range of dependencies, all of which are provided in the 'libs' subfolder.

Most notable include [MASON](https://cs.gmu.edu/~eclab/projects/mason/) as the underlying scheduling engine, the [Micro-agents platform](https://github.com/micro-agents/micro-agents) to model feature-rich efficient agents, as well as [ParallelLauncher](https://github.com/chrfrantz/ParallelLauncher) to automated sensitivity analyses as well as large-scale experimental runs. Additional dependencies largely pertain to visualization and the collection of statistical information. 
