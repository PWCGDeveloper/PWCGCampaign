package pwcg.mission.flight.offensive;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IFixedPosition;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.Bridge;
import pwcg.campaign.group.GroupManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.end.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointRouteSet;
import pwcg.mission.mcu.McuWaypoint;

public class OffensiveWaypointsTransportFactory
{
    private IFlight flight;
    private MissionPointRouteSet missionPointSet = new MissionPointRouteSet();
    
    public OffensiveWaypointsTransportFactory(IFlight flight) throws PWCGException
    {
        this.flight = flight;
    }

    public IMissionPointSet createWaypoints(McuWaypoint ingressWaypoint) throws PWCGException
    {
        missionPointSet.addWaypoint(ingressWaypoint);
        
        List<McuWaypoint> waypoints = createTargetWaypoints(ingressWaypoint.getPosition());
        missionPointSet.addWaypoints(waypoints);

        McuWaypoint egressWaypoint = EgressWaypointGenerator.createEgressWaypoint(flight, ingressWaypoint.getPosition());
        missionPointSet.addWaypoint(egressWaypoint);

        return missionPointSet;
    }

    private List<McuWaypoint> createTargetWaypoints(Coordinate ingressPosition) throws PWCGException
    {
        Campaign campaign = flight.getCampaign();
        List<McuWaypoint> targetWaypoints = new ArrayList<McuWaypoint>();

        List <IFixedPosition> allFixedPositionsInRadius = getTransportPositionsWithinRadius();
        
        SortedFixedPositions sortedFixedPositions = new SortedFixedPositions();
        sortedFixedPositions.sortPositionsByDirecton(allFixedPositionsInRadius);
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        List <IFixedPosition> sortedPositions = sortedFixedPositions.getSortedPositionsWithMaxDistanceTTravelled(
                campaign, flight.getFlightData().getFlightInformation().getTargetPosition(), productSpecific.getSmallMissionRadius());

        for (IFixedPosition transportBlock : sortedPositions)
        {
            McuWaypoint offensiveWaypoint = createWP(transportBlock.getPosition());
            targetWaypoints.add(offensiveWaypoint);
        }
        return targetWaypoints;
	}

    private List <IFixedPosition> getTransportPositionsWithinRadius() throws PWCGException 
    {
        Campaign campaign = flight.getCampaign();

        ICountry enemycountry = CountryFactory.makeMapReferenceCountry(flight.getFlightData().getFlightInformation().getCountry().getSide().getOppositeSide());
        List <IFixedPosition> allFixedPositionsInRadius = new ArrayList<>();
        double missionTargetRadius = flight.getMission().getMissionBorders().getAreaRadius();
        while (!stopLooking(allFixedPositionsInRadius, missionTargetRadius))
        {
            GroupManager groupData =  PWCGContext.getInstance().getCurrentMap().getGroupManager();
            List<Bridge> bridges = groupData.getBridgeFinder().
                    findBridgesForSideWithinRadius(enemycountry.getSide(), campaign.getDate(), flight.getMission().getMissionBorders().getCenter(), missionTargetRadius);
            List<Block> trainStations = groupData.getRailroadStationFinder().
                    getTrainPositionWithinRadiusBySide(enemycountry.getSide(), campaign.getDate(), flight.getMission().getMissionBorders().getCenter(), missionTargetRadius);
            
            allFixedPositionsInRadius.addAll(bridges);
            allFixedPositionsInRadius.addAll(trainStations);
            
            missionTargetRadius += 10000.0;
        }
        return allFixedPositionsInRadius;
    }
    
    private boolean stopLooking(List <IFixedPosition> allFixedPositionsInRadius, double maxRadius)
    {
        if (allFixedPositionsInRadius.size() >= 2)
        {
            return true;
        }

        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        if (allFixedPositionsInRadius.size() >= 1 && maxRadius >= productSpecific.getSmallMissionRadius())
        {
            return true;
        }
        
        return false;
    }

    private McuWaypoint createWP(Coordinate transportPosition) throws PWCGException 
    {
        transportPosition.setYPos(flight.getFlightData().getFlightInformation().getAltitude());
        McuWaypoint wp = WaypointFactory.createPatrolWaypointType();
        wp.setTriggerArea(McuWaypoint.TARGET_AREA);
        wp.setSpeed(flight.getFlightData().getFlightPlanes().getFlightCruisingSpeed());
        wp.setPosition(transportPosition);
        
        return wp;
    }
}
