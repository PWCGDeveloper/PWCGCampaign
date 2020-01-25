package pwcg.mission.flight.initialposition;

import pwcg.campaign.plane.PlaneType.PlaneSize;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.FlightStartPosition;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;

public class FlightPositionParkedStart
{
    private IFlight flight;

    public FlightPositionParkedStart (IFlight flight)
    {
        this.flight = flight;
    }

    public void createPlanePositionParkedStart() throws PWCGException
    {
        PlaneMcu flightLeader = flight.getFlightPlanes().getFlightLeader();
        PWCGLocation parkingLocation = flight.getFlightInformation().getDepartureAirfield().getParkingLocation();
        double offsetAngle = MathUtils.adjustAngle(parkingLocation.getOrientation().getyOri(), 90);
        int planeSpacing = calculateParkedSpacing(flightLeader);

        // Initial position has already been set for ground starts
        int i = 0;
        for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
        {
            Coordinate planeCoords = MathUtils.calcNextCoord(parkingLocation.getPosition(), offsetAngle, planeSpacing * i);
            int startParkedVal = FlightStartPosition.START_PARKED;

            setPlanePosition(flightLeader, parkingLocation, plane, planeCoords, startParkedVal);


            ++i;
        }
    }

    private void setPlanePosition(PlaneMcu flightLeader, PWCGLocation parkingLocation, PlaneMcu plane, Coordinate planeCoords, int startParkedVal)
    {
        plane.setPosition(planeCoords);
        plane.setOrientation(parkingLocation.getOrientation().copy());
        plane.setStartInAir(startParkedVal);
        plane.populateEntity(flight, flightLeader);
    }

    private int calculateParkedSpacing(PlaneMcu flightLeader)
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
