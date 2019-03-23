package pwcg.mission.flight.paradrop;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.group.Bridge;
import pwcg.campaign.group.GroupManager;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightPackage;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.ground.GroundUnitCollection;

public class ParaDropPackage extends FlightPackage
{
    public ParaDropPackage(Mission mission, Campaign campaign, Squadron squadron, FlightTypes flightType, boolean isPlayerFlight)
    {
        super(mission, campaign, squadron, isPlayerFlight);
        this.flightType = flightType;
    }

    public Flight createPackage () throws PWCGException 
	{
	    ParaDropFlight paradropFlight = null;
        paradropFlight = createPackageTarget ();
        addEscortIfNeeded(paradropFlight);
		return paradropFlight;
	}

	public ParaDropFlight createPackageTarget () throws PWCGException 
	{
        GroundUnitCollection groundUnitCollection = createGroundUnitsForFlight();
        Coordinate groundUnitCoordinates = groundUnitCollection.getTargetCoordinatesFromGroundUnits(squadron.determineEnemySide());
        Coordinate targetCoordinates = getTargetLocation(groundUnitCoordinates);
        ParaDropFlight paradropFlight = makeParaDropFlight(targetCoordinates);
        paradropFlight.linkGroundUnitsToFlight(groundUnitCollection);
        return paradropFlight;
	}
	
	private Coordinate getTargetLocation(Coordinate groundUnitCoordinates) throws PWCGException
	{
	    if (flightType == FlightTypes.PARATROOP_DROP)
	    {
	        return getTargetBridgeLocationForParaDrop(groundUnitCoordinates);
	    }
	    else
	    {
	        return groundUnitCoordinates;
	    }
	}

    private Coordinate getTargetBridgeLocationForParaDrop(Coordinate groundUnitCoordinates) throws PWCGException
    {
        GroupManager groupManager = PWCGContextManager.getInstance().getCurrentMap().getGroupManager();
        Side enemySide = squadron.determineEnemyCountry(campaign, campaign.getDate()).getSide();
        Bridge targetBridge = groupManager.getBridgeFinder().findClosestBridgeForSide(enemySide, campaign.getDate(), groundUnitCoordinates);
        Coordinate targetCoordinates = targetBridge.getPosition().copy();
        return targetCoordinates;
    }

    private ParaDropFlight makeParaDropFlight(Coordinate targetCoordinates) throws PWCGException
    {
        Coordinate startCoords = squadron.determineCurrentPosition(campaign.getDate());
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
        missionBeginUnit.initialize(startCoords.copy());
            
        FlightInformation flightInformation = createFlightInformation(targetCoordinates);
        ParaDropFlight paradropFlight = new ParaDropFlight (flightInformation, missionBeginUnit);
        
        paradropFlight.createUnitMission();

        return paradropFlight;
    }

    private void addEscortIfNeeded(ParaDropFlight paradropFlight) throws PWCGException
    {
        if (!mission.isNightMission())
        {
            addPossibleEscort(paradropFlight);
        }
    }
}
