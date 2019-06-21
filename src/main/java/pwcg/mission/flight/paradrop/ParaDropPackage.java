package pwcg.mission.flight.paradrop;

import pwcg.campaign.target.unit.TargetBuilder;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.ground.GroundUnitCollection;

public class ParaDropPackage implements IFlightPackage
{
    private FlightInformation flightInformation;

    public ParaDropPackage(FlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public Flight createPackage () throws PWCGException 
	{
	    ParaDropFlight paradropFlight = null;
        paradropFlight = createPackageTarget ();
		return paradropFlight;
	}

	public ParaDropFlight createPackageTarget () throws PWCGException 
	{
        GroundUnitCollection groundUnitCollection = createGroundUnitsForFlight();
        ParaDropFlight paradropFlight = makeParaDropFlight();
        paradropFlight.linkGroundUnitsToFlight(groundUnitCollection);
        return paradropFlight;
	}

    private ParaDropFlight makeParaDropFlight() throws PWCGException
    {
        Coordinate startCoords = flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate());
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit(startCoords.copy());        
        ParaDropFlight paradropFlight = new ParaDropFlight (flightInformation, missionBeginUnit);        
        paradropFlight.createUnitMission();
        return paradropFlight;
    }

    private GroundUnitCollection createGroundUnitsForFlight() throws PWCGException
    {
        TargetBuilder targetBuilder = new TargetBuilder(flightInformation);
        targetBuilder.buildTarget();
        return targetBuilder.getGroundUnits();
    }

}
