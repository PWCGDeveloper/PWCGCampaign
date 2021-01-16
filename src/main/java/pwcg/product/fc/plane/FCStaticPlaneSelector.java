package pwcg.product.fc.plane;

import pwcg.campaign.api.IStaticPlane;
import pwcg.campaign.api.IStaticPlaneSelector;
import pwcg.core.exception.PWCGException;

public class FCStaticPlaneSelector implements IStaticPlaneSelector 
{
	@Override
	public IStaticPlane getStaticPlane(String planeName) throws PWCGException 
	{
		return FCPlaneAttributeFactory.getStaticPlane(planeName);
	}

    @Override
    public boolean isStaticPlane(String planeType) throws PWCGException 
    {
        return FCPlaneAttributeFactory.isStaticPlane(planeType);
    }

}
