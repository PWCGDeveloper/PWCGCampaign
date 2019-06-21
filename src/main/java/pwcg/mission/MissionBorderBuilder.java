package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.RandomNumberGenerator;

public class MissionBorderBuilder 
{
    private Campaign campaign;
    private MissionHumanParticipants participatingPlayers;
	
	public MissionBorderBuilder(Campaign campaign, MissionHumanParticipants participatingPlayers)
	{
        this.participatingPlayers = participatingPlayers;
        this.campaign = campaign;
	}

    public CoordinateBox buildCoordinateBox () throws PWCGException
    {
        Coordinate centralLocation = findAveragePlayerLocation();
        Coordinate missionCenterCoordinate = findMissionCenter(centralLocation);
        CoordinateBox missionBox = CoordinateBox.coordinateBoxFromCenter(missionCenterCoordinate, 50000);
        return missionBox;

    }

    private Coordinate findMissionCenter(Coordinate centralLocation) throws PWCGException
    {
        FrontLinesForMap frontLinesForMap = PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        Coordinate closestFrontLineCoordinate = frontLinesForMap.findClosestFrontCoordinateForSide(centralLocation,Side.AXIS);
        List<FrontLinePoint> nearbyFrontPoints = frontLinesForMap.findClosestFrontPositionsForSide(closestFrontLineCoordinate, 40000.0, Side.AXIS);
        int frontLinePointIndex = RandomNumberGenerator.getRandom(nearbyFrontPoints.size());
        FrontLinePoint centralFrontLinePoint = nearbyFrontPoints.get(frontLinePointIndex);
        Coordinate missionCenterCoordinate = centralFrontLinePoint.getPosition();
        return missionCenterCoordinate;
    }

    private Coordinate findAveragePlayerLocation() throws PWCGException
    {
        List<Coordinate> playerLocations = new ArrayList<>();
        for (SquadronMember player : participatingPlayers.getAllParticipatingPlayers())
        {
            Squadron squadron = player.determineSquadron();
            Coordinate squadronLocation = squadron.determineCurrentAirfieldAnyMap(campaign.getDate()).getPosition();
            playerLocations.add(squadronLocation);
        }
        
        double xSum = 0.0;
        double zSum = 0.0;
        for (Coordinate playerLocation : playerLocations)
        {
            xSum += playerLocation.getXPos();
            zSum += playerLocation.getZPos();
        }
        double xAvg = xSum / playerLocations.size();
        double zAvg = zSum / playerLocations.size();
        
        Coordinate centralLocation = new Coordinate(xAvg, 0.0, zAvg);
        return centralLocation;
    }
}
