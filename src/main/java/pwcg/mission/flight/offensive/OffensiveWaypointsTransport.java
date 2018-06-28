package pwcg.mission.flight.offensive;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.Bridge;
import pwcg.campaign.group.FixedPosition;
import pwcg.campaign.group.GroupManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.mcu.McuWaypoint;

public class OffensiveWaypointsTransport extends OffensiveWaypoints
{
    private List <FixedPosition> sortedFixedPositionsNS = new ArrayList<FixedPosition>();
    private List <FixedPosition> sortedFixedPositionsEW = new ArrayList<FixedPosition>();
    

	public OffensiveWaypointsTransport(Coordinate startCoords, 
					  	  Coordinate targetCoords, 
					  	  Flight flight,
					  	  Mission mission) throws PWCGException 
	{
		super(startCoords, targetCoords, flight, mission);
	}
	

	/**
	 * @throws PWCGException 
	 * @
	 */
	protected void createOffensivePatrolWaypoints() throws PWCGException  
	{
        ICountry enemycountry = CountryFactory.makeMapReferenceCountry(flight.getCountry().getSide().getOppositeSide());

	    // Find transportation targets
        List <FixedPosition> allFixedPositionsInRadius = new ArrayList<FixedPosition>();
        double maxRadius = 30000.0;
        
        while (allFixedPositionsInRadius.size() <= 8)
        {
            GroupManager groupData =  PWCGContextManager.getInstance().getCurrentMap().getGroupManager();
    	    List<Bridge> bridges = groupData.getBridgeFinder().findBridgesForSideWithinRadius(enemycountry.getSide(), campaign.getDate(), targetCoords, maxRadius);
    	    List<Block> trainStations = groupData.getRailroadStationFinder().getTrainPositionWithinRadiusBySide(enemycountry.getSide(), campaign.getDate(), targetCoords, maxRadius);
    	    
            allFixedPositionsInRadius.addAll(bridges);
            allFixedPositionsInRadius.addAll(trainStations);
            
            maxRadius += 10000.0;
        }
                
        // Pick the most reasonable direction
        sortPositionsByDirecton(allFixedPositionsInRadius);
        
        // Count how many in each direction and use the best direction
        int numEW = 0;
        int numNS = 0;
        
        for (FixedPosition fixedPosition : allFixedPositionsInRadius)
        {
            double referenceAngle = MathUtils.calcAngle(startCoords, fixedPosition.getPosition());
            boolean isEW = isEW(referenceAngle);
            if (isEW)
            {
                ++numEW;
            }
            else
            {
                ++numNS;
            }
        }
        
        List <FixedPosition> sortedPositions = sortedFixedPositionsEW;
        boolean isEW = false;
        if (numEW > numNS)
        {
            sortedPositions = sortedFixedPositionsNS;
            isEW = true;
        }


        int waypointsAdded = 0;
        Coordinate lastCoord = null;
        double totalDistance = 0.0;
        for (int i = 0; i < sortedPositions.size(); ++i)
        {
            FixedPosition transportBlock = sortedPositions.get(i);
            
            // Skip WP until we find the first one going in the right direction
            if (waypointsAdded == 0)
            {
                double blockReferenceAngle = MathUtils.calcAngle(startCoords, transportBlock.getPosition());
                boolean isBlockEW = isEW(blockReferenceAngle);
                
                if (isEW && !isBlockEW)
                {
                    continue;
                }
                
                if (!isEW && isBlockEW)
                {
                    continue;
                }
                
            }

            boolean addWP = false;
            
            double distance = 0.0;
            if (lastCoord != null)
            {
                distance = MathUtils.calcDist(transportBlock.getPosition(), lastCoord);
                if (distance > 3000)
                {
                    addWP = true;
                }
                
                // Don't go too far
                double totalDistanceWouldBe = totalDistance += distance;
                if (totalDistanceWouldBe > 35000.0)
                {
                    addWP = false;
                }
            }
            else
            {
                addWP = true;
            }

            if (addWP)
            {
                createWP(transportBlock);
                ++waypointsAdded;
                lastCoord = transportBlock.getPosition().copy();

                totalDistance += distance;
            }
            else
            {
                //Logger.log(LogLevel.DEBUG, "Reject " + transportBlock.getPosition().getXPos() + "   " +  transportBlock.getPosition().getZPos());
            }
            
            // Record the last coordinate and remove the field
            if (totalDistance > 30000.0)
            {
                break;
            }
        }
	}
    
	/**
	 * @param referenceAngle
	 * @return
	 */
	private boolean isEW(double referenceAngle)
	{
        if ((referenceAngle > 45 && referenceAngle < 135) || 
            (referenceAngle > 225 && referenceAngle < 315))
        {
            return true;
        }
        
        
        return false;
	}

    /**
     * @param referenceAngle
     * @param fixedPositions
     * @return
     */
    private void sortPositionsByDirecton(List <FixedPosition> fixedPositions)
    {        
        TreeMap<Double, FixedPosition> sortedMapNS = new TreeMap<Double, FixedPosition>();
        TreeMap<Double, FixedPosition> sortedMapEW = new TreeMap<Double, FixedPosition>();
        
        for (FixedPosition fixedPosition : fixedPositions)
        {
            sortedMapNS.put(fixedPosition.getPosition().getXPos(), fixedPosition);
            sortedMapEW.put(fixedPosition.getPosition().getZPos(), fixedPosition);
        }
        
        sortedFixedPositionsNS.addAll(sortedMapNS.values());
        sortedFixedPositionsEW.addAll(sortedMapEW.values());
    }
    
    /**
     * @param transportBlock
     * @throws PWCGException 
     */
    protected void createWP(FixedPosition transportBlock) throws PWCGException 
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
