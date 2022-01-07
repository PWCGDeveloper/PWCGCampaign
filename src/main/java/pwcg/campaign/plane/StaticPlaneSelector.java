package pwcg.campaign.plane;

import pwcg.campaign.api.IStaticPlane;
import pwcg.campaign.api.IStaticPlaneSelector;
import pwcg.core.exception.PWCGException;

public class StaticPlaneSelector implements IStaticPlaneSelector 
{
    @Override
    public IStaticPlane getStaticPlane(String planeName) throws PWCGException 
    {
        return PlaneAttributeFactory.getStaticPlane(planeName);
    }

    @Override
    public boolean isStaticPlane(String planeType) throws PWCGException 
    {
        return PlaneAttributeFactory.isStaticPlane(planeType);
    }
}
