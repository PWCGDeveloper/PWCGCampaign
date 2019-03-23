package pwcg.campaign.target;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.Mission;

public class GeneralTargetLocationGenerator
{
    public static Coordinate createTargetGeneralLocation(Campaign campaign, Mission mission, Squadron squadron) throws PWCGException 
    {
        Coordinate referenceCoordinate;
        if (mission.getMissionFlightBuilder().getPlayerFlights().size() == 0)
        {
            referenceCoordinate = squadron.determineCurrentPosition(campaign.getDate());
        }
        else
        {
            referenceCoordinate = getReferenceCoordinatesWithinPlayerMissionBox(mission, squadron);
            if (referenceCoordinate == null)
            {
                referenceCoordinate = squadron.determineCurrentPosition(campaign.getDate());
            }
        }
        
        return referenceCoordinate;
    }

    private static Coordinate getReferenceCoordinatesWithinPlayerMissionBox(Mission mission, Squadron squadron) throws PWCGException, PWCGMissionGenerationException
    {
        int missionBoxShrinkage = -8000;
        CoordinateBox missionBorders = mission.getMissionFlightBuilder().getMissionBorders(missionBoxShrinkage);
        Coordinate approximatePosition = missionBorders.chooseCoordinateWithinBox();
        return approximatePosition;
    }
}
