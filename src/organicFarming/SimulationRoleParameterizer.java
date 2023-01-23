package organicFarming;

import java.util.ArrayList;

public interface SimulationRoleParameterizer {

	/**
	 * Method to prepare parameterisation based on external source for given role class.
	 * @param clazz Role class for which instantiation parameters are required.
	 * @return
	 */
	public abstract ArrayList instantiate(Class clazz);
	
}
