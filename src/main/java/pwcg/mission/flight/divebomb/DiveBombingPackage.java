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

public class DiveBombingPackage implements IFlightPackage
{
    private IFlightInformation flightInformation;

    public DiveBombingPackage()
    {
    }

    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        this.flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.DIVE_BOMB);

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
        DiveBombingFlight diveBombingFlight = new DiveBombingFlight (flightInformation);
        diveBombingFlight.createFlight();
        return diveBombingFlight;
    }

    private IGroundUnitCollection createGroundUnitsForFlight() throws PWCGException
    {
        TargetFactory targetBuilder = new TargetFactory(flightInformation);
        targetBuilder.buildTarget();
        return targetBuilder.getGroundUnits();
    }
}
