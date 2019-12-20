package pwcg.mission.flight.initialposition;

import pwcg.campaign.plane.PlaneType.PlaneSize;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightStartPosition;
import pwcg.mission.flight.plane.PlaneMCU;

public class FlightPositionParkedStart
{
    private Flight flight;

    public FlightPositionParkedStart (Flight flight)
    {
        this.flight = flight;
    }

    public void createPlanePositionParkedStart() throws PWCGException
    {
        PlaneMCU flightLeader = flight.getFlightLeader();
        PWCGLocation parkingLocation = flight.getFlightInformation().getDepartureAirfield().getParkingLocation();
        double offsetAngle = MathUtils.adjustAngle(parkingLocation.getOrientation().getyOri(), 90);
        int planeSpacing = calculateParkedSpacing(flightLeader);

        // Initial position has already been set for ground starts
        int i = 0;
        for (PlaneMCU plane : flight.getPlanes())
        {
            Coordinate planeCoords = MathUtils.calcNextCoord(parkingLocation.getPosition(), offsetAngle, planeSpacing * i);

            plane.setPosition(planeCoords);
            plane.setOrientation(parkingLocation.getOrientation().copy());

            // This must be done last
            plane.populateEntity(flight, flightLeader);

            int startParkedVal = FlightStartPosition.START_PARKED;
            plane.setStartInAir(startParkedVal);

            ++i;
        }
    }

    protected int calculateParkedSpacing(PlaneMCU flightLeader)
    {
        int spacing = 20;
        if (flightLeader.getPlaneSize() == PlaneSize.PLANE_SIZE_MEDIUM)
        {
            spacing += 10;
        }
        else if (flightLeader.getPlaneSize() == PlaneSize.PLANE_SIZE_LARGE)
        {
            spacing += 55;
        }
        return spacing;
    }
}
