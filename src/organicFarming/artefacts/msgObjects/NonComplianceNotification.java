package organicFarming.artefacts.msgObjects;

import organicFarming.roles.OrganicTradingRole;

public class NonComplianceNotification {
	
	/**
	 * Notifying entity.
	 */
	public OrganicTradingRole notifier;
	
	/**
	 * Subject of sanctioning.
	 */
	public OrganicTradingRole subject;
	
	/**
	 * Instantiates non-compliance notification to be sent to violating certified agents.
	 * @param notifier
	 * @param subject
	 */
	public NonComplianceNotification(OrganicTradingRole notifier, OrganicTradingRole subject) {
		this.notifier = notifier;
		this.subject = subject;
	}

	@Override
	public String toString() {
		return "NonComplianceNotification [notifier=" + notifier + ", subject=" + subject + "]";
	}

}
