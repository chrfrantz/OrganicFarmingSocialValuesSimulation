package organicFarming.artefacts;

import java.util.ArrayList;
import java.util.List;

public class SanctioningType {
	
	/**
	 * Hard sanctions by certifiers for non-compliant operators.
	 */
	public static final String HARD = "HARD";
	
	/**
	 * Mixed sanctioning behaviour by certifiers for non-compliant operators.
	 */
	public static final String MEDIUM = "MEDIUM";
	
	/**
	 * Soft sanctions by certifiers for non-compliant operators.
	 */
	public static final String SOFT = "SOFT";
	
	/**
	 * Sanctioning types
	 */
	public static List<String> sanctioningTypes = new ArrayList<>();
	
	static {
		sanctioningTypes.add(SOFT);
		sanctioningTypes.add(MEDIUM);
		sanctioningTypes.add(HARD);
	}

}
