package pwcg.mission.target;

import java.util.List;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.mission.ground.BattleSize;

public class AssaultDefinitionRange
{
    private static final int MINIMUM_INDECES_FROM_BATTLE_CENTER = 6;

    public static int determineNumberOfAssaultSegments(BattleSize battleSize, int centerFrontIndex)
    {
        int numAssaults = 1;
        if (centerFrontIndex < MINIMUM_INDECES_FROM_BATTLE_CENTER)
        {
            numAssaults = 1; 
        }
        else
        {
            IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
            numAssaults = productSpecific.getNumAssaultSegments(battleSize);
        }
        return numAssaults;
    }

    public static boolean isInUse(int index, List<Integer> inUseIndeces)
    {
        for (int inUseIndex : inUseIndeces)
        {
            if (index >= (inUseIndex - MINIMUM_INDECES_FROM_BATTLE_CENTER) && index <= (inUseIndex + MINIMUM_INDECES_FROM_BATTLE_CENTER))
            {
                return true;
            }
        }
        return false;
    }
}
