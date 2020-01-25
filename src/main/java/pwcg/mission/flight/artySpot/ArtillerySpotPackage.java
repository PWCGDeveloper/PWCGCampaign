package pwcg.mission.flight.artySpot;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.ground.factory.TargetFactory;
import pwcg.mission.ground.org.IGroundUnitCollection;

public class ArtillerySpotPackage implements IFlightPackage
{
    private IFlightInformation flightInformation;

    public ArtillerySpotPackage(IFlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public IFlight createPackage () throws PWCGException 
    {
        IFlight artySpot = createFlight();
		return artySpot;
	}

    private IFlight createFlight() throws PWCGException
    {        
        IGroundUnitCollection groundUnitCollection = createGroundUnitsForFlight();
        Coordinate targetCoordinates = groundUnitCollection.getTargetCoordinatesFromGroundUnits(flightInformation.getSquadron().determineEnemySide());
		IFlight artySpot = null;
		if (flightInformation.isPlayerFlight())
		{
		    throw new PWCGException("Player artillery spot not supported");
		}
		else
		{
            artySpot = createAiFlight(targetCoordinates);
		}

        artySpot.getLinkedGroundUnits().addLinkedGroundUnit(groundUnitCollection);
        return artySpot;
    }

    private IGroundUnitCollection createGroundUnitsForFlight() throws PWCGException
    {
        TargetFactory targetBuilder = new TargetFactory(flightInformation);
        targetBuilder.buildTarget();
        return targetBuilder.getGroundUnits();
    }

    private IFlight createAiFlight(Coordinate targetCoordinates) throws PWCGException
    {
        IFlight artySpot;        
        ArtillerySpotFlight artySpotAI = new ArtillerySpotFlight (flightInformation);
        artySpotAI.createFlight();
        artySpot = artySpotAI;
        return artySpot;
    }
}
