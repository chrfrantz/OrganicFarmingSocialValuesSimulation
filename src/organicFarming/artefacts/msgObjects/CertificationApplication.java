package organicFarming.artefacts.msgObjects;

import organicFarming.roles.OrganicTradingRole;

public class CertificationApplication {

	/**
	 * Applicant
	 */
	public final OrganicTradingRole applicant;
	/**
	 * Certifier
	 */
	public OrganicTradingRole certifier;
	/**
	 * Indicates approval status: null --> not processed, true --> approved, false --> not approved
	 */
	public Boolean approved = null;
	
	/**
	 * Constructor for applicant
	 * @param applicant
	 */
	public CertificationApplication(OrganicTradingRole applicant) {
		this.applicant = applicant;
	}
	
	@Override
	public String toString() {
		return "CertificationApplication [applicant=" + applicant + ", certifier=" + certifier + ", approved="
				+ approved + "]";
	}

}
