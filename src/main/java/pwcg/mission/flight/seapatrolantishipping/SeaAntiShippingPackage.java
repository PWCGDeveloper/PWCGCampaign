package pwcg.mission.flight.seapatrolantishipping;

import java.util.List;

import pwcg.campaign.api.Side;
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

public class SeaAntiShippingPackage implements IFlightPackage
{
    private FlightInformation flightInformation;

    public SeaAntiShippingPackage(FlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }
    
	public Flight createPackage () throws PWCGException 
	{
        ShippingLane selectedShippingLane = getEnemyShippingLane();

        Coordinate startCoords = flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate());
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit(startCoords.copy());
		SeaAntiShippingFlight seaPatrol = new SeaAntiShippingFlight (flightInformation, missionBeginUnit);
		generateConvoysForPlayerFlight(selectedShippingLane, seaPatrol);		
		seaPatrol.createUnitMission();
		return seaPatrol;
	}

	private void generateConvoysForPlayerFlight(
	        ShippingLane selectedShippingLane,
	        SeaAntiShippingFlight seaPatrol) throws PWCGException
	{
		if (flightInformation.isPlayerFlight())
		{
		    ShipConvoyGenerator shipConvoyGenerator = new ShipConvoyGenerator();
            List<ShipConvoyUnit> otherConvoys = shipConvoyGenerator.generateConvoys(flightInformation, selectedShippingLane);
            for (ShipConvoyUnit convoy : otherConvoys)
            {
                seaPatrol.addLinkedUnit(convoy);
            }
		}
	}

	private ShippingLane getEnemyShippingLane() throws PWCGException 
	{
	    
        ShippingLaneManager shippingLaneManager = PWCGContextManager.getInstance().getCurrentMap().getShippingLaneManager();
        Coordinate referenceCoordinate = flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate());
        Side enemySide = flightInformation.getSquadron().determineEnemySide();
        ShippingLane selectedShippingLane = shippingLaneManager.getTargetShippingLane(referenceCoordinate, enemySide);;
	    return selectedShippingLane;
	}
}
