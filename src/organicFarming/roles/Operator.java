package organicFarming.roles;

import java.util.HashMap;

import org.nzdis.micro.inspector.annotations.Inspect;
import org.sofosim.environment.memoryTypes.DiscreteCategoricalListMemory;

import organicFarming.artefacts.Actions;
import organicFarming.artefacts.CertificationStatus;
import organicFarming.artefacts.MemoryKeys;
import organicFarming.artefacts.Message;
import organicFarming.artefacts.Sanctions;
import organicFarming.artefacts.SvoTypes;
import organicFarming.artefacts.ViolationActionListener;
import organicFarming.artefacts.msgObjects.CertificationApplication;
import organicFarming.artefacts.msgObjects.ComplianceObservation;
import organicFarming.artefacts.msgObjects.NonComplianceNotification;
import organicFarming.artefacts.msgObjects.SanctionAdministration;
import organicFarming.Simulation;
import sim.engine.SimState;

public class Operator extends OrganicTradingRole {
	
	/**
	 * Operator's capital
	 */
	@Inspect
	private Float capital;
	
	/**
	 * Monitoring frequency per round. Used for stats.
	 */
	public int monitoringFrequency = 0;
	
	/**
	 * Increases counter for monitoring. To be called when inspected.
	 */
	public void monitoredByOtherAgent() {
		monitoringFrequency++;
	}
	
	/**
	 * Indicates whether agent has been monitored.
	 * @return
	 */
	public boolean hasBeenMonitored() {
		return monitoringFrequency > 0;
	}
	
	/**
	 * Certifier
	 * Note: Has to be public for quick access.
	 */
	@Inspect
	public Certifier certifier = null;
	
	/**
	 * Certification status as per organicFarming.artefacts.CertificationStatus
	 */
	@Inspect
	public String CERTIFICATION_STATUS = CertificationStatus.STATUS_NOT_CERTIFIED;
	
	/**
	 * Indicates whether agent is applying for organic trading license.
	 */
	@Inspect
	private boolean applying = false;
	
	/**
	 * Indicates whether agent has cheated.
	 */
	@Inspect
	public boolean cheating = false;
	
	/**
	 * Indicates whether an agent is monitoring.
	 */
	@Inspect
	public boolean monitoring = false;
	
	/**
	 * Indicates whether agent acted non-compliantly.
	 */
	@Inspect
	public boolean actNonCompliantly = false;
	
	/**
	 * Tracks non-compliance notifications received by agent.
	 */
	@Inspect
	public HashMap<Certifier, NonComplianceNotification> notificationRecord = new HashMap<>();

	/**
	 * Violation listener for operators.
	 */
	private static final ViolationActionListener violationActionListener = new ViolationActionListener() {
		
		@Override
		public void actOnObservedViolation(OrganicTradingRole observer, OrganicTradingRole certifier, OrganicTradingRole actor, String violation) {
			observer.printError("Sending message to " + certifier.me());
			observer.send(new Message(observer, certifier, Message.MSG_TYPE_OBSERVATION, new ComplianceObservation(observer, actor, violation)));
		}
	};
	
	/**
	 * Memory
	 */
	@Inspect
	private DiscreteCategoricalListMemory<String> memory = null;
	
	/**
	 * Constructor for Operator
	 * @param simulation
	 * @param cheater
	 * @param certifier
	 */
	public Operator(Simulation simulation, Boolean cheater, Certifier certifier) {
		super(simulation, cheater);
		this.capital = simulation.setup.STARTING_CAPITAL;
		this.certifier = certifier;
	}
	
	/**
	 * Operate as regular farmer without certification.
	 */
	private void operateWithoutCertification() {
		print("Operate as regular operator");
		LAST_ACTION = Actions.ACTION_OPERATOR_ACT_REGULAR;
	}
	
	/**
	 * Operate as organic farming operator (compliantly, or non-compliantly).
	 * @param cheating Indicates whether agent acts non-compliantly
	 */
	private void operateAsCertifiedOperator(boolean cheating) {
		print("Operate as certified operator");
				
		if (cheating) {
			LAST_ACTION = Actions.ACTION_OPERATOR_ACT_CERTIFIED_CHEATER;
		} else {
			LAST_ACTION = Actions.ACTION_OPERATOR_ACT_CERTIFIED;
		}
	}
	
	/**
	 * Apply for organic trading license.
	 */
	private void applyForOrganicTradingLicense() {
		
		print("Applying for organic trading license.");
		
		if (certifier == null) {
			throw new RuntimeException("Could not find preassigned certifier. Should never happen.");
		}
		
		applying = true; // Switch
		
		// Send application message
		CertificationApplication application = new CertificationApplication(this);
		send(new Message(this, certifier, Message.MSG_TYPE_APPLICATION, application));
	}
	
	/**
	 * Method for processing of application response.
	 * @param application
	 */
	protected void processApplicationResponse(CertificationApplication application) {
		print("Received application response " + application);
		if (application.approved) {
			modifyCertificationStatus(CertificationStatus.STATUS_CERTIFIED);
		} else {
			modifyCertificationStatus(CertificationStatus.STATUS_REJECTED);
		}
		applying = false; // reset switch
	}
	
	/**
	 * Method for processing of sanction.
	 * @param sanction
	 */
	protected void processSanction(SanctionAdministration sanction) {
		print("Received sanction " + sanction);
		switch (sanction.sanction) {
			case Sanctions.SUSPEND_LICENSE:
				modifyCertificationStatus(CertificationStatus.STATUS_SUSPENDED);
				break;
			case Sanctions.REVOKE_LICENSE:
				modifyCertificationStatus(CertificationStatus.STATUS_REVOKED);
				break;
			default: throw new RuntimeException("Unknown sanction " + sanction);
		}
	}
	
	/**
	 * Method for processing incoming notifications.
	 * @param notification
	 */
	protected void processNotification(NonComplianceNotification notification) {
		print("Received notification " + notification);
		notificationRecord.put((Certifier) notification.notifier, notification);
	}

	@Override
	public void stop() {
		
	}

	@Override
	public void step(SimState arg0) {
		
		if (this.memory == null) {
			this.memory = new DiscreteCategoricalListMemory<String>(sim.setup.MEMORY_MAX_ENTRIES);
		} else {
			// Memorising actions & success before resetting those
			if (cheating && hasBeenMonitored()) {
				memory.memorize(MemoryKeys.MEM_CHEATED_MONITORED);
			} else if (cheating && !hasBeenMonitored()) {
				memory.memorize(MemoryKeys.MEM_CHEATED_NOT_MONITORED);
			} else if (!cheating && hasBeenMonitored()) {
				memory.memorize(MemoryKeys.MEM_MONITORED);
			} else if (!cheating && !hasBeenMonitored()) {
				memory.memorize(MemoryKeys.MEM_NO_CHEATING_NO_MONITORING);
			}
		}
		
		// Reset action
		LAST_ACTION = Actions.ACTION_NO_ACTION;
		
		// Reset monitoring count per round
		monitoringFrequency = 0;
		
		// Reset flags
		cheating = false;
		actNonCompliantly = false;
		//monitoring = false; // don't reset to avoid bias towards non-monitoring for competitive individuals
		
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
					processApplicationResponse((CertificationApplication) currentMsg.getMsgObject());
					break;
				case Message.MSG_TYPE_SANCTION:
					processSanction((SanctionAdministration) currentMsg.getMsgObject());
					break;
				case Message.MSG_TYPE_NOTIFICATION:
					processNotification((NonComplianceNotification) currentMsg.getMsgObject());
					break;
				default:
					throw new RuntimeException("Invalid message type for operator " + currentMsg.getMsgType());
			}
			processedMsg++;
		}
		
		//// ## ACTIONS ##
		
		//// CONSIDERATION OF APPLICATION TO ORGANIC PROGRAM
		
		// only apply if not already applying
		if (!applying) {
			
			// only apply if not already certified
			if (!this.CERTIFICATION_STATUS.equals(CertificationStatus.STATUS_CERTIFIED)) {
				/**
				 * TODO
				 * Decision determinants
				 * - Frequency
				 * - Premium
				 * -  
				 * Questions: How long does application take?
				 */
				
				/**
				 * Potential determinant for participation: Community buy-in
				 */
				
				int sumOfParticipatingOperators = 0;
				for (Operator entry: certifier.getPreassignedOperators()) {
					if (entry.CERTIFICATION_STATUS.equals(CertificationStatus.STATUS_CERTIFIED)) {
						sumOfParticipatingOperators++;
					}
				}
				// Percentage of committed fellow operators
				float communityBuyIn = sumOfParticipatingOperators / (float)certifier.getPreassignedOperators().size();
				
				/**
				 * Potential determinant for behaviour: cheating in community
				 */
				
				int sumOfParticipatingCheaters = 0;
				for (Operator entry: certifier.getPreassignedOperators()) {
					if (entry.CERTIFICATION_STATUS.equals(CertificationStatus.STATUS_REVOKED)) {
						sumOfParticipatingCheaters++;
					}
				}
				// Percentage of committed fellow operators
				float detectedCheatersCommunity = sumOfParticipatingCheaters / (float)certifier.getPreassignedOperators().size();
				
				boolean shouldApply = false;
				
				//TODO: To refine
				// SVO
				switch (this.svoAttitude) {
					case SvoTypes.SVO_INDIVIDUALIST:
						// Original idea: Premium determines; Variant 2: detected cheaters in community determine
						shouldApply = !sim.random().nextBoolean(detectedCheatersCommunity);
						break;
					case SvoTypes.SVO_COMPETITIVE:
						// depends on fraction of others partaking - differentiating
						shouldApply = sim.random().nextBoolean(communityBuyIn); 
						break;
					case SvoTypes.SVO_PROSOCIAL:
						// organic as socially beneficial
						shouldApply = true;
						break;
					default: throw new RuntimeException("Unknown SVO type " + this.svoAttitude + " in operator " + me());
				}
				
				//TODO: More explicit social learning?
				
				if (shouldApply) {
					applyForOrganicTradingLicense();
				}
			}
		}	
		
		//// OPERATING
		
		// CHEATING
		
		// Cheating in social environment
		int sumOfCheatingFellowOperators = 0;
		int sumOfDetectedCheaters = 0;
		for (Operator entry: certifier.getPreassignedOperators()) {
			if (entry.cheating) {
				//System.out.println("Detected cheating");
				sumOfCheatingFellowOperators++;
			}
			if (entry.cheating && entry.hasBeenMonitored()) {
				//System.out.println("Detected cheating & caught");
				sumOfDetectedCheaters++;
			}
		}
		
		// Percentage of undetected cheating fellow operators
		float nonDetectedCheatingCommunity = (sumOfCheatingFellowOperators - sumOfDetectedCheaters) / (float)certifier.getPreassignedOperators().size();
		
		/**
		 * Cheating based on SVOs
		 */
		switch (this.svoAttitude) {
			case SvoTypes.SVO_INDIVIDUALIST:
				// Variant 1: purely naive probabilistic
				//cheating = sim.random().nextBoolean(sim.setup.PROBABILITY_CHEATING);
				
				// Variant 2: Individual memory of past cheating performance including social learning - whether one gets away with it
				float nonDetectedProb = memory.getFractionForKey(MemoryKeys.MEM_CHEATED_NOT_MONITORED, 
						MemoryKeys.MEM_CHEATED_NOT_MONITORED, MemoryKeys.MEM_CHEATED_MONITORED);
				// If no initial memory, start with cheating probability 0.5, then increase
				cheating = sim.random().nextBoolean(nonDetectedProb == 0 ? 0.5f : nonDetectedProb);
				break;
			case SvoTypes.SVO_COMPETITIVE:
				// depends on others. The more cheating, the more cheating
				cheating = sim.random().nextBoolean(nonDetectedCheatingCommunity);
				break;
			case SvoTypes.SVO_PROSOCIAL:
				// does not cheat by principle
				cheating = false;
				break;
			default: throw new RuntimeException("Unknown SVO type " + this.svoAttitude + " in operator " + me());
		}
		
		if (cheating) {
			printError("Cheating!" + ", SVO: " + svoAttitude);
		}
		
		//Noise, unintentional cheating by mishap
		if (sim.random().nextBoolean(sim.setup.PROBABILITY_ACCIDENTAL_NON_COMPLIANCE)) {
			cheating = !cheating;
		}
		
		/**
		 * TODO
		 * Compliance motivation:
		 * Individual:
		 * - Motivations for entry (link to SVO?)
		 * 
		 * Social: 
		 * - Others' experience with the system (social learning)
		 * - Others' behaviour (social learning)
		 * - Social pressure, emotions
		 * Enforcement: 
		 * - Facilitation (appeal, mediation) vs. enforcement
		 */
		
		/*if (!notificationRecord.isEmpty()) {
			print("Reduced probability of non-compliant action after notifications.");
			actNonCompliantly = sim.random().nextBoolean(sim.setup.PROBABILITY_NON_COMPLIANT_OPERATION_AFTER_NOTIFICATION);
		} else {
			actNonCompliantly = sim.random().nextBoolean(sim.setup.PROBABILITY_NON_COMPLIANT_OPERATION);
		}*/
		
		// Operate
		if (this.CERTIFICATION_STATUS.equals(CertificationStatus.STATUS_CERTIFIED)) {
			// Act certified, either cheating or not
			operateAsCertifiedOperator(cheating);
		} else {
			// Assumption: non-certified operators don't cheat (i.e. use the label)
			operateWithoutCertification();
		}
		
		//// MONITORING
		
		// Monitor peers
		
		// Cheating in social environment
		int sumOfMonitoringFellowOperators = 0;
		//int sumOfMonitoredMonitors = 0;
		//int sumOfMonitoredFellowOperators = 0;
		for (Operator entry: certifier.getPreassignedOperators()) {
			if (entry.monitoring) {
				sumOfMonitoringFellowOperators++;
			}
			/*if (entry.monitoring && entry.hasBeenMonitored()) {
				sumOfMonitoredMonitors++;
			}*/
			/*if (entry.hasBeenMonitored()) {
				sumOfMonitoredFellowOperators++;
			}*/
		}
		
		// Percentage of monitoring fellow operators
		float monitoringCommunity = sumOfMonitoringFellowOperators / (float)certifier.getPreassignedOperators().size();
		// Percentage of monitored fellow operators
		//float monitoredCommunity = sumOfMonitoredFellowOperators / (float)certifier.getPreassignedOperators().size();
		
		// Percentage of cheating fellow operators
		float cheatingCommunity = sumOfCheatingFellowOperators / (float)certifier.getPreassignedOperators().size();
		
		// Monitor only if certified
		if (sim.setup.MONITORING_BY_PEERS && 
				this.CERTIFICATION_STATUS.equals(CertificationStatus.STATUS_CERTIFIED)) { 
			
			switch (this.svoAttitude) {
				case SvoTypes.SVO_INDIVIDUALIST:
					// Interested in enforcing reputation of community - thus depends on cheating level in community (monitoring is costly)
					monitoring = sim.random().nextBoolean(cheatingCommunity);
					break;
				case SvoTypes.SVO_COMPETITIVE:
					// Depends on others. The more monitoring, the more monitoring
					monitoring = sim.random().nextBoolean(monitoringCommunity);
					break;
				case SvoTypes.SVO_PROSOCIAL:
					// Monitors by principle
					monitoring = true;
					break;
				default: throw new RuntimeException("Unknown SVO type " + this.svoAttitude + " in operator " + me());
			}
			
			// Probabilistic flip in monitoring (noise)
			if (sim.random().nextBoolean(sim.setup.PROBABILITY_MONITORING_FLIP)) {
				monitoring = !monitoring;
			}
			
			if (monitoring) {
				// Monitor operators and report to random certifier
				monitorOperators(sim.setup.MONITORING_NUMBER, null, violationActionListener);
			}
		}
				
		//printError(memory.toString());
		
	}
	
	/**
	 * Modify certification status to specified status.
	 * @param targetStatus
	 */
	private void modifyCertificationStatus(String targetStatus) {
		if (targetStatus.equals(this.CERTIFICATION_STATUS)) {
			print("Desired status and current status are identical.");
		}
		// TODO: Discuss permissible status transitions
		/*if (CERTIFICATION_STATUS.equals(CertificationStatus.STATUS_SUSPENDED) || 
				CERTIFICATION_STATUS.equals(CertificationStatus.STATUS_REVOKED)) {
			
		}*/
		print("Changed certification status to " + targetStatus + ", previous: " + CERTIFICATION_STATUS);
		this.CERTIFICATION_STATUS = targetStatus;
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
