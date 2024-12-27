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
        PlaneMcu flightLeader = flight.getFlightPlanes().getFlightLeader();

        RunwayPlacer runwayPlacer = new RunwayPlacer();
        List<Coordinate> takeOffPositions = runwayPlacer.getFlightTakeoffPositions(flight);

        for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
        {
            plane.setPosition(takeOffPositions.get(plane.getNumberInFormation()-1));

            Orientation orient = flight.getFlightInformation().getDepartureAirfield().getTakeoffLocation(flight.getMission()).getOrientation().copy();
            plane.setOrientation(orient);

            plane.populateEntity(flight, flightLeader);
            
            int startOnRunwayVal = FlightStartPosition.START_ENGINE_WARM;
            if (plane.isRotary())
            {
            	startOnRunwayVal = FlightStartPosition.START_ENGINE_COLD;
            }
            plane.setStartInAir(startOnRunwayVal);
        }
    }
}
