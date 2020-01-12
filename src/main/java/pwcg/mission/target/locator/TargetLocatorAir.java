package pwcg.mission.target.locator;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.campaign.shipping.ShippingLane;
import pwcg.campaign.shipping.ShippingLaneManager;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.intercept.InterceptAiCoordinateGenerator;
import pwcg.mission.flight.intercept.InterceptPlayerCoordinateGenerator;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.utils.BehindFriendlyLinesPositionCalculator;

public class TargetLocatorAir
{
    private IFlightInformation flightInformation;

    public TargetLocatorAir (IFlightInformation flightInformation)
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
            InterceptPlayerCoordinateGenerator coordinateGenerator = new InterceptPlayerCoordinateGenerator(flightInformation);
            TargetDefinition interceptedFlightTarget = coordinateGenerator.createTargetCoordinates();
            return interceptedFlightTarget.getTargetPosition();
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

    public Coordinate getSeaLaneCoordinate() throws PWCGException
    {
        Mission mission = flightInformation.getMission();
        ShippingLaneManager shippingLaneManager = PWCGContext.getInstance().getCurrentMap().getShippingLaneManager();        
        ShippingLane selectedShippingLane = shippingLaneManager.getClosestShippingLane(mission.getMissionBorders().getCenter());
        return selectedShippingLane.getShippingLaneBorders().getCoordinateInBox();
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

    public Coordinate getEscortForPlayerRendezvousCoordinate()
    {
        return new Coordinate();
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
        Squadron squadron = flightInformation.getSquadron();
        return squadron.determineCurrentPosition(campaign.getDate());
    }
}
