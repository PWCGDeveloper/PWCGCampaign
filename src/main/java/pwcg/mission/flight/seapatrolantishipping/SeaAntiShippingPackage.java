package pwcg.mission.flight.seapatrolantishipping;

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

public class SeaAntiShippingPackage extends FlightPackage
{
    public SeaAntiShippingPackage(Mission mission, Campaign campaign, Squadron squadron, boolean isPlayerFlight)
    {
    	super(mission, campaign, squadron, isPlayerFlight);
    	flightType = FlightTypes.ANTI_SHIPPING;
    }

	public Flight createPackage () throws PWCGException 
	{
        Coordinate startCoords = squadron.determineCurrentPosition(campaign.getDate());

        ShippingLane selectedShippingLane = getEnemyShippingLane(
                        mission, 
                        squadron.determineSquadronPrimaryRole(campaign.getDate()), 
                        startCoords, 
                        squadron.determineSquadronCountry(campaign.getDate()).getSide().getOppositeSide());

        Coordinate targetPosition = selectedShippingLane.getShippingLaneBorders().getCoordinateInBox();
		
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
        missionBeginUnit.initialize(startCoords.copy());
        
        FlightInformation flightInformation = createFlightInformation(targetPosition);
		SeaAntiShippingFlight seaPatrol = new SeaAntiShippingFlight (flightInformation, missionBeginUnit);
		generateConvoysForPlayerFlight(isPlayerFlight, selectedShippingLane, targetPosition, seaPatrol);
		
		seaPatrol.createUnitMission();

		addOpposingFlightForSeapatrol(mission, squadron, targetPosition, seaPatrol);
		

		return seaPatrol;
	}

	private void generateConvoysForPlayerFlight(
	        boolean isPlayerFlight, 
	        ShippingLane selectedShippingLane,
	        Coordinate targetPosition, 
	        SeaAntiShippingFlight seaPatrol) throws PWCGException
	{
		if (isPlayerFlight)
		{
		    ShipConvoyGenerator shipConvoyGenerator = new ShipConvoyGenerator();
            List<ShipConvoyUnit> otherConvoys = shipConvoyGenerator.generateConvoys(campaign, selectedShippingLane, targetPosition, isPlayerFlight);
            for (ShipConvoyUnit convoy : otherConvoys)
            {
                seaPatrol.addLinkedUnit(convoy);
            }
		}
	}

	private void addOpposingFlightForSeapatrol(Mission mission, Squadron squadron, Coordinate targetPosition,
	        SeaAntiShippingFlight seaPatrol) throws PWCGException
	{
		int opposingOdds = RandomNumberGenerator.getRandom(100);
		if (opposingOdds < getOppositionOdds(squadron.determineSquadronPrimaryRole(campaign.getDate())))
		{
			double interceptFlightDistance= seaPatrol.calcFlightDistance() / 2;

			Flight opposingSeaFlight = getOpposingFlight(mission, targetPosition, interceptFlightDistance);
			if (opposingSeaFlight != null)
			{
				seaPatrol.addLinkedUnit(opposingSeaFlight);
			}
		}
	}

	private int getOppositionOdds(Role role)
	{
		int odds = 0;
		if (role == Role.ROLE_SEA_PLANE_LARGE)
		{
			odds = 20;
		}
		
		return odds;
	}

	protected ShippingLane getEnemyShippingLane(Mission mission, Role role, Coordinate referenceCoordinate, Side enemySide) throws PWCGException 
	{
	    ShippingLane selectedShippingLane = null;
	    
        ShippingLaneManager shippingLaneManager = PWCGContextManager.getInstance().getCurrentMap().getShippingLaneManager();
        
	    if (role == Role.ROLE_SEA_PLANE_LARGE)
	    {
	        selectedShippingLane = shippingLaneManager.getTargetShippingLane(referenceCoordinate, enemySide);
	    }
	    else
	    {
	        selectedShippingLane = shippingLaneManager.getTargetShippingLane(referenceCoordinate, enemySide);
	    }
	    
	    return selectedShippingLane;
	}

	private Flight getOpposingFlight(Mission mission, Coordinate targetPosition, double distance) throws PWCGException 
	{
		SeaAntiShippingOpposingFlight opposingFlight = null;

		List<Role> acceptableRoles = new ArrayList<Role>();
        acceptableRoles.add(Role.ROLE_SEA_PLANE_SMALL);
		
		List<Squadron> opposingSquads = null;
        Side enemySide = squadron.determineEnemySide();
        opposingSquads =  PWCGContextManager.getInstance().getSquadronManager().getNearestSquadronsByRole(campaign, targetPosition.copy(), 1, 400000.0, acceptableRoles, enemySide, campaign.getDate());

		if (opposingSquads != null && opposingSquads.size() != 0)
		{
			int index= RandomNumberGenerator.getRandom(opposingSquads.size());
			Squadron opposingSquad = opposingSquads.get(index);

			String opposingFieldName = opposingSquad.determineCurrentAirfieldName(campaign.getDate());
			IAirfield opposingField =  PWCGContextManager.getInstance().getCurrentMap().getAirfieldManager().getAirfield(opposingFieldName);
			double angle = MathUtils.calcAngle(targetPosition, opposingField.getPosition());
			
			Coordinate startingPosition = MathUtils.calcNextCoord(targetPosition, angle, distance);

            MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
            missionBeginUnit.initialize(startingPosition.copy());
            
            FlightInformation opposingFlightInformation = FlightInformationFactory.buildAiFlightInformation(opposingSquad, mission, FlightTypes.SEA_PATROL, targetPosition);
			opposingFlight = new SeaAntiShippingOpposingFlight (opposingFlightInformation, missionBeginUnit, startingPosition);
			opposingFlight.createUnitMission();
			
			opposingFlight.getMissionBeginUnit().setStartTime(2);
		}
		
		return opposingFlight;
	}

}
