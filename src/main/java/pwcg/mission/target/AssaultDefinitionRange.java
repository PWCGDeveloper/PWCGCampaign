package pwcg.mission.target;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.ground.BattleSize;

public class AssaultDefinitionRange
{
    private static final int MAXIMUM_ASSAULT_SEGMENTS = 3;
    private static final int MINIMUM_INDECES_FROM_BATTLE_CENTER = 10;

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
            if (numAssaults > MAXIMUM_ASSAULT_SEGMENTS)
            {
                numAssaults = MAXIMUM_ASSAULT_SEGMENTS;
            }
        }
        return numAssaults;
    }

    public static int determineCenterOfBattle(Campaign campaign, ICountry defendingCountry, Coordinate battleLocation) throws PWCGException
    {
        FrontLinesForMap frontLineMarker = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        List<FrontLinePoint> frontLines = frontLineMarker.getFrontLines(defendingCountry.getSide());
        int centerFrontIndex = frontLineMarker.findIndexForClosestPosition(battleLocation, defendingCountry.getSide());
        if (centerFrontIndex < MINIMUM_INDECES_FROM_BATTLE_CENTER)
        {
            centerFrontIndex = MINIMUM_INDECES_FROM_BATTLE_CENTER;
        }

        if (centerFrontIndex > (frontLines.size() - MINIMUM_INDECES_FROM_BATTLE_CENTER))
        {
            centerFrontIndex = frontLines.size() - MINIMUM_INDECES_FROM_BATTLE_CENTER;
        }
        
        centerFrontIndex = accountForVerySmallFrontLines(frontLines, centerFrontIndex);

        return centerFrontIndex;
    }

    public static boolean isInBattleArea(List<FrontLinePoint> frontLines, int centerFrontIndex)
    {
        if (centerFrontIndex < MINIMUM_INDECES_FROM_BATTLE_CENTER || centerFrontIndex > (frontLines.size() - MINIMUM_INDECES_FROM_BATTLE_CENTER))
        {
            return false;
        }
        return true;
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

    private static int accountForVerySmallFrontLines(List<FrontLinePoint> frontLines, int centerFrontIndex)
    {
        if (isInBattleArea(frontLines, centerFrontIndex))
        {
            centerFrontIndex = frontLines.size() / 2;
        }
        return centerFrontIndex;
    }


}
