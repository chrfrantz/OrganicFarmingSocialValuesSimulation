package organicFarming.artefacts;

import organicFarming.roles.OrganicTradingRole;

public interface ViolationActionListener {

	/**
	 * Notification for observed violation by actor for enacting role-specific behaviour.
	 * @param observer Observer
	 * @param certifier Target of notification
	 * @param actor Violating operator
	 * @param violation Violation as per Violations class.
	 */
	public void actOnObservedViolation(OrganicTradingRole observer, OrganicTradingRole certifier, OrganicTradingRole actor, String violation);
	
}
