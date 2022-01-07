package pwcg.mission.target.locator;

import java.util.List;

import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.ground.org.GroundUnitCollection;

public class TargetLocatorAir
{
    private FlightInformation flightInformation;

    public TargetLocatorAir (FlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }
    
    public Coordinate getFrontCoordinate() throws PWCGException
    {
        Coordinate missionCenter = flightInformation.getMission().getMissionBorders().getCenter();
        double missionRadius = flightInformation.getMission().getMissionBorders().getAreaRadius();
        TargetLocationFinder targetLocationFinder = new TargetLocationFinder(
                flightInformation.getCampaign(), flightInformation.getCountry().getSide().getOppositeSide(), missionCenter, missionRadius);
        Coordinate targetWaypoint = targetLocationFinder.findLocationAtFront();
        return targetWaypoint;
    }

    public Coordinate getEnemyTerritoryPatrolCoordinate() throws PWCGException
    {
        Coordinate missionCenter = flightInformation.getMission().getMissionBorders().getCenter();
        double missionRadius = flightInformation.getMission().getMissionBorders().getAreaRadius();        
        TargetLocationFinder targetLocationFinder = new TargetLocationFinder(
                flightInformation.getCampaign(), flightInformation.getCountry().getSide().getOppositeSide(), missionCenter, missionRadius);
        Coordinate targetWaypoint = targetLocationFinder.findTargetCoordinatesBehindEnemyLines();
        return targetWaypoint;
    }

    public Coordinate getFerryAirfieldCoordinate() throws PWCGException
    {
        throw new PWCGException("Ferry missions not implemented");
    }

    public Coordinate getBattleCoordinate() throws PWCGException
    {
        List<GroundUnitCollection> shuffledGroundUnits = flightInformation.getMission().getGroundUnitBuilder().getBattleMissionGroundUnits();
        if (!shuffledGroundUnits.isEmpty())
        {
            Coordinate battleCoordinate = shuffledGroundUnits.get(0).getPosition().copy();
            FrontLinesForMap frontLinesForMap = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(flightInformation.getCampaign().getDate());
            Coordinate targetCoordinate = frontLinesForMap.findClosestFrontCoordinateForSide(battleCoordinate, flightInformation.getCountry().getSide());
            return targetCoordinate;
        }
        else
        {
            return getFrontCoordinate();
        }
    }
}
