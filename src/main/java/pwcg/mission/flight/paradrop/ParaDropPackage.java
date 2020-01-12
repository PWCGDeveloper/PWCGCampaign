package pwcg.mission.flight.paradrop;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.ground.factory.TargetFactory;
import pwcg.mission.ground.org.IGroundUnitCollection;

public class ParaDropPackage implements IFlightPackage
{
    private IFlightInformation flightInformation;

    public ParaDropPackage(IFlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    @Override
    public IFlight createPackage () throws PWCGException 
    {
        IFlight paraDropFlight = createPackageTacticalTarget ();
        return paraDropFlight;
    }

    public IFlight createPackageTacticalTarget () throws PWCGException 
    {
        IGroundUnitCollection groundUnitCollection = createGroundUnitsForFlight();
        Coordinate targetCoordinates = groundUnitCollection.getTargetCoordinatesFromGroundUnits(flightInformation.getSquadron().determineEnemySide());

        IFlight paraDropFlight = makeParaDropFlight(targetCoordinates);
        paraDropFlight.getFlightData().getLinkedGroundUnits().addLinkedGroundUnit(groundUnitCollection);

        return paraDropFlight;
    }

    private IFlight makeParaDropFlight(Coordinate targetCoordinates) throws PWCGException
    {
        ParaDropFlight paraDropFlight = new ParaDropFlight (flightInformation);
        paraDropFlight.createFlight();
        return paraDropFlight;
    }

    private IGroundUnitCollection createGroundUnitsForFlight() throws PWCGException
    {
        TargetFactory targetBuilder = new TargetFactory(flightInformation);
        targetBuilder.buildTarget();
        return targetBuilder.getGroundUnits();
    }
}
