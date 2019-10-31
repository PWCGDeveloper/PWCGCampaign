package pwcg.mission.flight.offensive;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IFixedPosition;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.Flight;
import pwcg.mission.mcu.McuWaypoint;

public class OffensiveWaypointsAirfield extends OffensiveWaypoints
{
    private List<McuWaypoint> targetWaypoints = new ArrayList<McuWaypoint>();
    
	public OffensiveWaypointsAirfield(Flight flight) throws PWCGException 
	{
		super(flight);
	}
	
    protected List<McuWaypoint> createTargetWaypoints(Coordinate ingressPosition) throws PWCGException
	{
	    List <IFixedPosition> allFixedPositionsInRadius = findEnemyAirieldsForOffensivePatrol();
	        
	    SortedFixedPositions sortedFixedPositions = new SortedFixedPositions();
	    sortedFixedPositions.sortPositionsByDirecton(allFixedPositionsInRadius);
	    IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
	    List <IFixedPosition> sortedPositions = sortedFixedPositions.getSortedPositionsWithMaxDistanceTTravelled(campaign, flight.getTargetCoords(), productSpecific.getSmallMissionRadius());
	    
	    for (IFixedPosition transportBlock : sortedPositions)
	    {
	        McuWaypoint offensiveWaypoint = createWP(transportBlock);
	        targetWaypoints.add(offensiveWaypoint);
	    }
	    
	    return targetWaypoints;
	}

    private List<IFixedPosition> findEnemyAirieldsForOffensivePatrol() throws PWCGException
    {
        double missionTargetRadius = flight.getMission().getMissionBorders().getAreaRadius();
        Side enemySide = flight.getCountry().getSide().getOppositeSide();
        List <IAirfield> enemyAirfields = new ArrayList<>();
        while (!stopLooking(enemyAirfields, missionTargetRadius))
        {
            enemyAirfields =  PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAirfieldFinder().
                    getAirfieldsWithinRadiusBySide(flight.getMission().getMissionBorders().getCenter(), campaign.getDate(), missionTargetRadius, enemySide);
            missionTargetRadius += 10000.0;
        }

        List <IFixedPosition> enemyFixedPositions = new ArrayList<>();
        for (IAirfield airfield : enemyAirfields)
        {
            enemyFixedPositions.add(airfield);
        }
        return enemyFixedPositions;
    }
    
    
    private boolean stopLooking(List <IAirfield> enemyAirfields, double maxRadius)
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


    protected McuWaypoint createWP(IFixedPosition field) throws PWCGException 
    {
        flight.addAirfieldTargets((IAirfield)field);               
        McuWaypoint offensiveWaypoint = super.createWP(field.getPosition().copy());
        return offensiveWaypoint;
    }

}
