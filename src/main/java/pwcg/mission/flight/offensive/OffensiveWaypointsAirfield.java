package pwcg.mission.flight.offensive;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.mcu.McuWaypoint;

public class OffensiveWaypointsAirfield extends OffensiveWaypoints
{
    private List <IAirfield> sortedAirfieldsNS = new ArrayList<IAirfield>();
    private List <IAirfield> sortedAirfieldsEW = new ArrayList<IAirfield>();
    
	public OffensiveWaypointsAirfield(Coordinate startCoords, 
					  	  Coordinate targetCoords, 
					  	  Flight flight,
					  	  Mission mission) throws PWCGException 
	{
		super(startCoords, targetCoords, flight, mission);
	}

    protected void createOffensivePatrolWaypoints() throws PWCGException  
    {
        List<IAirfield> enemyAirfields = findEnemyAirieldsForOffensivePatrol();
                

        sortPositionsByDirecton(enemyAirfields);
        double referenceAngle = MathUtils.calcAngle(startCoords, enemyAirfields.get(0).getPosition());
        boolean isEW = isEW(referenceAngle);
        
        List <IAirfield> sortedPositions = sortedAirfieldsEW;
        if (isEW)
        {
            sortedPositions = sortedAirfieldsNS;
        }

        int numWaypoints = 3 + RandomNumberGenerator.getRandom(2);
        if (numWaypoints > enemyAirfields.size())
        {
            numWaypoints = enemyAirfields.size();
        }

        int waypointsAdded = 0;
        Coordinate lastCoord = null;
        double totalDistance = 0.0;
        for (int i = 0; i < sortedPositions.size(); ++i)
        {
            IAirfield transportBlock = sortedPositions.get(i);
            
            // Skip WP until we find the first one going in the right direction
            if (waypointsAdded == 0)
            {
                double blockReferenceAngle = MathUtils.calcAngle(startCoords, sortedPositions.get(i).getPosition());
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
                if (distance > 4000)
                {
                    addWP = true;
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
            
            // Continue until mission over
            if (waypointsAdded >= numWaypoints || sortedPositions.size() == 0 || totalDistance > 25000.0)
            {
                break;
            }
        }
    }

    private List<IAirfield> findEnemyAirieldsForOffensivePatrol() throws PWCGException
    {
        List <IAirfield> enemyAirfields = new ArrayList<IAirfield>();        
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        double maxRadius = productSpecific.getInitialTargetRadiusFromGeneralTargetLocation(FlightTypes.OFFENSIVE);

        Side enemySide = flight.getCountry().getSide().getOppositeSide();

        while (enemyAirfields.size() <= 5)
        {
            enemyAirfields =  PWCGContextManager.getInstance().getCurrentMap().getAirfieldManager().getAirfieldFinder().getAirfieldsWithinRadiusBySide(targetCoords, campaign.getDate(), maxRadius, enemySide);
            maxRadius += 10000.0;
        }
        return enemyAirfields;
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
     * @param airfields
     * @return
     */
    private void sortPositionsByDirecton(List <IAirfield> airfields)
    {        
        TreeMap<Double, IAirfield> sortedMapNS = new TreeMap<Double, IAirfield>();
        TreeMap<Double, IAirfield> sortedMapEW = new TreeMap<Double, IAirfield>();
        
        for (IAirfield field : airfields)
        {
            sortedMapNS.put(field.getPosition().getXPos(), field);
            sortedMapEW.put(field.getPosition().getZPos(), field);
        }
        
        sortedAirfieldsNS.addAll(sortedMapNS.values());
        sortedAirfieldsEW.addAll(sortedMapEW.values());
    }
    
    /**
     * @param transportBlock
     * @throws PWCGException 
     */
    protected void createWP(IAirfield field) throws PWCGException 
    {
        flight.addAirfieldTargets(field);               
        McuWaypoint wp = super.createWP(field.getPosition().copy());
        waypoints.add(wp);
    }

}
