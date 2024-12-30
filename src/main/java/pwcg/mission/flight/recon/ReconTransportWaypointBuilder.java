package pwcg.mission.flight.recon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.Bridge;
import pwcg.campaign.group.GroupManager;
import pwcg.campaign.group.ScriptedFixedPosition;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PositionFinder;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.mcu.McuWaypoint;

public class ReconTransportWaypointBuilder
{
    private IFlight flight;
    private Campaign campaign;

    public ReconTransportWaypointBuilder(IFlight flight) throws PWCGException
    {
        this.flight = flight;
        this.campaign = flight.getCampaign();
    }

    public List<McuWaypoint> createTargetWaypoints(Coordinate ingressPosition) throws PWCGException
    {
        List<McuWaypoint> targetWaypoints = new ArrayList<McuWaypoint>();
        
        Side enemySide = flight.getFlightInformation().getCountry().getSide().getOppositeSide();
        ICountry enemycountry = CountryFactory.makeMapReferenceCountry(flight.getCampaignMap(), enemySide);

        List <ScriptedFixedPosition> allFixedPositionsInRadius = new ArrayList<ScriptedFixedPosition>();
        double maxRadius = 40000.0;
        
        while (allFixedPositionsInRadius.size() <= 2)
        {
    	    GroupManager groupData =  PWCGContext.getInstance().getMap(campaign.getCampaignMap()).getGroupManager();
    	    List<Bridge> bridges = groupData.getBridgeFinder().findBridgesForSideWithinRadius(
    	            campaign.getCampaignMap(), enemycountry.getSide(), flight.getCampaign().getDate(), flight.getTargetDefinition().getPosition(), maxRadius);
    	    
    	    List<Block> trainStations = groupData.getRailroadStationFinder().getTrainPositionWithinRadiusBySide(
    	            campaign.getCampaignMap(), enemycountry.getSide(), flight.getCampaign().getDate(), flight.getTargetDefinition().getPosition(), maxRadius);
    	    
            allFixedPositionsInRadius.addAll(bridges);
            allFixedPositionsInRadius.addAll(trainStations);
            
            maxRadius += 10000.0;
        }
        
        
        // Not more WPs than we have targets
        int numWaypoints = 2 + RandomNumberGenerator.getRandom(4);
        if (numWaypoints > allFixedPositionsInRadius.size())
        {
            numWaypoints = allFixedPositionsInRadius.size();
        }
        
        Map<String, ScriptedFixedPosition> locationsIncluded = new HashMap<String, ScriptedFixedPosition>();

        List <ScriptedFixedPosition> remainingFixedPositions = allFixedPositionsInRadius;
        Coordinate lastCoord = flight.getTargetDefinition().getPosition().copy();
        for (int i = 0; i < numWaypoints; ++i)
        {
            int index = getNextFixedPosition(remainingFixedPositions, lastCoord, locationsIncluded);
            
            ScriptedFixedPosition fixedPosition = remainingFixedPositions.get(index);
            McuWaypoint wp = createWP(fixedPosition.getPosition().copy());
            targetWaypoints.add(wp);

            lastCoord = fixedPosition.getPosition().copy();
            remainingFixedPositions.remove(index);
        }
        return targetWaypoints;
	}

	private String formLocationKey(Coordinate coord)
	{
        int x = Double.valueOf(coord.getXPos()).intValue();
        int z = Double.valueOf(coord.getZPos()).intValue();
        String key = "" + x + " " + z;
        
        return key;
	}

	private int getNextFixedPosition(List <ScriptedFixedPosition> remainingFixedPositions, Coordinate lastCoord, Map<String, ScriptedFixedPosition> locationsIncluded) 
	{
	    int index = 0;
	    double leastDistance = PositionFinder.ABSURDLY_LARGE_DISTANCE;
	    
	    for (int i = 0; i < remainingFixedPositions.size(); ++i)
	    {
	        ScriptedFixedPosition fixedPosition = remainingFixedPositions.get(i);
	        String locationKey = formLocationKey(fixedPosition.getPosition());
	        if (!locationsIncluded.containsKey(locationKey))
	        {
    	        double thisDistance = MathUtils.calcDist(lastCoord, fixedPosition.getPosition());
    	        if (thisDistance < leastDistance)
    	        {
    	            leastDistance = thisDistance;
    	            index = i;
    	            locationsIncluded.put(locationKey, fixedPosition);
    	        }
	        }
	    }
	    
	    return index;
	}

    private McuWaypoint createWP(Coordinate coord) throws PWCGException 
    {
        coord.setYPos(flight.getFlightInformation().getAltitude());

        McuWaypoint wp = WaypointFactory.createReconWaypointType();
        wp.setTriggerArea(McuWaypoint.TARGET_AREA);
        wp.setSpeed(flight.getFlightCruisingSpeed());
        wp.setPosition(coord);

        return wp;
    }
}
