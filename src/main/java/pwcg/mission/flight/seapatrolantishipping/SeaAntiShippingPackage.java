package pwcg.mission.flight.seapatrolantishipping;

import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.shipping.ShippingLane;
import pwcg.campaign.shipping.ShippingLaneManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.flight.attack.GroundAttackFlight;
import pwcg.mission.flight.bomb.BombingFlight;
import pwcg.mission.flight.divebomb.DiveBombingFlight;
import pwcg.mission.ground.ShipConvoyGenerator;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;

public class SeaAntiShippingPackage implements IFlightPackage
{
    private IFlightInformation flightInformation;
    private TargetDefinition targetDefinition;
    private FlightTypes flightType;

    public SeaAntiShippingPackage(FlightTypes flightType)
    {
        this.flightType = flightType;
    }

    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        this.flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, flightType);
        this.targetDefinition = buildTargetDefintion();

        IFlight seaPatrol = createSeaPatrol();
		generateConvoysForPlayerFlight(seaPatrol);
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
        DiveBombingFlight diveBombingFlight = new DiveBombingFlight (flightInformation, targetDefinition);
        diveBombingFlight.createFlight();
        return diveBombingFlight;
    }

    private IFlight createAntiShippingBombFlight() throws PWCGException
    {
        BombingFlight bombingFlight = new BombingFlight (flightInformation, targetDefinition);
        bombingFlight.createFlight();
        return bombingFlight;
    }

    private IFlight createAntiShippingAttackFlight() throws PWCGException
    {
        GroundAttackFlight antiShippingAttackFlight = new GroundAttackFlight (flightInformation, targetDefinition);
        antiShippingAttackFlight.createFlight();
        return antiShippingAttackFlight;
    }


	private void generateConvoysForPlayerFlight(IFlight seaPatrol) throws PWCGException
	{
		if (flightInformation.isPlayerFlight())
		{
		    ShipConvoyGenerator shipConvoyGenerator = new ShipConvoyGenerator();
            List<IGroundUnitCollection> otherConvoys = shipConvoyGenerator.generateConvoys(flightInformation, targetDefinition);
            for (IGroundUnitCollection convoy : otherConvoys)
            {
                seaPatrol.addLinkedGroundUnit(convoy);
            }
		}
	}

    private TargetDefinition buildTargetDefintion() throws PWCGException
    {
        ShippingLane selectedShippingLane = getEnemyShippingLane();
        Coordinate shippingPosition = selectedShippingLane.getShippingLaneBox().chooseCoordinateWithinBox();
        TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_AIR, shippingPosition, flightInformation.getCountry());
        return targetDefinition;
        
    }

    private ShippingLane getEnemyShippingLane() throws PWCGException 
    {
        ShippingLaneManager shippingLaneManager = PWCGContext.getInstance().getCurrentMap().getShippingLaneManager();        
        ShippingLane selectedShippingLane = shippingLaneManager.getClosestShippingLane(flightInformation.getMission().getMissionBorders().getCenter());
        return selectedShippingLane;
    }
}
