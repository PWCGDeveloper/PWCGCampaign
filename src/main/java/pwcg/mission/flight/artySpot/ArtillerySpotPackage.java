package pwcg.mission.flight.artySpot;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderAirToGround;
import pwcg.mission.target.TargetSelectorGroundUnit;

public class ArtillerySpotPackage implements IFlightPackage
{
    private IFlightInformation flightInformation;
    private TargetDefinition targetDefinition;

    public ArtillerySpotPackage()
    {
    }

    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        this.flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.ARTILLERY_SPOT);
        this.targetDefinition = buildTargetDefintion();
        
        IFlight artySpot = createFlight();
		return artySpot;
	}

    private IFlight createFlight() throws PWCGException
    {        
        GroundUnitCollection groundUnitCollection = createGroundUnitsForFlight();
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

        flightInformation.getMission().getMissionGroundUnitBuilder().addFlightSpecificGroundUnit(groundUnitCollection);
        return artySpot;
    }

    private GroundUnitCollection createGroundUnitsForFlight() throws PWCGException
    {
        TargetSelectorGroundUnit targetBuilder = new TargetSelectorGroundUnit(flightInformation);
        return targetBuilder.findTarget();
    }

    private IFlight createAiFlight(Coordinate targetCoordinates) throws PWCGException
    {
        IFlight artySpot;        
        ArtillerySpotFlight artySpotAI = new ArtillerySpotFlight (flightInformation, targetDefinition);
        artySpotAI.createFlight();
        artySpot = artySpotAI;
        return artySpot;
    }

    private TargetDefinition buildTargetDefintion() throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilderAirToGround(flightInformation);
        return targetDefinitionBuilder.buildTargetDefinition();
    }
}
