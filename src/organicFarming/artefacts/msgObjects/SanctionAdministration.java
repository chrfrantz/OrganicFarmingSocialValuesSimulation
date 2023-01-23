package organicFarming.artefacts.msgObjects;

import organicFarming.artefacts.Sanctions;
import organicFarming.roles.OrganicTradingRole;

public class SanctionAdministration {

	/**
	 * Sanctioning entity.
	 */
	public OrganicTradingRole sanctioner;
	
	/**
	 * Subject of sanctioning.
	 */
	public OrganicTradingRole subject;
	
	/**
	 * Sanction applied (constant from Sanctions class).
	 */
	public String sanction;
	
	/**
	 * Creates sanction to be administered.
	 * @param sanctioner
	 * @param subject
	 * @param sanction
	 */
	public SanctionAdministration(OrganicTradingRole sanctioner, OrganicTradingRole subject, String sanction) {
		this.sanctioner = sanctioner;
		this.subject = subject;
		if (!Sanctions.sanctions.contains(sanction)) {
			throw new RuntimeException("Illegal sanction type " + sanction + " during instantiation of sanction administration.");
		}
		this.sanction = sanction;
	}

	@Override
	public String toString() {
		return "SanctionAdministration [sanctioner=" + sanctioner + ", subject=" + subject + ", sanction=" + sanction
				+ "]";
	}
	
}

