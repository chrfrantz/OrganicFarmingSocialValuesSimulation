package organicFarming;

import org.jfree.chart.plot.XYPlot;
import org.nzdis.micro.AbstractRole;
import org.sofosim.environment.stats.Statistics;
import org.sofosim.environment.stats.StatsCalculator;
import org.sofosim.environment.stats.charts.TimeSeriesChartWrapper;

import organicFarming.artefacts.SvoTypes;
import organicFarming.roles.Certifier;
import organicFarming.roles.Operator;
import organicFarming.roles.OrganicTradingRole;

/**
 * Defines structure of stats output files, alongside location
 */
public class Stats extends Statistics {

    /**
     * Simulation reference
     */
    public Simulation mySim = null;
    

    public Stats(boolean activateStatsGraphController) {
        super(activateStatsGraphController, false, 1);
    }
    
    public static final String STATS_CHART_KEY_ACTIONS = "Actions Chart";    
    public static final String STATS_CHART_KEY_STATUS = "Status Chart";
    public static final String STATS_CHART_KEY_CHEATING = "Cheating Chart";
    public static final String STATS_CHART_KEY_QUEUES = "Queues Chart";
    public static final String STATS_CHART_KEY_MONITORING = "Monitoring Chart";
    public static final String STATS_CHART_KEY_LINKS = "Links Chart";
    public static final String STATS_CHART_KEY_SVO = "SVO Chart";
    
    public static final String STATS_CHART_NETWORK_DISTRO = "Network Distribution";
    
    public static final String KEY_MONITORING_NUMBER_OF_MONITORING_AGENTS = "Fraction of monitoring agents";
    public static final String KEY_MONITORING_FREQUENCY = "Monitoring frequency";
    
    public static final String KEY_MSG_QUEUE_SIZE_OPERATORS = "Operator Queues";
    public static final String KEY_MSG_QUEUE_SIZE_CERTIFIERS = "Certifier Queues";
    
    public static final String KEY_CERTIFIER_OPERATOR_LINKS = "Certifier-Operator links";
    public static final String KEY_CERTIFIER_INSPECTOR_LINKS = "Certifier-Inspector links";
    
    public static final String KEY_CHEATING_SVO_PROSOCIAL = "Cheating prosocial";
    public static final String KEY_CHEATING_SVO_INDIVIDUALIST = "Cheating individualist";
    public static final String KEY_CHEATING_SVO_COMPETITIVE = "Cheating competitive";
    
    public static final String KEY_SVO_PROSOCIAL = "SVO prosocial";
    public static final String KEY_SVO_INDIVIDUALIST = "SVO individualist";
    public static final String KEY_SVO_COMPETITIVE = "SVO competitive";
    
    StatsCalculator<Integer> calcActions = new StatsCalculator<>();
    StatsCalculator<Integer> calcStatus = new StatsCalculator<>();
    StatsCalculator<Integer> calcCheating = new StatsCalculator<>();
    StatsCalculator<Integer> calcQueues = new StatsCalculator<>();
    StatsCalculator<Integer> calcMonitoring = new StatsCalculator<>();
    StatsCalculator<Integer> calcRelationships = new StatsCalculator<>();
    StatsCalculator<Integer> calcSvo = new StatsCalculator<>();
   
    
    @Override
    public void singleStep() {
        
        //// MICRO OPERATIONS / INPUT
        
        for(AbstractRole agent: mySim.agentDirectory.values()) {
        	
        	OrganicTradingRole role = (OrganicTradingRole) agent;
        	
        	// All agents
        	calcActions.enterValue(role.LAST_ACTION, 1);
        	
        	// Operators
        	if (role.getClass().equals(Operator.class)) {
        		Operator operator = (Operator)role;
        		calcStatus.enterValue(operator.CERTIFICATION_STATUS, 1);
        		calcCheating.enterValue((operator.cheating ? "CHEATING" : (operator.actNonCompliantly ? "ACT NON-COMPLIANT" : "NO CHEATING; COMPLIANCE")), 1);
        		calcQueues.enterValue(KEY_MSG_QUEUE_SIZE_OPERATORS, operator.getNumberOfQueuedMessages());
        		calcMonitoring.enterValue(KEY_MONITORING_NUMBER_OF_MONITORING_AGENTS, operator.monitoring ? 1 : 0);
        		calcMonitoring.enterValue(KEY_MONITORING_FREQUENCY, operator.monitoringFrequency);
        		switch (operator.svoAttitude) {
	        		case SvoTypes.SVO_INDIVIDUALIST:
	        			if (operator.cheating) {
	        				calcSvo.enterValue(KEY_CHEATING_SVO_INDIVIDUALIST, 1);
	        			}
        				calcSvo.enterValue(KEY_SVO_INDIVIDUALIST, 1);
	        			break;
	        		case SvoTypes.SVO_COMPETITIVE:
	        			if (operator.cheating) {
	        				calcSvo.enterValue(KEY_CHEATING_SVO_COMPETITIVE, 1);
	        			}
        				calcSvo.enterValue(KEY_SVO_COMPETITIVE, 1);
	        			break;
	        		case SvoTypes.SVO_PROSOCIAL:
	        			if (operator.cheating) {
	        				calcSvo.enterValue(KEY_CHEATING_SVO_PROSOCIAL, 1);
	        			}
	        			calcSvo.enterValue(KEY_SVO_PROSOCIAL, 1);
	        			break;
	        		default: throw new RuntimeException("Unknown SVO type in stats.");
        		}
        		
        	} else if (role.getClass().equals(Certifier.class)) {
        		Certifier certifier = (Certifier)role;
        		calcQueues.enterValue(KEY_MSG_QUEUE_SIZE_CERTIFIERS, certifier.getNumberOfQueuedMessages());
        		
        		// Collect links
        		calcRelationships.enterValue(KEY_CERTIFIER_INSPECTOR_LINKS, certifier.managedInspectors.size());
        		calcRelationships.enterValue(KEY_CERTIFIER_OPERATOR_LINKS, certifier.managedOperators.size());
        	}
        	
        }
        
        //// MACRO OPERATIONS / OUTPUT
        
        //// CSV 
        //appendToFile(STATS_CHART_COOPERATE, cooperationStatsCalculator.getSumOfValues(STATS_CHART_COOPERATE));
        
        //// CHARTS
        
        for (String key: calcActions.getKeys()) {
        	addDataSeriesEntryForCurrentRound(STATS_CHART_KEY_ACTIONS, key, calcActions.getSumOfValues(key), true, "ACTION: ");
        }
        
        for (String key: calcStatus.getKeys()) {
        	addDataSeriesEntryForCurrentRound(STATS_CHART_KEY_STATUS, key, calcStatus.getSumOfValues(key), true, "STATUS: ");
        }
        
        for (String key: calcCheating.getKeys()) {
        	addDataSeriesEntryForCurrentRound(STATS_CHART_KEY_CHEATING, key, calcCheating.getSumOfValues(key), true, "CHEATING: ");
        }
        
        for (String key: calcQueues.getKeys()) {
        	addDataSeriesEntryForCurrentRound(STATS_CHART_KEY_QUEUES, key, calcCheating.getMean(key), true, "QUEUE LENGTH: ");
        }
        
        for (String key: calcMonitoring.getKeys()) {
        	addDataSeriesEntryForCurrentRound(STATS_CHART_KEY_MONITORING, key + " (mean)", calcMonitoring.getMean(key), true, "MONITORING: ");
        	addDataSeriesEntryForCurrentRound(STATS_CHART_KEY_MONITORING, key + " (stddev)", calcMonitoring.getStdDeviation(key), true, "MONITORING: ");
        }
        
        for (String key: calcRelationships.getKeys()) {
        	addDataSeriesEntryForCurrentRound(STATS_CHART_KEY_LINKS, key, calcRelationships.getMean(key), true, "LINKS MEAN: ");
        }
        
        for (String key: calcSvo.getKeys()) {
        	switch (key) {
        		case KEY_SVO_INDIVIDUALIST:
        			addDataSeriesEntryForCurrentRound(STATS_CHART_KEY_SVO, 
        					KEY_CHEATING_SVO_INDIVIDUALIST, 
        					calcSvo.containsKey(KEY_CHEATING_SVO_INDIVIDUALIST) ? 
        							(calcSvo.getSumOfValues(KEY_CHEATING_SVO_INDIVIDUALIST) / 
        									calcSvo.getSumOfValues(key)) : 0, 
        							true, "CHEATING BY SVO: ");
        			break;
        		case KEY_SVO_COMPETITIVE: 
        			addDataSeriesEntryForCurrentRound(STATS_CHART_KEY_SVO, 
        					KEY_CHEATING_SVO_COMPETITIVE, 
        					calcSvo.containsKey(KEY_CHEATING_SVO_COMPETITIVE) ? 
        							(calcSvo.getSumOfValues(KEY_CHEATING_SVO_COMPETITIVE) / 
        									calcSvo.getSumOfValues(key)) : 0, 
        							true, "CHEATING BY SVO: ");
        			break;
        		case KEY_SVO_PROSOCIAL:
        			addDataSeriesEntryForCurrentRound(STATS_CHART_KEY_SVO, 
        					KEY_CHEATING_SVO_PROSOCIAL, 
        					calcSvo.containsKey(KEY_CHEATING_SVO_PROSOCIAL) ? 
        							(calcSvo.getSumOfValues(KEY_CHEATING_SVO_PROSOCIAL) / 
        									calcSvo.getSumOfValues(key)) : 0, 
        							true, "CHEATING BY SVO: ");
        			break;
        		// tolerate other keys
        	}
        }
        
        //addDataSeriesEntryForCurrentRound(STATS_CHARTS_ACTIONS_BASED_ON_MEMORY, STATS_CHARTS_ACTIONS_BASED_ON_MEMORY, cooperationStatsCalculator.getMean(STATS_CHARTS_ACTIONS_BASED_ON_MEMORY));
        //addMultipleDataSeriesEntriesForCurrentRound(STATS_CHART_NADICO_DEONTIC_ACTION_CITIZEN, nAdicoStatsCalculatorCitizenActual.getAggregatedMap(StatsCalculator.AGGREGATION_SUM, -1), true, LABEL_CT_COUNT);
        
        //// STATS FORM
        
        // Print top-x official and citizen information in stats form
        //addToRoundBuffer(LABEL_OFFICIAL);
        //addToRoundBuffer(DataStructurePrettyPrinter.decomposeRecursively(nAdicoStatsCalculatorOfficialWithDeontic.getAggregatedMap(StatsCalculator.AGGREGATION_SUM, 10), null, 1));
        
        addCompleteDataSeries(STATS_CHART_NETWORK_DISTRO, KEY_CERTIFIER_INSPECTOR_LINKS, "Certifiers -> Inspectors", calcRelationships.getFrequencyDistribution(KEY_CERTIFIER_INSPECTOR_LINKS));
    	addCompleteDataSeries(STATS_CHART_NETWORK_DISTRO, KEY_CERTIFIER_OPERATOR_LINKS, "Certifiers -> Operators", calcRelationships.getFrequencyDistribution(KEY_CERTIFIER_OPERATOR_LINKS));
    }    
    	
	@Override
	public void runAtEndOfEachRound() {
        //// CLEAN UP - Clean up at the end of the round
        //cooperationStatsCalculator.clearAllEntries();
        
        calcActions.clearAllEntries();
        calcStatus.clearAllEntries();
        calcCheating.clearAllEntries();
        calcQueues.clearAllEntries();
        calcMonitoring.clearAllEntries();
        calcRelationships.clearAllEntries();
        calcSvo.clearAllEntries();
    }

    @Override
    public void customMethodThatIsRunPeriodically() {
    	
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void formatCharts(TimeSeriesChartWrapper chart, XYPlot plot, Statistics stats) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setupCharts() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void printGraphs() {
        // TODO Auto-generated method stub
        
    }

}
