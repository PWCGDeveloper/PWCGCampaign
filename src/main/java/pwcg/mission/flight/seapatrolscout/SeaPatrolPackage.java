package pwcg.mission.flight.seapatrolscout;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.target.ShippingLane;
import pwcg.campaign.target.ShippingLaneManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightPackage;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.ground.ShipConvoyGenerator;
import pwcg.mission.ground.unittypes.transport.ShipConvoyUnit;

public class SeaPatrolPackage extends FlightPackage
{
    public SeaPatrolPackage(Mission mission, Campaign campaign, Squadron squadron, boolean isPlayerFlight)
    {
        super(mission, campaign, squadron, isPlayerFlight);
        flightType = FlightTypes.SEA_PATROL;
    }

    public Flight createPackage () throws PWCGException 
	{
		// Get the target shipping unit.  
        Coordinate startCoords = squadron.determineCurrentPosition(campaign.getDate());
        ShippingLane selectedShippingLane = getShippingLane(startCoords);

        Coordinate targetPosition = selectedShippingLane.getShippingLaneBorders().getCoordinateInBox();
		
		// Now the sea patrol mission
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
        missionBeginUnit.initialize(startCoords.copy());
        
        FlightInformation flightInformation = createFlightInformation(targetPosition);
		SeaPatrolFlight seaPatrol = new SeaPatrolFlight (flightInformation, missionBeginUnit);
		
		// Add a sea unit for the player flight
        if (isPlayerFlight)
        {
            // This is really an anti-air patrol but add random shipping
            ShipConvoyGenerator shipBattleGenerator = new ShipConvoyGenerator();
            List<ShipConvoyUnit> otherConvoys = shipBattleGenerator.generateConvoys(campaign, selectedShippingLane, targetPosition, isPlayerFlight);
            for (ShipConvoyUnit convoy : otherConvoys)
            {
                seaPatrol.addLinkedUnit(convoy);
            }
        }
		
		seaPatrol.createUnitMission();

		// Maybe an opposing flight
        double interceptFlightDistance= seaPatrol.calcFlightDistance() / 2;
        Flight opposingSeaFlight = getOpposingFlight(mission, targetPosition, interceptFlightDistance);
        if (opposingSeaFlight != null)
        {
            seaPatrol.addLinkedUnit(opposingSeaFlight);
        }
        
        // Now create some random flights in the same area
        int numSeaplaneFlights = RandomNumberGenerator.getRandom(4);
        for (int i = 0; i < numSeaplaneFlights; ++i)
        {
            Coordinate otherFlightPosition = selectedShippingLane.getShippingLaneBorders().getCoordinateInBox();
            Flight otherSeaFlight = getOpposingFlight(mission, otherFlightPosition, interceptFlightDistance);
            seaPatrol.addLinkedUnit(otherSeaFlight);
        }


		return seaPatrol;
	}

    protected ShippingLane getShippingLane(Coordinate referenceCoordinate) throws PWCGException 
    {
        ShippingLane selectedShippingLane = null;
        
        ShippingLaneManager shippingLaneManager = PWCGContextManager.getInstance().getCurrentMap().getShippingLaneManager();
        selectedShippingLane = shippingLaneManager.getTargetShippingLane(referenceCoordinate, squadron.getCountry().getSide());
        
        return selectedShippingLane;
    }

	private Flight getOpposingFlight(Mission mission, Coordinate targetPosition, double distance) throws PWCGException 
	{
		SeaPlaneOpposingFlight opposingFlight = null;

		List<Role> acceptableRoles = new ArrayList<Role>();
        acceptableRoles.add(Role.ROLE_SEA_PLANE_SMALL);
        acceptableRoles.add(Role.ROLE_SEA_PLANE_LARGE);
		
		List<Squadron> opposingSquads = null;
		Side enemySide = campaign.determineCountry().getSideNoNeutral().getOppositeSide();
        opposingSquads =  PWCGContextManager.getInstance().getSquadronManager().getNearestSquadronsByRole(campaign, targetPosition.copy(), 1, 400000.0, acceptableRoles, enemySide, campaign.getDate());

		if (opposingSquads != null && opposingSquads.size() != 0)
		{
			int index= RandomNumberGenerator.getRandom(opposingSquads.size());
			Squadron opposingSquad = opposingSquads.get(index);

			String opposingFieldName = opposingSquad.determineCurrentAirfieldName(campaign.getDate());
			IAirfield opposingField =  PWCGContextManager.getInstance().getCurrentMap().getAirfieldManager().getAirfield(opposingFieldName);
			double angle = MathUtils.calcAngle(targetPosition, opposingField.getPosition());
			
			Coordinate startingPosition = MathUtils.calcNextCoord(targetPosition, angle, distance);


			// And the opposing mission
			// Target is the start of the opposing flight while the player field 
			// is the target for the opposing flight.
            MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
            missionBeginUnit.initialize(startingPosition.copy());
            
            FlightInformation opposingFlightInformation = FlightInformationFactory.buildAiFlightInformation(opposingSquad, mission, FlightTypes.SEA_PATROL, targetPosition.copy());
			opposingFlight = new SeaPlaneOpposingFlight (opposingFlightInformation, missionBeginUnit);
			opposingFlight.createUnitMission();
			
			// Start the bombers right away
			opposingFlight.getMissionBeginUnit().setStartTime(2);
		}
		
		return opposingFlight;
	}

}
