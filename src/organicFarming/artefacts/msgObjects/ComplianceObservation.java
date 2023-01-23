package organicFarming.artefacts.msgObjects;

import organicFarming.artefacts.Violations;
import organicFarming.roles.OrganicTradingRole;

public class ComplianceObservation {

	/**
	 * Observing entity
	 */
	public final OrganicTradingRole observer;
	
	/**
	 * Observed entity
	 */
	public OrganicTradingRole observee;
	
	/**
	 * Entity observation is reported to
	 */
	public OrganicTradingRole reportee;
	
	/**
	 * Indicates violation type based on constants from Violations class.
	 */
	public String violationType = null;

	/**
	 * Creates Compliance Observation by observer with respect to observee and observed violation
	 * @param observer Observer
	 * @param observee Violator
	 * @param observedViolation Value from Violations class
	 */
	public ComplianceObservation(OrganicTradingRole observer, OrganicTradingRole observee, String observedViolation) {
		this.observer = observer;
		this.observee = observee;
		if (!Violations.violations.contains(observedViolation)) {
			throw new RuntimeException("Illegal violation type " + observedViolation + " during instantiation of observation.");
		}
		this.violationType = observedViolation;
	}

	@Override
	public String toString() {
		return "ComplianceObservation [observer=" + observer + ", observee=" + observee + ", reportee=" + reportee
				+ ", violationType=" + violationType + "]";
	}
	
}
