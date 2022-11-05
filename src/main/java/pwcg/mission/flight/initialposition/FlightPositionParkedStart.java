package pwcg.mission.flight.initialposition;

import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.plane.PlaneType.PlaneSize;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
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
        Mission mission = flight.getMission();
        PlaneMcu flightLeader = flight.getFlightPlanes().getFlightLeader();
        PWCGLocation parkingLocation = flight.getFlightInformation().getDepartureAirfield().getParkingLocation(mission);
        double angleOffsetForPlanePlacement = calculateAngleForLeadToBeClosestToRunway(flight.getFlightInformation().getDepartureAirfield());
        double offsetAngle = MathUtils.adjustAngle(parkingLocation.getOrientation().getyOri(), angleOffsetForPlanePlacement);
        int planeSpacing = calculateParkedSpacing(flightLeader);

        // Initial position has already been set for ground starts
        int i = 0;
        for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
        {
            Coordinate planeCoords = MathUtils.calcNextCoord(flight.getCampaign().getCampaignMap(), parkingLocation.getPosition(), offsetAngle, planeSpacing * i);
            int startParkedVal = FlightStartPosition.START_PARKED;

            setPlanePosition(flightLeader, parkingLocation, plane, planeCoords, startParkedVal);
            ++i;
        }
    }
    
    private double calculateAngleForLeadToBeClosestToRunway(Airfield airfield) throws PWCGException
    {
        Mission mission = flight.getMission();
        PWCGLocation parkingLocation = flight.getFlightInformation().getDepartureAirfield().getParkingLocation(mission);
        PWCGLocation takeoffLocation = flight.getFlightInformation().getDepartureAirfield().getTakeoffLocation(mission);
                
        double parkingAngle = parkingLocation.getOrientation().getyOri();
        double parkingAngle90 = MathUtils.adjustAngle(parkingAngle, 90);
        double parkingAngle270 = MathUtils.adjustAngle(parkingAngle, 270);
        
        double angleParkingToTakeoff = MathUtils.calcAngle(parkingLocation.getPosition(), takeoffLocation.getPosition());
        double angle90Difference = Math.min(Math.abs((2 * Math.PI) - Math.abs(parkingAngle90 - angleParkingToTakeoff)), Math.abs(parkingAngle90 - angleParkingToTakeoff));
        double angle270Difference = Math.min(Math.abs((2 * Math.PI) - Math.abs(parkingAngle270 - angleParkingToTakeoff)), Math.abs(parkingAngle270 - angleParkingToTakeoff));
        
        double oppositeMovement = 90;
        if (angle90Difference < angle270Difference)
        {
            oppositeMovement = 270;
        }

        return oppositeMovement;
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
