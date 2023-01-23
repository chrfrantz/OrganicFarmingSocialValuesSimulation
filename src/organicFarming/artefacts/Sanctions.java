package organicFarming.artefacts;

import java.util.ArrayList;
import java.util.List;

public class Sanctions {

	/**
	 * Constant indicating suspended license
	 */
	public static final String SUSPEND_LICENSE = "SUSPEND";
	
	/**
	 * Constant indicating revoked license
	 */
	public static final String REVOKE_LICENSE = "REVOKE";
	
	/**
	 * List of sanctions
	 */
	public static final List<String> sanctions = new ArrayList<>();
	
	static {
		sanctions.add(SUSPEND_LICENSE);
		sanctions.add(REVOKE_LICENSE);
	}
}
