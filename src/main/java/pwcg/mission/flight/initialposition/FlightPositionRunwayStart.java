package pwcg.mission.flight.initialposition;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.flight.FlightStartPosition;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;

public class FlightPositionRunwayStart
{
    private IFlight flight;
    
    public FlightPositionRunwayStart (IFlight flight)
    {
        this.flight = flight;
    }

    public void createPlanePositionRunway() throws PWCGException 
    {
        PlaneMcu flightLeader = flight.getFlightData().getFlightPlanes().getFlightLeader();

        RunwayPlacer runwayPlacer = new RunwayPlacer();
        List<Coordinate> takeOffPositions = runwayPlacer.getFlightTakeoffPositions(flight);

        for (PlaneMcu plane : flight.getFlightData().getFlightPlanes().getPlanes())
        {
            plane.setPosition(takeOffPositions.get(plane.getNumberInFormation()-1));

            Orientation orient = flight.getFlightData().getFlightInformation().getDepartureAirfield().getTakeoffLocation().getOrientation().copy();
            plane.setOrientation(orient);

            plane.populateEntity(flight, flightLeader);
            
            int startOnRunwayVal = FlightStartPosition.START_ON_RUNWAY;
            plane.setStartInAir(startOnRunwayVal);
        }
    }
}
