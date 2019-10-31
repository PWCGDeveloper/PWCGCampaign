package pwcg.mission.flight.offensive;

import java.util.ArrayList;
import java.util.List;

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
import pwcg.mission.flight.Flight;
import pwcg.mission.mcu.McuWaypoint;

public class OffensiveWaypointsTransport extends OffensiveWaypoints
{
    private List<McuWaypoint> targetWaypoints = new ArrayList<McuWaypoint>();
    
    public OffensiveWaypointsTransport(Flight flight) throws PWCGException 
    {
        super(flight);
    }
    
    protected List<McuWaypoint> createTargetWaypoints(Coordinate ingressPosition) throws PWCGException
    {
        List <IFixedPosition> allFixedPositionsInRadius = getTransportPositionsWithinRadius();
        
        SortedFixedPositions sortedFixedPositions = new SortedFixedPositions();
        sortedFixedPositions.sortPositionsByDirecton(allFixedPositionsInRadius);
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        List <IFixedPosition> sortedPositions = sortedFixedPositions.getSortedPositionsWithMaxDistanceTTravelled(
                campaign, flight.getTargetCoords(), productSpecific.getSmallMissionRadius());

        for (IFixedPosition transportBlock : sortedPositions)
        {
            McuWaypoint offensiveWaypoint = createWP(transportBlock);
            targetWaypoints.add(offensiveWaypoint);
        }
        return targetWaypoints;
	}

    private List <IFixedPosition> getTransportPositionsWithinRadius() throws PWCGException 
    {
        ICountry enemycountry = CountryFactory.makeMapReferenceCountry(flight.getCountry().getSide().getOppositeSide());
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
    
    private McuWaypoint createWP(IFixedPosition transportBlock) throws PWCGException 
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
        return wp;
    }
}
