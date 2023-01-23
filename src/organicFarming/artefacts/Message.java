package organicFarming.artefacts;

import organicFarming.roles.OrganicTradingRole;

public class Message {
	
	/**
	 * Indicates an application message.
	 */
	public static final String MSG_TYPE_APPLICATION = "MSG_APPLICATION";
	/**
	 * Indicates an observation message.
	 */
	public static final String MSG_TYPE_OBSERVATION = "MSG_OBSERVATION";
	/**
	 * Indicates a notification message.
	 */
	public static final String MSG_TYPE_NOTIFICATION = "MSG_NOTIFICATION";
	/**
	 * Indicates a sanction message.
	 */
	public static final String MSG_TYPE_SANCTION = "MSG_SANCTION";
	
	/**
	 * Sender of message.
	 */
	private OrganicTradingRole sender;
	
	/**
	 * Recipient of message.
	 */
	private OrganicTradingRole recipient;
	
	/**
	 * Type of message as identified by constants in Message class.
	 */
	private String msgType;
	
	/**
	 * Object contained in messages.
	 */
	private Object msgObject;
	
	/**
	 * Creates new instance of message with with message sender, recipient, type (constant specified in Message class), and object.
	 * @param sender
	 * @param recipient
	 * @param msgType
	 * @param msgObject
	 */
	public Message(OrganicTradingRole sender, OrganicTradingRole recipient, String msgType, Object msgObject) {
		this.sender = sender;
		this.recipient = recipient;
		this.msgType = msgType;
		this.msgObject = msgObject;
	}
	
	/**
	 * Returns sender information.
	 * @return
	 */
	public OrganicTradingRole getSender() {
		return sender;
	}

	/**
	 * Sets message sender.
	 * @param sender
	 */
	public void setSender(OrganicTradingRole sender) {
		this.sender = sender;
	}

	/**
	 * Returns the recipient of the message.
	 * @return
	 */
	public OrganicTradingRole getRecipient() {
		return recipient;
	}

	/**
	 * Sets the recipient of the message.
	 * @param recipient
	 */
	public void setRecipient(OrganicTradingRole recipient) {
		this.recipient = recipient;
	}

	/**
	 * Get message type.
	 * @return
	 */
	public String getMsgType() {
		return this.msgType;
	}
	
	/**
	 * Get message object.
	 * @return
	 */
	public Object getMsgObject() {
		return this.msgObject;
	}

	@Override
	public String toString() {
		return "Message [sender=" + sender + ", msgType=" + msgType + ", msgObject=" + msgObject + "]";
	}
	
}
