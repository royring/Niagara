package kdde.niagara.mining;

import org.apache.commons.lang.StringUtils;

import kdde.niagara.ming.contrast.mdsp.GapConstraintMiner;

public class SqMiningFactory {
 
	public static Miner createMiner(String mineName){
		if (StringUtils.isEmpty(mineName)) {
			return null;
		}
		switch (mineName) {
		case "MDSP-CGC":
			return new GapConstraintMiner();
		default:
			break;
		}
		
		return null;
	}
}
