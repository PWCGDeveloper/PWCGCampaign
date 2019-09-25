package pwcg.mission.flight.bomb;

import pwcg.campaign.target.unit.TargetBuilder;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.ground.GroundUnitCollection;

public class BombingPackage implements IFlightPackage
{
    private FlightInformation flightInformation;

    public BombingPackage(FlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    @Override
    public Flight createPackage () throws PWCGException 
	{
	    BombingFlight bombingFlight = createPackageTacticalTarget ();
		return bombingFlight;
	}

	public BombingFlight createPackageTacticalTarget () throws PWCGException 
	{
        GroundUnitCollection groundUnitCollection = createGroundUnitsForFlight();
        Coordinate targetCoordinates = groundUnitCollection.getTargetCoordinatesFromGroundUnits(flightInformation.getSquadron().determineEnemySide());

        BombingFlight bombingFlight = makeBombingFlight(targetCoordinates);
        bombingFlight.linkGroundUnitsToFlight(groundUnitCollection);

        return bombingFlight;
	}

    private BombingFlight makeBombingFlight(Coordinate targetCoordinates)
                    throws PWCGException
    {
	    Coordinate startCoords = flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate());
	    MissionBeginUnit missionBeginUnit = new MissionBeginUnit(startCoords.copy());	        
        BombingFlight bombingFlight = new BombingFlight (flightInformation, missionBeginUnit);

	    bombingFlight.createUnitMission();
	    
	    return bombingFlight;
    }

    private GroundUnitCollection createGroundUnitsForFlight() throws PWCGException
    {
        TargetBuilder targetBuilder = new TargetBuilder(flightInformation);
        targetBuilder.buildTarget();
        return targetBuilder.getGroundUnits();
    }
}
