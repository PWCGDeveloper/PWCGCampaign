package pwcg.mission.flight.paradrop;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.ground.factory.TargetFactory;
import pwcg.mission.ground.org.IGroundUnitCollection;

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
        IGroundUnitCollection groundUnitCollection = createGroundUnitsForFlight();
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

    private IGroundUnitCollection createGroundUnitsForFlight() throws PWCGException
    {
        TargetFactory targetBuilder = new TargetFactory(flightInformation);
        targetBuilder.buildTarget();
        return targetBuilder.getGroundUnits();
    }

}
