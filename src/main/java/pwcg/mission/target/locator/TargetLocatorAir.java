package pwcg.mission.target.locator;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.campaign.squadron.Company;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.intercept.InterceptAiCoordinateGenerator;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.target.GroundTargetDefinitionFactory;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;
import pwcg.mission.utils.BehindFriendlyLinesPositionCalculator;

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
        TargetLocationFinder targetLocationFinder = new TargetLocationFinder(flightInformation.getCampaign(), getEnemySide(), missionCenter, missionRadius);
        Coordinate targetWaypoint = targetLocationFinder.findLocationAtFront();
        return targetWaypoint;
    }

    public Coordinate getEnemyTerritoryPatrolCoordinate() throws PWCGException
    {
        Coordinate missionCenter = flightInformation.getMission().getMissionBorders().getCenter();
        double missionRadius = flightInformation.getMission().getMissionBorders().getAreaRadius();        
        TargetLocationFinder targetLocationFinder = new TargetLocationFinder(flightInformation.getCampaign(), getEnemySide(), missionCenter, missionRadius);
        Coordinate targetWaypoint = targetLocationFinder.findTargetCoordinatesBehindEnemyLines();
        return targetWaypoint;
    }
    
    public Coordinate getInterceptCoordinate() throws PWCGException
    {
        
        if (flightInformation.isPlayerFlight())
        {
            TargetDefinition interceptedFlightTarget = GroundTargetDefinitionFactory.buildTargetDefinition(flightInformation);;
            return interceptedFlightTarget.getPosition();
        }
        else
        {
            InterceptAiCoordinateGenerator coordinateGenerator = new InterceptAiCoordinateGenerator(flightInformation.getMission());
            Coordinate targetCoordinates = coordinateGenerator.createTargetCoordinates();
            return targetCoordinates;
        }
    }
    
    public Coordinate getTransportAirfieldCoordinate() throws PWCGException
    {
        TransportReferenceLocationSelector transportReferenceLocationSelector = new TransportReferenceLocationSelector(flightInformation);
        return transportReferenceLocationSelector.getTargetCoordinate();
    }

    public Coordinate getFerryAirfieldCoordinate() throws PWCGException
    {
        throw new PWCGException("Ferry missions not implemented");
    }

    public Coordinate getPlayerEscortRendezvousCoordinate() throws PWCGException
    {
        Coordinate nearbyEnemyFrontPosition = getFrontCoordinate();
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int rendezvousDistanceFromFront = productSpecific.getRendezvousDistanceFromFront();
        Coordinate homePosition = flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate());
        return BehindFriendlyLinesPositionCalculator.getPointBehindFriendlyLines(
                nearbyEnemyFrontPosition, homePosition, rendezvousDistanceFromFront, flightInformation.getCampaign().getDate(), getFriendlySide());
    }

    public Coordinate getEscortForPlayerRendezvousCoordinate() throws PWCGException
    {
        return flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate());
    }

    private Side getFriendlySide() throws PWCGException
    {
        return flightInformation.getSquadron().determineSide();
    }

    private Side getEnemySide() throws PWCGException
    {
        return flightInformation.getSquadron().determineEnemySide();
    }

    public Coordinate getScrambleCoordinate() throws PWCGException
    {
        Campaign campaign = flightInformation.getCampaign();
        Company squadron = flightInformation.getSquadron();
        return squadron.determineCurrentPosition(campaign.getDate());
    }

    public Coordinate getBalloonCoordinate(Side oppositeSide) throws PWCGException
    {
        List<GroundUnitCollection> shuffledGroundUnits = flightInformation.getMission().getGroundUnitBuilder().getAllMissionGroundUnits();
        for (GroundUnitCollection groundUnit : shuffledGroundUnits)
        {
            if (groundUnit.getTargetType() == TargetType.TARGET_BALLOON)
            {
                return groundUnit.getPosition();
            }
        }
        
        throw new PWCGException("No balloon found");
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
