package pwcg.campaign.skirmish;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;

public class SkirmishDistance
{
    public static int findMaxSkirmishDistance() throws PWCGException
    {
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int closeToBattleDistance = productSpecific.getCloseToBattleDistance();
        return closeToBattleDistance;
    }
}
