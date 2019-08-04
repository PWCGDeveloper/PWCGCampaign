package pwcg.mission.flight.offensive;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IFixedPosition;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.Bridge;
import pwcg.campaign.group.GroupManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.mcu.McuWaypoint;

public class OffensiveWaypointsTransport extends OffensiveWaypoints
{
	public OffensiveWaypointsTransport(Coordinate startCoords, 
					  	  Coordinate targetCoords, 
					  	  Flight flight,
					  	  Mission mission) throws PWCGException 
	{
		super(startCoords, targetCoords, flight, mission);
	}

    protected void createTargetWaypoints(Coordinate startWaypoint) throws PWCGException
	{
        List <IFixedPosition> allFixedPositionsInRadius = getTransportPositionsWithinRadius();
        
        SortedFixedPositions sortedFixedPositions = new SortedFixedPositions();
        sortedFixedPositions.sortPositionsByDirecton(allFixedPositionsInRadius);
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        List <IFixedPosition> sortedPositions = sortedFixedPositions.getSortedPositionsWithMaxDistanceTTravelled(campaign, targetCoords, productSpecific.getSmallMissionRadius());

        for (IFixedPosition transportBlock : sortedPositions)
        {
            createWP(transportBlock);
        }
	}

    protected List <IFixedPosition> getTransportPositionsWithinRadius() throws PWCGException 
    {
        ICountry enemycountry = CountryFactory.makeMapReferenceCountry(flight.getCountry().getSide().getOppositeSide());
        List <IFixedPosition> allFixedPositionsInRadius = new ArrayList<>();
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        double maxRadius = productSpecific.getVerySmallMissionRadius();
        while (!stopLooking(allFixedPositionsInRadius, maxRadius))
        {
            GroupManager groupData =  PWCGContextManager.getInstance().getCurrentMap().getGroupManager();
            List<Bridge> bridges = groupData.getBridgeFinder().
                    findBridgesForSideWithinRadius(enemycountry.getSide(), campaign.getDate(), mission.getMissionBorders().getCenter(), maxRadius);
            List<Block> trainStations = groupData.getRailroadStationFinder().
                    getTrainPositionWithinRadiusBySide(enemycountry.getSide(), campaign.getDate(), mission.getMissionBorders().getCenter(), maxRadius);
            
            allFixedPositionsInRadius.addAll(bridges);
            allFixedPositionsInRadius.addAll(trainStations);
            
            maxRadius += 10000.0;
        }
        return allFixedPositionsInRadius;
    }
    
    protected boolean stopLooking(List <IFixedPosition> allFixedPositionsInRadius, double maxRadius)
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
    
    protected void createWP(IFixedPosition transportBlock) throws PWCGException 
    {
        if(transportBlock instanceof Bridge)
        {
            flight.addBridgeTarget((Bridge)transportBlock);               
        }
        else if(transportBlock instanceof Block)
        {
            flight.addTrainTargets((Block)transportBlock);
        }
        
        McuWaypoint wp = super.createWP(transportBlock.getPosition().copy());
        waypoints.add(wp);
    }
}
