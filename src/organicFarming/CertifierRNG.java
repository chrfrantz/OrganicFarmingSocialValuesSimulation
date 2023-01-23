package organicFarming;

import org.nzdis.micro.random.MersenneTwister;
import org.sofosim.util.StagedRNG;

public class CertifierRNG extends StagedRNG {

	public static final String DISTRIBUTION_GROUP_1 = "DIST_GROUP_1";
	public static final String DISTRIBUTION_GROUP_2 = "DIST_GROUP_2";
	public static final String DISTRIBUTION_GROUP_3 = "DIST_GROUP_3";
	public static final String DISTRIBUTION_GROUP_4 = "DIST_GROUP_4";
	public static final String DISTRIBUTION_GROUP_5 = "DIST_GROUP_5";
	public static final String DISTRIBUTION_GROUP_6 = "DIST_GROUP_6";
	
	public CertifierRNG() {
		
		// Fill group distributions
		addGroupDistributionMapEntry(DISTRIBUTION_GROUP_1, 0.05f);
		addGroupDistributionMapEntry(DISTRIBUTION_GROUP_2, 0.16f);
		addGroupDistributionMapEntry(DISTRIBUTION_GROUP_3, 0.06f);
		addGroupDistributionMapEntry(DISTRIBUTION_GROUP_4, 0.03f);
		addGroupDistributionMapEntry(DISTRIBUTION_GROUP_5, 0.03f);
		addGroupDistributionMapEntry(DISTRIBUTION_GROUP_6, 0.05f);
		
		// Fill boundary values
		addGroupBoundaryValueMapEntry(DISTRIBUTION_GROUP_1, 50);
		addGroupBoundaryValueMapEntry(DISTRIBUTION_GROUP_2, 200);
		addGroupBoundaryValueMapEntry(DISTRIBUTION_GROUP_3, 400);
		addGroupBoundaryValueMapEntry(DISTRIBUTION_GROUP_4, 600);
		addGroupBoundaryValueMapEntry(DISTRIBUTION_GROUP_5, 1000);
		addGroupBoundaryValueMapEntry(DISTRIBUTION_GROUP_6, 2000); // realistic max. value?
		
		prepareDistributions();
	}
	
	public static void main(String[] args) {
		MersenneTwister rng = new MersenneTwister();
		CertifierRNG certRng = new CertifierRNG();
		for (int i = 0; i < 1000; i++) {
			System.out.println(certRng.nextInt(rng));
		}
	}
	
}
