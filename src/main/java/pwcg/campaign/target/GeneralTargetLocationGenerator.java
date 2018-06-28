package pwcg.campaign.target;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;

public class GeneralTargetLocationGenerator
{
    public static Coordinate createTargetGeneralLocation(Campaign campaign, Mission mission, Squadron squad) throws PWCGException 
    {
        Coordinate referenceCoordinate;
        if (squad.getSquadronId() == campaign.getSquadronId())
        {
            referenceCoordinate = squad.determineCurrentPosition(campaign.getDate());
        }
        else
        {
            referenceCoordinate = getReferenceCoordinatesWithinPlayerMissionBox(mission);
            if (referenceCoordinate == null)
            {
                referenceCoordinate = squad.determineCurrentPosition(campaign.getDate());
            }
        }
        
        return referenceCoordinate;
    }

    private static Coordinate getReferenceCoordinatesWithinPlayerMissionBox(Mission mission) throws PWCGException, PWCGMissionGenerationException
    {
        Coordinate approximatePosition;
        int missionBoxShrinkage = -8000;
        CoordinateBox missionBorders = mission.getMissionFlightBuilder().getMissionBorders(missionBoxShrinkage);
        if (missionBorders.getSW() != null && missionBorders.getNE() != null)
        {
            int distanceX = new Double(missionBorders.getNE().getXPos() - missionBorders.getSW().getXPos()).intValue();
            int distanceZ = new Double(missionBorders.getNE().getZPos() - missionBorders.getSW().getZPos()).intValue();
            
            int offsetX = RandomNumberGenerator.getRandom(distanceX);
            int offsety = RandomNumberGenerator.getRandom(distanceZ);
            
            approximatePosition = missionBorders.getSW().copy();
            approximatePosition.setXPos(approximatePosition.getXPos() + offsetX);
            approximatePosition.setZPos(approximatePosition.getZPos() + offsety);
        }
        else
        {
            approximatePosition = mission.getMissionFlightBuilder().getPlayerFlight().getCoordinatesToIntersectWithPlayer();
        }
        return approximatePosition;
    }
}
