package pwcg.mission.flight.seapatrolscout;

import java.util.List;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.target.locator.ShippingLane;
import pwcg.campaign.target.locator.ShippingLaneManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.ground.ShipConvoyGenerator;
import pwcg.mission.ground.unittypes.transport.ShipConvoyUnit;

public class SeaPatrolPackage implements IFlightPackage
{
    private FlightInformation flightInformation;

    public SeaPatrolPackage(FlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public Flight createPackage () throws PWCGException 
	{
        Coordinate startCoords = flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate());
        ShippingLane selectedShippingLane = getShippingLane(startCoords);

        MissionBeginUnit missionBeginUnit = new MissionBeginUnit(startCoords.copy());                    
		SeaPatrolFlight seaPatrol = new SeaPatrolFlight (flightInformation, missionBeginUnit);
		
		// Add a sea unit for the player flight
        if (flightInformation.isPlayerFlight())
        {
            // This is really an anti-air patrol but add random shipping
            ShipConvoyGenerator shipBattleGenerator = new ShipConvoyGenerator();
            List<ShipConvoyUnit> otherConvoys = shipBattleGenerator.generateConvoys(flightInformation, selectedShippingLane);
            for (ShipConvoyUnit convoy : otherConvoys)
            {
                seaPatrol.addLinkedUnit(convoy);
            }
        }
		
		seaPatrol.createUnitMission();
		return seaPatrol;
	}

    protected ShippingLane getShippingLane(Coordinate referenceCoordinate) throws PWCGException 
    {
        ShippingLane selectedShippingLane = null;
        
        ShippingLaneManager shippingLaneManager = PWCGContextManager.getInstance().getCurrentMap().getShippingLaneManager();
        selectedShippingLane = shippingLaneManager.getTargetShippingLane(referenceCoordinate, flightInformation.getSquadron().getCountry().getSide());
        
        return selectedShippingLane;
    }
}
