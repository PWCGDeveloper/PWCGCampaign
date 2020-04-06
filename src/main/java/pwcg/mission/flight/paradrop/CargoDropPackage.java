package pwcg.mission.flight.paradrop;

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

public class CargoDropPackage implements IFlightPackage
{
    private IFlightInformation flightInformation;
    private TargetDefinition targetDefinition;

    public CargoDropPackage()
    {
    }

    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        this.flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.CARGO_DROP);
        this.targetDefinition = buildTargetDefintion();

        IFlight paraDropFlight = createPackageTacticalTarget ();
        return paraDropFlight;
    }

    public IFlight createPackageTacticalTarget () throws PWCGException 
    {
        IGroundUnitCollection groundUnitCollection = createGroundUnitsForFlight();
        Coordinate targetCoordinates = groundUnitCollection.getTargetCoordinatesFromGroundUnits(flightInformation.getSquadron().determineEnemySide());

        IFlight paraDropFlight = makeParaDropFlight(targetCoordinates);
        paraDropFlight.addLinkedGroundUnit(groundUnitCollection);

        return paraDropFlight;
    }

    private IFlight makeParaDropFlight(Coordinate targetCoordinates) throws PWCGException
    {
        ParaDropFlight paraDropFlight = new ParaDropFlight (flightInformation, targetDefinition);
        paraDropFlight.createFlight();
        return paraDropFlight;
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
