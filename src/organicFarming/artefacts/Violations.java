package organicFarming.artefacts;

import java.util.ArrayList;
import java.util.List;


public class Violations {

	/**
	 * Constant indicating that uncertified agent acts as certified one.
	 */
	public static final String UNCERTIFIED_ACTING_AS_CERTIFIED = "UNCERTIFIED_AGENT_ACTING_AS_CERTIFIED";
	
	/**
	 * Constant indicating that a certified agent acts as an uncertified one.
	 */
	public static final String CERTIFIED_ACTING_AS_UNCERTIFIED = "CERTIFIED_AGENT_ACTING_AS_UNCERTIFIED";
	
	/**
	 * Constant indicating that a certified agent acts non-compliantly.
	 */
	public static final String CERTIFIED_ACTING_NON_COMPLIANT = "CERTIFIED_AGENT_ACTING_NON_COMPLIANT";
	
	/**
	 * List of violations.
	 */
	public static final List<String> violations = new ArrayList<>();
	
	static {
		violations.add(UNCERTIFIED_ACTING_AS_CERTIFIED);
		violations.add(CERTIFIED_ACTING_AS_UNCERTIFIED);
		violations.add(CERTIFIED_ACTING_NON_COMPLIANT);
	}
	
}
