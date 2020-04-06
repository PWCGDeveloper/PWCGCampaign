package pwcg.mission.flight.divebomb;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.ground.factory.TargetFactory;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderFactory;

public class DiveBombingPackage implements IFlightPackage
{
    private IFlightInformation flightInformation;
    private TargetDefinition targetDefinition;

    public DiveBombingPackage()
    {
    }

    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        this.flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.DIVE_BOMB);
        this.targetDefinition = buildTargetDefintion();

        IFlight bombingFlight = createPackageTacticalTarget ();
        return bombingFlight;
    }

    public IFlight createPackageTacticalTarget () throws PWCGException 
    {
        IGroundUnitCollection groundUnitCollection = createGroundUnitsForFlight();
        Coordinate targetCoordinates = groundUnitCollection.getTargetCoordinatesFromGroundUnits(flightInformation.getSquadron().determineEnemySide());

        IFlight diveBombingFlight = makeDiveBombingFlight(targetCoordinates);
        diveBombingFlight.addLinkedGroundUnit(groundUnitCollection);
        return diveBombingFlight;
    }

    private IFlight makeDiveBombingFlight(Coordinate targetCoordinates) throws PWCGException
    {
        DiveBombingFlight diveBombingFlight = new DiveBombingFlight (flightInformation, targetDefinition);
        diveBombingFlight.createFlight();
        return diveBombingFlight;
    }

    private IGroundUnitCollection createGroundUnitsForFlight() throws PWCGException
    {
        TargetFactory targetBuilder = new TargetFactory(flightInformation, targetDefinition);
        targetBuilder.buildTarget();
        return targetBuilder.getGroundUnits();
    }

    private TargetDefinition buildTargetDefintion() throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = TargetDefinitionBuilderFactory.createFlightTargetDefinitionBuilder(flightInformation);
        return  targetDefinitionBuilder.buildTargetDefinition();
    }
}
