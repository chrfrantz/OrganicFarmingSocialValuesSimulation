package organicFarming.roles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.nzdis.micro.inspector.annotations.Inspect;
import org.sofosim.util.RandomHelper;

import organicFarming.artefacts.Actions;
import organicFarming.artefacts.CertificationStatus;
import organicFarming.artefacts.Message;
import organicFarming.artefacts.SanctioningType;
import organicFarming.artefacts.Sanctions;
import organicFarming.artefacts.ViolationActionListener;
import organicFarming.artefacts.Violations;
import organicFarming.artefacts.msgObjects.CertificationApplication;
import organicFarming.artefacts.msgObjects.ComplianceObservation;
import organicFarming.artefacts.msgObjects.NonComplianceNotification;
import organicFarming.artefacts.msgObjects.SanctionAdministration;
import organicFarming.Simulation;
import sim.engine.SimState;

public class Certifier extends OrganicTradingRole {
	
	/**
	 * Maximum number of managed operators.
	 */
	@Inspect
	private int maxOperators = 0;
	
	/**
	 * Number of currently managed operators.
	 * Note: Has to be public for graph generation.
	 */
	@Inspect
	public ArrayList<Operator> managedOperators = new ArrayList<>();
	
	/**
	 * Number of currently managed inspectors.
	 * Note: Has to be public for graph generation.
	 */
	@Inspect
	public ArrayList<Inspector> managedInspectors = new ArrayList<>();
	
	/**
	 * Violation listener for certifiers.
	 */
	private static final ViolationActionListener violationActionListener = new ViolationActionListener() {
		
		@Override
		public void actOnObservedViolation(OrganicTradingRole observer, OrganicTradingRole certifier, OrganicTradingRole actor, String violation) {
			((Certifier)observer).processComplianceObservation(new ComplianceObservation(observer, actor, violation));
		}
		
	};
	
	/**
	 * Sanctioning type as described in SanctioningType.
	 */
	@Inspect
	private String sanctioningType;
	
	/**
	 * Constructor for Certifier
	 * @param simulation
	 * @param cheater
	 * @param maxOperators
	 */
	public Certifier(Simulation simulation, Boolean cheater, Integer maxOperators) {
		super(simulation, cheater);
		this.maxOperators = maxOperators;
	}
	
	/**
	 * Sets the sanctioning type for an individual agent.
	 * @param sanctioningType
	 */
	public void setSanctioningType(String sanctioningType) {
		this.sanctioningType = sanctioningType;
	}
	
	/**
	 * Returns sanctioning type of given certifier.
	 * @return
	 */
	public String getSanctioningType() {
		return this.sanctioningType;
	}
	
	/**
	 * Adds preassigned inspectors.
	 * @param inspector
	 */
	public void addPreassignedInspector(Inspector inspector) {
		if (this.managedInspectors.contains(inspector)) {
			throw new RuntimeException("Repeated addition of inspector to certifier attempted.");
		}
		this.managedInspectors.add(inspector);
	}
	
	/**
	 * Returns preassigned inspectors.
	 */
	public List<Inspector> getPreassignedInspectors() {
		return this.managedInspectors;
	}
	
	/**
	 * Adds preassigned operators.
	 * @param operator
	 */
	public void addPreassignedOperator(Operator operator) {
		if (this.managedOperators.contains(operator)) {
			throw new RuntimeException("Repeated addition of operator to certifier attempted.");
		}
		this.managedOperators.add(operator);
	}
	
	/**
	 * Returns preassigned operators.
	 */
	public List<Operator> getPreassignedOperators() {
		return this.managedOperators;
	}
	
	/**
	 * Returns the maximum number of operators managed by certifier.
	 * @return
	 */
	public int getMaxOperators() {
		return this.maxOperators;
	}
	
	/**
	 * Holds all processed applications.
	 */
	@Inspect
	private HashMap<OrganicTradingRole, CertificationApplication> processedApplications = new HashMap<>();
	
	/**
	 * Holds all reported violations by non-certified operators.
	 */
	@Inspect
	private HashMap<OrganicTradingRole, SanctionAdministration> nonComplianceViolationsOfNonCertifiedOperators = new HashMap<>();
	
	/**
	 * Holds all notifications sent to non-compliant certified operations.
	 */
	@Inspect
	private HashMap<OrganicTradingRole, NonComplianceNotification> sentNotifications = new HashMap<>();
		
	/**
	 * Invoked for applications for certification.
	 * @param application
	 */
	private void processApplication(CertificationApplication application) {
		
		// Register certifier
		application.certifier = this;
		
		//TODO: Decide on application
		boolean approve = false;
		// Decide on approval depending on certification status of applicant (and correspondingly varying probabilities)
		switch (((Operator)application.applicant).CERTIFICATION_STATUS) {
			case CertificationStatus.STATUS_NOT_CERTIFIED:
				print("Applicant " + application.applicant.me() + " applies for the first time.");
				approve = sim.random().nextBoolean(sim.setup.PROBABILITY_APPLICATION_APPROVAL);
				break;
			case CertificationStatus.STATUS_CERTIFIED:
				throw new RuntimeException("Invalid state: Certified operator should not be able to apply again.");
			case CertificationStatus.STATUS_REJECTED:
				print("Applicant " + application.applicant.me() + " applies after REJECT.");
				approve = sim.random().nextBoolean(sim.setup.PROBABILITY_APPLICATION_APPROVAL);
				break;
			case CertificationStatus.STATUS_REVOKED:
				if (nonComplianceViolationsOfNonCertifiedOperators.containsKey(application.applicant)) {
					// Has the applicant cheated while being non-certified operator?
					print("Applicant " + application.applicant.me() + " applies after action as non-certified non-compliant operator.");
					approve = sim.random().nextBoolean(sim.setup.PROBABILITY_APPLICATION_APPROVAL_AFTER_BEING_NON_COMPLIANT_REGULAR_OPERATOR);
				} else {
					// Has license been genuinely revoked?
					print("Applicant " + application.applicant.me() + " applies after REVOCATION or action as non-certified non-compliant operator.");
					approve = sim.random().nextBoolean(sim.setup.PROBABILITY_APPLICATION_APPROVAL_AFTER_REVOCATION);
				}
				break;
			case CertificationStatus.STATUS_SUSPENDED:
				print("Applicant " + application.applicant.me() + " applies after SUSPENSION.");
				approve = sim.random().nextBoolean(sim.setup.PROBABILITY_APPLICATION_APPROVAL_AFTER_SUSPENSION);
				break;
			default: throw new RuntimeException("Invalid certification status " + ((Operator)application.applicant).CERTIFICATION_STATUS +
					" by applicant " + application.applicant.me());
		}
		
		if (approve) {
			print("Application by " + application.applicant.me() + " has been approved.");
		} else {
			print("Application by " + application.applicant.me() + " has NOT been approved.");
		}
		application.approved = approve; // Store result
		
		// Keep track of application (perhaps relevant for cheating assessment?)
		//processedApplications.put(application.applicant, application);
		
		// Respond the requester
		send(new Message(this, application.applicant, Message.MSG_TYPE_APPLICATION, application));
		
		LAST_ACTION = Actions.ACTION_CERTIFIER_ACT_PROCESS_APPLICATION;
	}
	
	/**
	 * Invoked for processing compliance or violations.
	 * @param observation
	 */
	private void processComplianceObservation(ComplianceObservation observation) {
		
		print("Processing compliance observation ...");

		switch (observation.violationType) {
			case Violations.CERTIFIED_ACTING_AS_UNCERTIFIED:
				// Depends on certifier attitude
				String sanction;
				switch (sanctioningType) {
					case SanctioningType.HARD:
						// Outright revoke license
						sanction = Sanctions.REVOKE_LICENSE;
						break;
					case SanctioningType.MEDIUM:
						// Pick sanction probabilistically
						sanction = RandomHelper.getRandomElementWithWeightedInput(Sanctions.sanctions, 
							sim.setup.PROBABILITY_CERTIFICATION_SUSPENSION, sim.setup.PROBABILITY_CERTIFICATION_REVOCATION);
						break;
					case SanctioningType.SOFT:
						// Throw dice as to whether suspension is applied
						sanction = sim.random().nextBoolean(sim.setup.PROBABILITY_APPLICATION_APPROVAL_AFTER_SUSPENSION) ? 
								Sanctions.SUSPEND_LICENSE : null;
						break;
					default: throw new RuntimeException("Unknown sanction type.");
				}
				// If sanction is null, nothing is returned
				if (sanction == null) {
					return;
				}
				// Sending sanction
				Message msg = new Message(this, observation.observee, Message.MSG_TYPE_SANCTION, 
						new SanctionAdministration(this, observation.observee, sanction));
				print("Administering sanction " + sanction + " against " + observation.observee.me());
				send(msg);
				break;
			case Violations.UNCERTIFIED_ACTING_AS_CERTIFIED:
				Message msg1 = new Message(this, observation.observee, Message.MSG_TYPE_SANCTION, 
						new SanctionAdministration(this, observation.observee, Sanctions.REVOKE_LICENSE));
				print("Sending notification to " + observation.observee);
				send(msg1);
				// Storing reference
				nonComplianceViolationsOfNonCertifiedOperators.put(observation.observee, (SanctionAdministration) msg1.getMsgObject());
				break;
			case Violations.CERTIFIED_ACTING_NON_COMPLIANT:
				if (sentNotifications.containsKey(observation.observee)) {
					// 1-strike policy. Cheaters are out
					printError("Repeated non-compliance offence by " + observation.observee.me() + ". Reacting with sanction ...");
					// Redirect to sanctioning
					observation.violationType = Violations.CERTIFIED_ACTING_AS_UNCERTIFIED;
					processComplianceObservation(observation);
					break;
				}
				// Sending notification (if not already received before)
				Message msg2 = new Message(this, observation.observee, Message.MSG_TYPE_NOTIFICATION, 
						new NonComplianceNotification(this, observation.observee));
				print("Sending notification to " + observation.observee);
				send(msg2);
				// Storing reference
				sentNotifications.put(observation.observee, (NonComplianceNotification) msg2.getMsgObject());
				break;
			default: throw new RuntimeException("Unknown violation " + observation.violationType);
		}
		LAST_ACTION = Actions.ACTION_CERTIFIER_ACT_PROCESS_OBSERVATION;
	}
	
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void step(SimState arg0) {
		
		// Reset action
		LAST_ACTION = Actions.ACTION_NO_ACTION;
		
		//// MESSAGE PROCESSING
		
		// Counter for processed message
		int processedMsg = 0;
		
		while (haveUnprocessedMessage() && 
				(sim.setup.PROCESSED_MESSAGES_PER_ROUND == -1 ||
				processedMsg < sim.setup.PROCESSED_MESSAGES_PER_ROUND)) {
			
			// Retrieve next message
			Message currentMsg = getNextMessage();
			
			switch (currentMsg.getMsgType()) {
				case Message.MSG_TYPE_APPLICATION:
					processApplication((CertificationApplication) currentMsg.getMsgObject());
					break;
				case Message.MSG_TYPE_OBSERVATION:
					processComplianceObservation((ComplianceObservation) currentMsg.getMsgObject());
					break;
				default:
					throw new RuntimeException("Invalid message type for certifier " + currentMsg.getMsgType());
			}
			processedMsg++;
		}
		
		// no further action
	}
	
	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void release() {
		// TODO Auto-generated method stub
		
	}

}
