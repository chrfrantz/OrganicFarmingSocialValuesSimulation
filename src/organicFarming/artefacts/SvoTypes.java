package organicFarming.artefacts;

import java.util.ArrayList;

public class SvoTypes {
	
	public static final String SVO_INDIVIDUALIST = "Individualist"; // profits (51%), avoiding penalty for wrong marketing (32%)
	public static final String SVO_PROSOCIAL = "Prosocial"; // supporting organic movement (53%)
	public static final String SVO_COMPETITIVE = "Competitive"; // differentiation (75%)
	//public static final String SVO_ALTRUISTIC = "Altruistic"; // 0% 
	
	public static ArrayList<String> SVOs = new ArrayList<>();
	
	static {
		SVOs.add(SVO_INDIVIDUALIST);
		SVOs.add(SVO_PROSOCIAL);
		SVOs.add(SVO_COMPETITIVE);
	}

}
