package organicFarming;

import java.awt.Color;

import org.frogberry.windowPositionSaver.PositionSaver;
import org.nzdis.it2fls.ChartViewer;
import org.nzdis.micro.bootloader.MicroBootProperties;
import org.nzdis.micro.constants.AgentConsoleOutputLevels;
import org.nzdis.micro.constants.MessagePassingFrameworks;
import org.nzdis.micro.inspector.PlatformInspectorGui;
import org.nzdis.micro.inspector.listeners.ShowSelectedAgentsOutputInInspectorGuiListener;
import org.nzdis.micro.messaging.MTRuntime;
import org.sofosim.batching.BatchedExperiments;
import org.sofosim.environment.annotations.SimulationParam;
import org.sofosim.environment.stats.printer.GraphsPrinter;

public class SimulationSetup extends BatchedExperiments{

    public static boolean batchRun = false;
    
    public static Simulation sim;

    /**
     * Number of certifiers
     */
    @SimulationParam
    public static int NUMBER_OF_CERTIFIERS = 100;
    
    /**
     * Number of inspectors
     */
    @SimulationParam
    public static int NUMBER_OF_INSPECTORS = 100;
    
    /**
     * Number of operators
     */
    @SimulationParam
    public static int NUMBER_OF_OPERATORS = 100;
    
    /**
     * Maximum number of memory entries for action choice
     */
    @SimulationParam
    public static int MEMORY_MAX_ENTRIES = 100;
    
    /**
     * Probability for exploring new re/actions
     */
    @SimulationParam
    public static float EXPLORATION_PROBABILITY = 0.1f;
    
    /**
     * Input capital at the beginning
     */
    @SimulationParam
    public static float STARTING_CAPITAL = 100;
    
    /**
     * Mean profit from operation
     */
    @SimulationParam
    public static float OPERATION_PROFIT = 1.5f;  
    
    /**
     * Deontic range type
     */
    //@SimulationParam
    //public static String DEONTIC_RANGE_TYPE = DeonticRangeConfiguration.DEONTIC_RANGE_TYPE_DISCRETE;
    
    /**
     * Deontic value mapper
     */
    //@SimulationParam
    //public static Class DEONTIC_VALUE_MAPPER_CLASS = DiscreteDeonticValueMapper.class;
    
    /**
     * Strategy for aggregation of action instances along deontic range.
     */
    //@SimulationParam
    //public static String DEONTIC_RANGE_AGGREGATION_STRATEGY;
    
    /**
     * Deontic range history length
     */
    //@SimulationParam
    //public static int DEONTIC_RANGE_HISTORY_LENGTH = 100;
    
    /**
     * Permissible movement around center to be considered indifferent.
     */
    //@SimulationParam
    //public static Float DEONTIC_RANGE_MOVEMENT_AROUND_CENTER = 0.05f;
    
    /**
     * Tolerance zone around extreme deontics.
     */
    //@SimulationParam
    //public static Float DEONTIC_RANGE_TOLERANCE_ZONE = 0.05f;
    
    /**
     * First-order cheating
     */
    @SimulationParam
    public static boolean CHEATING_FIRST_ORDER = true;
    
    /**
     * Fraction of cheaters among all agents
     */
    @SimulationParam
    public static float CHEATER_FRACTION = 0.5f;
    
    /**
     * Cheating probability (for cheaters)
     */
    @SimulationParam
    public static float PROBABILITY_CHEATING = 0.5f;
    
    /**
     * Probability for non-compliant operation as certified operation
     */
    @SimulationParam
    public static float PROBABILITY_NON_COMPLIANT_OPERATION = 0.5f;
    
    /**
     * Probability for non-compliant operation as certified operation after notification
     */
    @SimulationParam
    public static float PROBABILITY_NON_COMPLIANT_OPERATION_AFTER_NOTIFICATION = 0.25f;
    
    /**
     * Probability that monitoring decision (whether or not to monitor) flips (noise)
     */
    @SimulationParam
    public static float PROBABILITY_MONITORING_FLIP = 0.05f;
    
    /**
     * Weight of observation in own memory (factor compared to own experience)
     */
    @SimulationParam
    public static float SOCIAL_LEARNING_WEIGHT = 0.5f;
    
    static {
    	
    }
    
    /**
     * Probability for applying for certification
     */
    @SimulationParam
    public static float PROBABILITY_APPLICATION_SUBMISSION = 0.5f;
    
    /**
     * Probability for approving applications
     */
    @SimulationParam
    public static float PROBABILITY_APPLICATION_APPROVAL = 0.5f;
    
    /**
     * Probability for approving applications after suspension
     */
    @SimulationParam
    public static float PROBABILITY_APPLICATION_APPROVAL_AFTER_SUSPENSION = 0.4f;
    
    /**
     * Probability for approving applications after revocation
     */
    @SimulationParam
    public static float PROBABILITY_APPLICATION_APPROVAL_AFTER_REVOCATION = 0.2f;
    
    /**
     * Probability for approving applications after being non-certified non-compliant operator
     */
    @SimulationParam
    public static float PROBABILITY_APPLICATION_APPROVAL_AFTER_BEING_NON_COMPLIANT_REGULAR_OPERATOR = 0.1f;
    
    /**
     * Probability of a license being revoked
     */
    @SimulationParam
    public static float PROBABILITY_CERTIFICATION_REVOCATION = 0.5f;
    
    /**
     * Probability of a license being suspended
     */
    @SimulationParam
    public static float PROBABILITY_CERTIFICATION_SUSPENSION = 0.5f;
    
    /**
     * Indicator whether inspectors monitor operators.
     */
    @SimulationParam
    public static boolean MONITORING_BY_INSPECTORS = true;
    
    /**
     * Indicator whether peers monitor other agents.
     */
    @SimulationParam
    public static boolean MONITORING_BY_PEERS = true;
    
    /**
     * Probability for monitoring by inspectors.
     */
    @SimulationParam
    public static float PROBABILITY_MONITORING_BY_INSPECTORS = 1.0f;
    
    /**
     * Probability for monitoring by peers.
     */
    //@SimulationParam
    //public static float PROBABILITY_MONITORING_BY_PEERS = 0.2f;
    
    /**
     * Number of fellow agents to be monitored. Does not apply to inspectors; they are controlled by sanction intensity.
     */
    @SimulationParam
    public static int MONITORING_NUMBER = 1;
    
    /**
     * Mean number of inspectors per certifier.
     * If set to -1f, the distribution as per Saba's mail is used
     * Alternatively, a random value with the given value as a mean is used.
     */
    @SimulationParam
    public static float MEAN_INSPECTORS_PER_CERTIFIER = -1f;
    
    /**
     * Mean premium for selling organic foods
     */
    @SimulationParam
    public static float ORGANIC_PREMIUM = 0.25f;
    
    /**
     * Fraction of individuals with individualistic attitude
     */
    @SimulationParam
    public static float SVO_INDIVIDUALISTIC_FRACTION = 0.285f; // 51%, scaled down with factor .56
    
    /**
     * Fraction of individuals with prosocial attitude
     */
    @SimulationParam
    public static float SVO_PROSOCIAL_FRACTION = 0.296f; // 53%, scaled down with factor .56
    
    /**
     * Fraction of individuals with competitive attitude
     */
    @SimulationParam
    public static float SVO_COMPETITIVE_FRACTION = 0.42f; // 75%, scaled down with factor .56
    
    /**
     * Fraction of certifiers that sanctions hard
     */
    @SimulationParam
    public static float CERTIFIER_FRACTION_SANCTIONING_HARD = 0.33f; 
    
    /**
     * Fraction of certifiers that sanctions medium
     */
    @SimulationParam
    public static float CERTIFIER_FRACTION_SANCTIONING_MEDIUM = 0.33f;
    
    /**
     * Fraction of certifiers that sanctions soft
     */
    @SimulationParam
    public static float CERTIFIER_FRACTION_SANCTIONING_SOFT = 0.33f;
    
    /**
     * Probability for accidental non-compliance
     */
    @SimulationParam
    public static float PROBABILITY_ACCIDENTAL_NON_COMPLIANCE = 0f;
    
    /**
     * Number of message processed per round. -1 indicates unlimited messages.
     */
    @SimulationParam
    public static int PROCESSED_MESSAGES_PER_ROUND = 1;
    
    /**
     * Simulation seed
     */
    @SimulationParam
    public static long SEED = 435455L;
    
    /**
     * Simulation rounds
     */
    @SimulationParam
    public static int EXPERIMENTAL_ROUNDS = 2001;
    
    public static void start(){
        //prepare folder name
        sim.getStatistics().setGlobalSubfolderForOutput("results/"
                + "crt" + NUMBER_OF_CERTIFIERS
                + "_isp" + NUMBER_OF_INSPECTORS
                + "_opr" + NUMBER_OF_OPERATORS
                + "_cir" + MEAN_INSPECTORS_PER_CERTIFIER // Certifier-inspector ratio (overrides empirical ratio)
                + "_svoI" + SVO_INDIVIDUALISTIC_FRACTION
                + "_svoC" + SVO_COMPETITIVE_FRACTION
                + "_svoP" + SVO_PROSOCIAL_FRACTION
                + "_sncH" + CERTIFIER_FRACTION_SANCTIONING_HARD
                + "_sncM" + CERTIFIER_FRACTION_SANCTIONING_MEDIUM
                + "_sncS" + CERTIFIER_FRACTION_SANCTIONING_SOFT
                + "_chtP" + PROBABILITY_CHEATING
                + "_monN" + MONITORING_NUMBER
                + "_monI" + MONITORING_BY_INSPECTORS
                + "_monP" + MONITORING_BY_PEERS
                + "_appP" + PROBABILITY_APPLICATION_APPROVAL
                + "_accP" + PROBABILITY_ACCIDENTAL_NON_COMPLIANCE
                + "_mem" + MEMORY_MAX_ENTRIES
                + "_sd" + String.valueOf(SEED).substring(String.valueOf(SEED).length() - 2)
                , true, true);
        
        //start Console or run simulation
        SimulationUi ui = new SimulationUi(sim); 
        if(batchRun){
            stepUi(ui, EXPERIMENTAL_ROUNDS, "organicFarming");
        } else {
            ui.createController();
        }
    }
    
    public static void setupSimulation(){
        sim = new Simulation(new Stats((batchRun ? false : true)), SEED);
        
        // TODO: Hack
        ((Stats)sim.getStatistics()).mySim = sim;
        
        NUMBER_OF_CERTIFIERS = 82;
        NUMBER_OF_INSPECTORS = 400;
        NUMBER_OF_OPERATORS = 10000; //7564;
        
        float scale = 1f;
        
        NUMBER_OF_CERTIFIERS = Math.round(NUMBER_OF_CERTIFIERS * scale);
        NUMBER_OF_INSPECTORS = Math.round(NUMBER_OF_INSPECTORS * scale);
        NUMBER_OF_OPERATORS = Math.round(NUMBER_OF_OPERATORS * scale);
        
        MONITORING_BY_INSPECTORS = true;
        MONITORING_BY_PEERS = true;
        
        PROBABILITY_APPLICATION_APPROVAL = 0.75f;
        
        SVO_INDIVIDUALISTIC_FRACTION = 0.33f;
        SVO_COMPETITIVE_FRACTION = 0.33f;
        SVO_PROSOCIAL_FRACTION = 0.33f;
        
        //PROBABILITY_CHEATING = 0.1f;
        MONITORING_NUMBER = 1;
        //MEAN_INSPECTORS_PER_CERTIFIER = 1.5f;
        
        PROBABILITY_ACCIDENTAL_NON_COMPLIANCE = 0.0f;
        PROBABILITY_MONITORING_FLIP = 0.05f;
        
        /*DEONTIC_RANGE_TYPE = DeonticRangeConfiguration.DEONTIC_RANGE_TYPE_HISTORY_MIN_MAX;
        DEONTIC_VALUE_MAPPER_CLASS = ZeroBasedEquiCompartmentDeonticValueMapper.class;
        DEONTIC_RANGE_AGGREGATION_STRATEGY = NAdicoGeneralizer.AGGREGATION_STRATEGY_SUM;
        
        CHEATING_FIRST_ORDER = true;
        CHEATING_SECOND_ORDER = false;
        CHEATER_FRACTION = 0.3f;
        ALLOW_IGNORE_ACTION = true;
        EXPLORATION_PROBABILITY = 0.1f;
        
        USE_APPEALS = true;
        //SOCIAL_LEARNING_WEIGHT = 1f;
        SOCIAL_LEARNING = true;
        SOCIAL_LEARNING_SEPARATED_BY_ROLES = true;
        FIXED_OFFICIAL_HIERARCHY = false;
        
        //MEMORY_MAX_ENTRIES = 150;
        DEONTIC_RANGE_HISTORY_LENGTH = 100;*/
        
        ChartViewer.backgroundColorForCharts = Color.white;
        
        
        sim.getStatistics().autogenerateGraphForNewChartKey = true;
        sim.getStatistics().printGraphsInUnifiedColor = false;
        sim.getStatistics().printGraphsAsDashedLines = true;
        //sim.getStatistics().printSeriesLabelsInUnifiedColor = false;
        sim.getStatistics().graphBackgroundColor = Color.WHITE;
        sim.getStatistics().printLegend = true;
        sim.getStatistics().printRadarCharts = true;
        sim.getStatistics().showChartTitle = false;
        
        if (batchRun) {
        	sim.getStatistics().showStatsForm = false;
        	sim.getStatistics().manageWindowsUsingPositionSaver = false;
        }
        
        //Data collection
        sim.getStatistics().setDataCollectionFrequency(1);
        sim.getStatistics().setChartUpdateFrequency(10);
                
        //CSV File output
        sim.getStatistics().setNoOfLinesBufferedBeforeWritingToFile(10);
        sim.getStatistics().showMessageWhenWritingData = false;
        //sim.getStatistics().overwriteOutputFileUponHeaderChange = true;
        sim.getStatistics().setReadCsvFileHeadersFromFile(true, true, true);
        sim.getStatistics().setWriteCsvFileHeadersToFile(true);
        
        //Image file output
        sim.getStatistics().setCustomMethodExecutionFrequency(20);
        sim.getStatistics().printMultipleImageFormats.add(GraphsPrinter.IMAGE_FORMAT_SVG);
        sim.getStatistics().printMultipleImageFormats.add(GraphsPrinter.IMAGE_FORMAT_PNG);
        sim.getStatistics().setChartsAutoPrintFrequency(100);
        sim.getStatistics().setStatsFormSavingFrequency(100);
        boolean printCharts = true;
        sim.getStatistics().printRadarCharts = printCharts;
        sim.getStatistics().updateStatsGraphs(printCharts);
        sim.getStatistics().saveDatasetsWhenPrintingCharts = true;
        GraphsPrinter.zipSvgOutput = true;
    }
    
    public static void setupPlatform(){
        MicroBootProperties.setRandomNumberGeneratorSeed(SEED);
        MicroBootProperties.setInternalMessageTransport(MessagePassingFrameworks.MICRO_FIBER);
        //MicroBootProperties.setExecutorProperties(1, 1);
        MicroBootProperties.startLazy(false);
        MicroBootProperties.setSynchronousMessageDeliveryMode(true);
        MicroBootProperties.setAgentConsoleOutputLevel(AgentConsoleOutputLevels.NO_OUTPUT);
        //MicroBootProperties.setPrefixForMultiLineAgentConsoleOutput(true);
        MicroBootProperties.setMessageTypePrefixForAgentConsoleOutput(true);
        //MicroBootProperties.setMicroMessageValidator("fuzzyMaghribis.ConversationMessageValidator");
        //MicroMessage.globalValidation = true;
        PositionSaver.setWindowSetId("organicFarming");
        PositionSaver.setIncludeTitleInFrameKeys(false);
        PlatformInspectorGui.getInstance().registerListener(new ShowSelectedAgentsOutputInInspectorGuiListener());
        PlatformInspectorGui.activateDebugMode(true);
        PlatformInspectorGui.setMapDecompositionThreshold(1);
        MTRuntime.showStackTraceOnErrors = true;
        MTRuntime.showErrorsWhenPickingRandomly = true;
    }
    
    /**
     * Returns new instance of DiscreteDeonticRange
     * @return
     */
    /*public static DeonticRangeConfiguration getDeonticRangeConfiguration(SimulationSetup setup) {
        return new DeonticRangeConfiguration(
                DEONTIC_RANGE_TYPE, 
                DEONTIC_RANGE_HISTORY_LENGTH, 
                DEONTIC_RANGE_MOVEMENT_AROUND_CENTER, 
                DEONTIC_RANGE_TOLERANCE_ZONE, 
                DEONTIC_VALUE_MAPPER_CLASS);
    }*/

    @Override
    public void batchSpecification() {
        // TODO Auto-generated method stub
        
    }
    
}
