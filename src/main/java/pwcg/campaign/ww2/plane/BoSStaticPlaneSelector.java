package pwcg.campaign.ww2.plane;

import pwcg.campaign.api.IStaticPlane;
import pwcg.campaign.api.IStaticPlaneSelector;
import pwcg.core.exception.PWCGException;

public class BoSStaticPlaneSelector implements IStaticPlaneSelector 
{
	@Override
	public IStaticPlane getStaticPlane(String planeName) throws PWCGException 
	{
		return BoSPlaneAttributeFactory.getStaticPlane(planeName);
	}

}
