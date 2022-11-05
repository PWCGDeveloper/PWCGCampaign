package pwcg.mission.flight.offensive;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IFixedPosition;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.end.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointRouteSet;
import pwcg.mission.mcu.McuWaypoint;

public class OffensiveWaypointsAirfieldFactory
{   
    private IFlight flight;
    private Mission mission;
    private Campaign campaign;
    private MissionPointRouteSet missionPointSet = new MissionPointRouteSet();
    
    public OffensiveWaypointsAirfieldFactory(IFlight flight) throws PWCGException
    {
        this.flight = flight;
        this.mission = flight.getMission();
        this.campaign = flight.getCampaign();
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
        List<McuWaypoint> targetWaypoints = new ArrayList<McuWaypoint>();
	    List <IFixedPosition> allFixedPositionsInRadius = findEnemyAirieldsForOffensivePatrol();
	        
	    SortedFixedPositions sortedFixedPositions = new SortedFixedPositions();
	    sortedFixedPositions.sortPositionsByDirecton(allFixedPositionsInRadius);
	    IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
	    List <IFixedPosition> sortedPositions = sortedFixedPositions.getSortedPositionsWithMaxDistanceTTravelled(
	            campaign, 
	            flight.getTargetDefinition().getPosition(), productSpecific.getSmallMissionRadius());
	    
	    for (IFixedPosition transportBlock : sortedPositions)
	    {
	        McuWaypoint offensiveWaypoint = createWP(transportBlock.getPosition());
	        targetWaypoints.add(offensiveWaypoint);
	    }
	    
	    return targetWaypoints;
	}

    private List<IFixedPosition> findEnemyAirieldsForOffensivePatrol() throws PWCGException
    {
        double missionTargetRadius = mission.getMissionBorders().getAreaRadius();
        Side enemySide = flight.getFlightInformation().getCountry().getSide().getOppositeSide();
        List <Airfield> enemyAirfields = new ArrayList<>();
        while (!stopLooking(enemyAirfields, missionTargetRadius))
        {
            enemyAirfields =  PWCGContext.getInstance().getMap(campaign.getCampaignMap()).getAirfieldManager().getAirfieldFinder().
                    getAirfieldsWithinRadiusBySide(campaign.getCampaignMap(), mission.getMissionBorders().getCenter(), campaign.getDate(), missionTargetRadius, enemySide);
            missionTargetRadius += 10000.0;
        }

        List <IFixedPosition> enemyFixedPositions = new ArrayList<>();
        for (Airfield airfield : enemyAirfields)
        {
            enemyFixedPositions.add(airfield);
        }
        return enemyFixedPositions;
    }
    
    
    private boolean stopLooking(List <Airfield> enemyAirfields, double maxRadius)
    {
        if (enemyAirfields.size() >= 2)
        {
            return true;
        }

        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        if (enemyAirfields.size() >= 1 && maxRadius >= productSpecific.getSmallMissionRadius())
        {
            return true;
        }
        
        return false;
    }

    private McuWaypoint createWP(Coordinate fieldPosition) throws PWCGException 
    {
        fieldPosition.setYPos(flight.getFlightInformation().getAltitude());
        McuWaypoint wp = WaypointFactory.createPatrolWaypointType();
        wp.setTriggerArea(McuWaypoint.TARGET_AREA);
        wp.setSpeed(flight.getFlightCruisingSpeed());
        wp.setPosition(fieldPosition);
        
        return wp;
    }
}
