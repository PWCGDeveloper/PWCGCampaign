package pwcg.product.rof.plane;

import pwcg.campaign.api.IStaticPlane;
import pwcg.campaign.api.IStaticPlaneSelector;
import pwcg.core.exception.PWCGException;

public class RoFStaticPlaneSelector implements IStaticPlaneSelector 
{
	@Override
	public IStaticPlane getStaticPlane(String planeName) throws PWCGException 
	{
		return RoFPlaneAttributeFactory.getStaticPlane(planeName);
	}

}
