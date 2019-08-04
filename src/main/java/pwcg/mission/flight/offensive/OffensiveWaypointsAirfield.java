package pwcg.mission.flight.offensive;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IFixedPosition;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.mcu.McuWaypoint;

public class OffensiveWaypointsAirfield extends OffensiveWaypoints
{
	public OffensiveWaypointsAirfield(Coordinate startCoords, 
					  	  Coordinate targetCoords, 
					  	  Flight flight,
					  	  Mission mission) throws PWCGException 
	{
		super(startCoords, targetCoords, flight, mission);
	}
	
    protected void createTargetWaypoints(Coordinate startWaypoint) throws PWCGException
	{
	    List <IFixedPosition> allFixedPositionsInRadius = findEnemyAirieldsForOffensivePatrol();
	        
	    SortedFixedPositions sortedFixedPositions = new SortedFixedPositions();
	    sortedFixedPositions.sortPositionsByDirecton(allFixedPositionsInRadius);
	    IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
	    List <IFixedPosition> sortedPositions = sortedFixedPositions.getSortedPositionsWithMaxDistanceTTravelled(campaign, targetCoords, productSpecific.getSmallMissionRadius());
	    
	    for (IFixedPosition transportBlock : sortedPositions)
	    {
	        createWP(transportBlock);
	    }
	}

    private List<IFixedPosition> findEnemyAirieldsForOffensivePatrol() throws PWCGException
    {
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        double maxRadius = productSpecific.getInitialTargetRadiusFromGeneralTargetLocation(FlightTypes.OFFENSIVE);

        Side enemySide = flight.getCountry().getSide().getOppositeSide();
        List <IAirfield> enemyAirfields = new ArrayList<>();
        while (!stopLooking(enemyAirfields, maxRadius))
        {
            enemyAirfields =  PWCGContextManager.getInstance().getCurrentMap().getAirfieldManager().getAirfieldFinder().
                    getAirfieldsWithinRadiusBySide(mission.getMissionBorders().getCenter(), campaign.getDate(), maxRadius, enemySide);
            maxRadius += 10000.0;
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


    protected void createWP(IFixedPosition field) throws PWCGException 
    {
        flight.addAirfieldTargets((IAirfield)field);               
        McuWaypoint wp = super.createWP(field.getPosition().copy());
        waypoints.add(wp);
    }

}
