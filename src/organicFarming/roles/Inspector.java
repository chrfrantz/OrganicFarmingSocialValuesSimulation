package organicFarming.roles;

import java.util.ArrayList;
import java.util.List;

import org.nzdis.micro.inspector.annotations.Inspect;

import organicFarming.artefacts.Actions;
import organicFarming.artefacts.Message;
import organicFarming.artefacts.ViolationActionListener;
import organicFarming.artefacts.msgObjects.ComplianceObservation;
import organicFarming.Simulation;
import sim.engine.SimState;

public class Inspector extends OrganicTradingRole {

	/**
	 * Certifiers related to inspector.
	 * Note: Field has to be public for graph generation.
	 */
	@Inspect
	public ArrayList<Certifier> associatedCertifiers = new ArrayList<>();
	
	/**
	 * Violation listener for certifiers.
	 */
	private static final ViolationActionListener violationActionListener = new ViolationActionListener() {
		
		@Override
		public void actOnObservedViolation(OrganicTradingRole observer, OrganicTradingRole certifier, OrganicTradingRole actor, String violation) {
			observer.printError("Sending message to " + actor.me() + "'s certifier " + ((Operator)actor).certifier.me());
			observer.send(new Message(observer, ((Operator)actor).certifier, Message.MSG_TYPE_OBSERVATION, new ComplianceObservation(observer, actor, violation)));
		}
		
	};
	
	/**
	 * Constructor for Inspector
	 * @param simulation
	 * @param cheater
	 */
	public Inspector(Simulation simulation, Boolean cheater) {
		super(simulation, cheater);
	}
	
	/**
	 * Adds preassigned operators.
	 * @param operator
	 */
	public void addPreassignedCertifier(Certifier certifier) {
		if (this.associatedCertifiers.contains(certifier)) {
			throw new RuntimeException("Repeated addition of certifier to operator attempted.");
		}
		this.associatedCertifiers.add(certifier);
	}
	
	/**
	 * Returns preassigned operators.
	 */
	public List<Certifier> getPreassignedCertifiers() {
		return this.associatedCertifiers;
	}


	@Override
	public void step(SimState arg0) {
		
		
		//// ## ACTIONS ##
		
		// Monitor operators
		if (sim.setup.MONITORING_BY_INSPECTORS && sim.random().nextBoolean(sim.setup.PROBABILITY_MONITORING_BY_INSPECTORS)) {
			// Monitor operators and sent notifications to relevant certifier
			monitorOperators(-1, null, violationActionListener);
			// Indicate that I have been monitoring
			LAST_ACTION = Actions.ACTION_MONITORING;
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
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
