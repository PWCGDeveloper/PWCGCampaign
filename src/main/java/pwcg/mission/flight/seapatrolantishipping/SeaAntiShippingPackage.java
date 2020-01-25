package pwcg.mission.flight.seapatrolantishipping;

import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.shipping.ShippingLane;
import pwcg.campaign.shipping.ShippingLaneManager;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.flight.attack.GroundAttackFlight;
import pwcg.mission.flight.bomb.BombingFlight;
import pwcg.mission.flight.divebomb.DiveBombingFlight;
import pwcg.mission.ground.ShipConvoyGenerator;
import pwcg.mission.ground.org.IGroundUnitCollection;

public class SeaAntiShippingPackage implements IFlightPackage
{
    private IFlightInformation flightInformation;

    public SeaAntiShippingPackage(IFlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }
    
	public IFlight createPackage () throws PWCGException 
	{
        ShippingLane selectedShippingLane = getEnemyShippingLane();
        IFlight seaPatrol = createSeaPatrol();
		generateConvoysForPlayerFlight(selectedShippingLane, seaPatrol);
		return seaPatrol;
	}
	
	private IFlight createSeaPatrol() throws PWCGException
	{
	    if (flightInformation.getFlightType() == FlightTypes.ANTI_SHIPPING_ATTACK)
	    {
	        return createAntiShippingAttackFlight();
	    }
        else if (flightInformation.getFlightType() == FlightTypes.ANTI_SHIPPING_BOMB)
        {
            return createAntiShippingBombFlight();
        }
        else if (flightInformation.getFlightType() == FlightTypes.ANTI_SHIPPING_DIVE_BOMB)
        {
            return createAntiShippingDiveBombFlight();
        }
        else
        {
            throw new PWCGException("Unexpected flight type for anti shipping mission: " + flightInformation.getFlightType());
        }
	}

    private IFlight createAntiShippingDiveBombFlight() throws PWCGException
    {
        DiveBombingFlight diveBombingFlight = new DiveBombingFlight (flightInformation);
        diveBombingFlight.createFlight();
        return diveBombingFlight;
    }

    private IFlight createAntiShippingBombFlight() throws PWCGException
    {
        BombingFlight bombingFlight = new BombingFlight (flightInformation);
        bombingFlight.createFlight();
        return bombingFlight;
    }

    private IFlight createAntiShippingAttackFlight() throws PWCGException
    {
        GroundAttackFlight antiShippingAttackFlight = new GroundAttackFlight (flightInformation);
        antiShippingAttackFlight.createFlight();
        return antiShippingAttackFlight;
    }


	private void generateConvoysForPlayerFlight(ShippingLane selectedShippingLane, IFlight seaPatrol) throws PWCGException
	{
		if (flightInformation.isPlayerFlight())
		{
		    ShipConvoyGenerator shipConvoyGenerator = new ShipConvoyGenerator();
            List<IGroundUnitCollection> otherConvoys = shipConvoyGenerator.generateConvoys(flightInformation, selectedShippingLane);
            for (IGroundUnitCollection convoy : otherConvoys)
            {
                seaPatrol.getLinkedGroundUnits().addLinkedGroundUnit(convoy);
            }
		}
	}

	private ShippingLane getEnemyShippingLane() throws PWCGException 
	{
        ShippingLaneManager shippingLaneManager = PWCGContext.getInstance().getCurrentMap().getShippingLaneManager();        
        ShippingLane selectedShippingLane = shippingLaneManager.getClosestShippingLane(flightInformation.getMission().getMissionBorders().getCenter());
	    return selectedShippingLane;
	}
}
