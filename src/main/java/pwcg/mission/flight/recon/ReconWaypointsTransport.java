package pwcg.mission.flight.recon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.Bridge;
import pwcg.campaign.group.FixedPosition;
import pwcg.campaign.group.GroupManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PositionFinder;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.Flight;
import pwcg.mission.mcu.McuWaypoint;

public class ReconWaypointsTransport extends ReconWaypoints
{
    private List<McuWaypoint> targetWaypoints = new ArrayList<McuWaypoint>();

    public ReconWaypointsTransport(Flight flight) throws PWCGException 
    {
        super(flight);
    }

    protected List<McuWaypoint> createTargetWaypoints(Coordinate ingressPosition) throws PWCGException
    {
        Side enemySide = flight.getCountry().getSide().getOppositeSide();
        ICountry enemycountry = CountryFactory.makeMapReferenceCountry(enemySide);

        List <FixedPosition> allFixedPositionsInRadius = new ArrayList<FixedPosition>();
        double maxRadius = 40000.0;
        
        while (allFixedPositionsInRadius.size() <= 2)
        {
    	    GroupManager groupData =  PWCGContext.getInstance().getCurrentMap().getGroupManager();
    	    List<Bridge> bridges = groupData.getBridgeFinder().findBridgesForSideWithinRadius(
    	            enemycountry.getSide(), campaign.getDate(), flight.getTargetPosition(), maxRadius);
    	    
    	    List<Block> trainStations = groupData.getRailroadStationFinder().getTrainPositionWithinRadiusBySide(
    	            enemycountry.getSide(), campaign.getDate(), flight.getTargetPosition(), maxRadius);
    	    
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
        
        Map<String, FixedPosition> locationsIncluded = new HashMap<String, FixedPosition>();

        List <FixedPosition> remainingFixedPositions = allFixedPositionsInRadius;
        Coordinate lastCoord = flight.getTargetPosition().copy();
        for (int i = 0; i < numWaypoints; ++i)
        {
            int index = getNextFixedPosition(remainingFixedPositions, lastCoord, locationsIncluded);
            
            FixedPosition fixedPosition = remainingFixedPositions.get(index);
            
            // Keep track of what we are adding
            if (fixedPosition instanceof Bridge)
            {
                flight.addBridgeTarget((Bridge)fixedPosition);
            }
            else if (fixedPosition instanceof Block)
            {
                flight.addTrainTargets((Block)fixedPosition);
            }
            
            McuWaypoint wp = super.createWP(fixedPosition.getPosition().copy());
            targetWaypoints.add(wp);

            lastCoord = fixedPosition.getPosition().copy();
            remainingFixedPositions.remove(index);
        }
        return targetWaypoints;
	}

	private String formLocationKey(Coordinate coord)
	{
        int x = new Double(coord.getXPos()).intValue();
        int z = new Double(coord.getZPos()).intValue();
        String key = "" + x + " " + z;
        
        return key;
	}

	private int getNextFixedPosition(List <FixedPosition> remainingFixedPositions, Coordinate lastCoord, Map<String, FixedPosition> locationsIncluded) 
	{
	    int index = 0;
	    double leastDistance = PositionFinder.ABSURDLY_LARGE_DISTANCE;
	    
	    for (int i = 0; i < remainingFixedPositions.size(); ++i)
	    {
	        FixedPosition fixedPosition = remainingFixedPositions.get(i);
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
}
