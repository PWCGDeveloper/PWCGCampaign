package pwcg.mission.flight.recon;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PositionFinder;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.mcu.McuWaypoint;

public class ReconWaypointsAirfield extends ReconWaypoints
{
	public ReconWaypointsAirfield(Coordinate startCoords, 
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
    protected void createReconWaypoints() throws PWCGException  
    {
        Side enemySide = flight.getCountry().getSide().getOppositeSide();

        // Find airfields to recon
        List <IAirfield> enemyAirfields = new ArrayList<IAirfield>();
        double maxRadius = 40000.0;
        
        while (enemyAirfields.size() <= 2)
        {
            enemyAirfields =  PWCGContextManager.getInstance().getCurrentMap().getAirfieldManager().getAirfieldFinder().getAirfieldsWithinRadiusBySide(targetCoords, campaign.getDate(), maxRadius, enemySide);
                        
            maxRadius += 10000.0;
        }
        
        
        // Not more WPs than we have targets
        int numWaypoints = 2 + RandomNumberGenerator.getRandom(4);
        if (numWaypoints > enemyAirfields.size())
        {
            numWaypoints = enemyAirfields.size();
        }
        
        List <IAirfield> remainingAirfields = enemyAirfields;
        Coordinate lastCoord = targetCoords.copy();
        for (int i = 0; i < numWaypoints; ++i)
        {
            int index = getNextAirfield(remainingAirfields, lastCoord);
            
            IAirfield field = remainingAirfields.get(index);
            
            flight.addAirfieldTargets(field);

            McuWaypoint wp = super.createWP(field.getPosition().copy());
            waypoints.add(wp);

            lastCoord = field.getPosition().copy();
            remainingAirfields.remove(index);
        }
    }
    
    /**
     * @param remainingAirfields
     * @param lastCoord
     * @return
     * @
     */
    private int getNextAirfield(List <IAirfield> remainingAirfields, Coordinate lastCoord) 
    {
        int index = 0;
        double leastDistance = PositionFinder.ABSURDLY_LARGE_DISTANCE;
        
        for (int i = 0; i < remainingAirfields.size(); ++i)
        {
            IAirfield field = remainingAirfields.get(i);
            double thisDistance = MathUtils.calcDist(lastCoord, field.getPosition());
            if (thisDistance < leastDistance)
            {
                leastDistance = thisDistance;
                index = i;
            }
        }
        
        return index;
    }
}
