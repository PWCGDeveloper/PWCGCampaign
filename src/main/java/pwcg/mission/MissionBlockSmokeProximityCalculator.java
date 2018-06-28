package pwcg.mission;

import java.util.List;

import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.mcu.group.SmokeGroup;

public class MissionBlockSmokeProximityCalculator
{
    private ConfigManagerCampaign configManagerCampaign;
    private List<SmokeGroup> smokingPositions;
        
    public MissionBlockSmokeProximityCalculator(ConfigManagerCampaign configManagerCampaign, List<SmokeGroup> smokingPositions)
    {
        this.configManagerCampaign = configManagerCampaign;        
        this.smokingPositions = smokingPositions;        
    }
    
    public boolean isCoordinateSaturated(Coordinate newSmokeCoordinate) throws PWCGException
    {                
        int nearbySmokeCount = getSmokeCountNearRequestedPosition(newSmokeCoordinate);
        
        int maxSmokingPositionsInArea = configManagerCampaign.getIntConfigParam(ConfigItemKeys.MaxSmokeInAreaKey);
        if (nearbySmokeCount < maxSmokingPositionsInArea)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    private int getSmokeCountNearRequestedPosition(Coordinate newSmokeCoordinate)
    {
        int nearbySmokeCount = 0;
        for (SmokeGroup smokeGroup : smokingPositions)
        {
            double distance = MathUtils.calcDist(newSmokeCoordinate, smokeGroup.getPosition());
            if (distance < 3000.0)
            {
                ++nearbySmokeCount;
            }
        }
        return nearbySmokeCount;
    }
}
