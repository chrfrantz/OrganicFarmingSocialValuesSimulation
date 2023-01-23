package organicFarming.roles;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.nzdis.micro.DefaultSocialRole;
import org.nzdis.micro.inspector.annotations.Inspect;
import org.sofosim.util.RandomHelper;

import organicFarming.artefacts.ViolationActionListener;
import organicFarming.artefacts.Actions;
import organicFarming.artefacts.CertificationStatus;
import organicFarming.artefacts.Message;
import organicFarming.artefacts.SanctioningType;
import organicFarming.artefacts.Violations;
import organicFarming.Simulation;
import sim.engine.Steppable;
import sim.engine.Stoppable;

public abstract class OrganicTradingRole extends DefaultSocialRole implements Steppable, Stoppable {
	
	/** 
	 * Reference to simulation
	 */
	protected Simulation sim;
	
	/**
	 * Indicates whether entity is cheater
	 */
	public boolean cheater;
	
	/**
	 * SVO of individual
	 */
	public String svoAttitude = null;
	
	/**
	 * Last action as per organicFarming.artefacts.Actions
	 */
	@Inspect
	public String LAST_ACTION = Actions.ACTION_NO_ACTION;
	
	/**
	 * Constructor for generic OrganicTradingRole
	 * @param simulation Reference to simulation
	 * @param cheater Indicator whether role is cheater
	 */
	public OrganicTradingRole(Simulation simulation, Boolean cheater) {
		this.sim = simulation;
		this.cheater = cheater;
	}
	
	/**
	 * List of received messages.
	 */
	private List<Message> receivedMessages = new LinkedList<>();

	/**
	 * Method handling receipt of message by any entity. 
	 * To be used to send messages to an agent.
	 * @param message
	 */
	public void receiveMessage(Message message) {
		print("Received message " + message);
		this.receivedMessages.add(message);
	}
	
	/**
	 * Indicates whether agent has unprocessed message(s).
	 * @return
	 */
	protected boolean haveUnprocessedMessage() {
		return !this.receivedMessages.isEmpty();
	}
	
	/**
	 * Returns number of unprocessed messages in agent's message queue.
	 */
	public int getNumberOfQueuedMessages() {
		return this.receivedMessages.size();
	}
	
	/**
	 * Returns first message in list and removes it.
	 * Used internally by agent to process received messages.
	 * @return
	 */
	protected Message getNextMessage() {
		if (!this.receivedMessages.isEmpty()) {
			print("Processing next message " + this.receivedMessages.get(0));
			return this.receivedMessages.remove(0);
		}
		return null;
	}
	
	/**
	 * Send message. Message needs to contain sender and receiver information.
	 * @param message
	 */
	protected void send(Message message) {
		if (message.getRecipient() == null) {
			throw new RuntimeException("Message does not contain recipient. Message: " + message);
		}
		print("Sending message to " + message.getRecipient().me());
		message.getRecipient().receiveMessage(message);
	}
	
	//// SCENARIO-SPECIFIC FUNCTIONALITY
	
	/**
	 * Sets individual's social value orientation. 
	 * @param svo
	 */
	public void setSVO(String svo) {
		this.svoAttitude = svo;
	}
	
	/**
	 * Initiates monitoring of operators.
	 * @param number Number of fellow operators to be monitored
	 * @param certifierToReportTo Certifier to report to. If null, a random certifier will be picked.
	 * 
	 */
	protected void monitorOperators(int number, Certifier certifierToReportTo, ViolationActionListener violationActionListener) {
		
		Certifier certifier = certifierToReportTo;
				
		if (certifierToReportTo == null) {
			// Pick random notification target
			certifier = (Certifier) RandomHelper.getRandomElement(sim.certifierDirectory.values());
		}
		
		// If the call comes from inspector, take certifiers sanctioning strategy as proxy for number of observations/enforcements.
		if (number == -1) {
			switch (certifier.getSanctioningType()) {
				case SanctioningType.HARD:
					number = 3;
					break;
				case SanctioningType.MEDIUM:
					number = 2;
					break;
				case SanctioningType.SOFT:
					number = 1;
					break;
				default: throw new RuntimeException("Unknown sanctioning type: " + certifier.getSanctioningType());
			}
		}
		
		// Pick random operators
		ArrayList<Operator> randomOperators = RandomHelper.getRandomElements(number, new ArrayList<Operator>(sim.operatorDirectory.values()), true);
		
		// Iterate through operators and assess their operational compliance
		for (Operator observee: randomOperators) {
			switch (observee.LAST_ACTION) {
				case Actions.ACTION_OPERATOR_ACT_CERTIFIED:
					if (!observee.CERTIFICATION_STATUS.equals(CertificationStatus.STATUS_CERTIFIED)) {
						// illegitimate activity
						print("Detected illegal activity (uncertified agent " + observee.me() + " acting as certified one).");
						violationActionListener.actOnObservedViolation(this, certifier, observee, Violations.UNCERTIFIED_ACTING_AS_CERTIFIED);
					} else {
						print(observee.me() + " acted legitimately as certified operator.");
					}
					break;
				case Actions.ACTION_OPERATOR_ACT_REGULAR:
					if (observee.CERTIFICATION_STATUS.equals(CertificationStatus.STATUS_CERTIFIED)) {
						// illegitimate activity
						print("Detected illegal activity (certified agent " + observee.me() + " acting as uncertified one).");
						violationActionListener.actOnObservedViolation(this, certifier, observee, Violations.CERTIFIED_ACTING_AS_UNCERTIFIED);
						// Record that agent has been observed
						observee.monitoredByOtherAgent();
					} else {
						print(observee.me() + " acted legitimately as regular operator.");
					}
					break;
				case Actions.ACTION_OPERATOR_ACT_CERTIFIED_CHEATER:
					if (observee.CERTIFICATION_STATUS.equals(CertificationStatus.STATUS_CERTIFIED)) {
						print("Detected non-compliant operation by certified operation " + observee.me());
						violationActionListener.actOnObservedViolation(this, certifier, observee, Violations.CERTIFIED_ACTING_NON_COMPLIANT);
					} else {
						throw new RuntimeException("Invalid action: Uncertified operator cannot act non-compliantly.");
					}
					break;
				case Actions.ACTION_NO_ACTION:
					// no action
					break;
				default: throw new RuntimeException("Unknown action " + observee.LAST_ACTION + " by " + observee.me());
			}
			// Record that agent has been observed
			observee.monitoredByOtherAgent();
		}
	}
	
}
