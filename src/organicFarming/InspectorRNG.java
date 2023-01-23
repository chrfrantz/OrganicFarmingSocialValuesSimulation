package organicFarming;

import org.nzdis.micro.random.MersenneTwister;
import org.sofosim.util.StagedRNG;

public class InspectorRNG extends StagedRNG {

	public static final String DISTRIBUTION_GROUP_1 = "DIST_GROUP_1";
	public static final String DISTRIBUTION_GROUP_2 = "DIST_GROUP_2";
	public static final String DISTRIBUTION_GROUP_3 = "DIST_GROUP_3";
	public static final String DISTRIBUTION_GROUP_4 = "DIST_GROUP_4";
	public static final String DISTRIBUTION_GROUP_5 = "DIST_GROUP_5";
	public static final String DISTRIBUTION_GROUP_6 = "DIST_GROUP_6";
	public static final String DISTRIBUTION_GROUP_7 = "DIST_GROUP_7";
	
	public InspectorRNG() {
		
		// Source: Saba's mail 20181031, Simulation Validation
		
		// Fill group distributions
		addGroupDistributionMapEntry(DISTRIBUTION_GROUP_1, 0.39f);
		addGroupDistributionMapEntry(DISTRIBUTION_GROUP_2, 0.21f);
		addGroupDistributionMapEntry(DISTRIBUTION_GROUP_3, 0.10f);
		addGroupDistributionMapEntry(DISTRIBUTION_GROUP_4, 0.09f);
		addGroupDistributionMapEntry(DISTRIBUTION_GROUP_5, 0.16f);
		addGroupDistributionMapEntry(DISTRIBUTION_GROUP_6, 0.02f);
		addGroupDistributionMapEntry(DISTRIBUTION_GROUP_7, 0.02f);
		
		// Fill boundary values
		addGroupBoundaryValueMapEntry(DISTRIBUTION_GROUP_1, 1);
		addGroupBoundaryValueMapEntry(DISTRIBUTION_GROUP_2, 2);
		addGroupBoundaryValueMapEntry(DISTRIBUTION_GROUP_3, 3);
		addGroupBoundaryValueMapEntry(DISTRIBUTION_GROUP_4, 4);
		addGroupBoundaryValueMapEntry(DISTRIBUTION_GROUP_5, 5);
		addGroupBoundaryValueMapEntry(DISTRIBUTION_GROUP_6, 6);
		addGroupBoundaryValueMapEntry(DISTRIBUTION_GROUP_7, 10);
		
		prepareDistributions();
	}
	
	public static void main(String[] args) {
		MersenneTwister rng = new MersenneTwister();
		InspectorRNG inspRNG = new InspectorRNG();
		for (int i = 0; i < 1000; i++) {
			System.out.println(inspRNG.nextInt(rng));
		}
	}
	
}
