package pwcg.mission.flight;

import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.plane.PlaneMcu;

public class FlightPayloadBuilder
{
    private IFlight flight;
    
    public FlightPayloadBuilder (IFlight flight)
    {
        this.flight = flight;
    }
    
    public void setFlightPayload() throws PWCGException
    {
        if (isHomogeneous())
        {
            setFlightPayloadHomogeneous();
        }
        else
        {
            setFlightPayloadMixed();
        }
        
        initializeFuel();
    }

    private void setFlightPayloadMixed() throws PWCGException
    {
        for (PlaneMcu plane : flight.getFlightData().getFlightPlanes().getPlanes())
        {
            plane.buildPlanePayload(flight);
        }
    }

    private void setFlightPayloadHomogeneous() throws PWCGException
    {
        PlaneMcu leadPlane = flight.getFlightData().getFlightPlanes().getFlightLeader();
        IPlanePayload payload = leadPlane.buildPlanePayload(flight);
        
        for (PlaneMcu plane : flight.getFlightData().getFlightPlanes().getPlanes())
        {
            plane.setPlanePayload(payload);
        }
    }

    private void initializeFuel() throws PWCGException
    {
        for (PlaneMcu plane : flight.getFlightData().getFlightPlanes().getPlanes())
        {
            if (!flight.getFlightData().getFlightInformation().isAirStart())
            {
                plane.setFuel(1.0);
            }
            else
            {
                plane.setFuel(0.6);
            }
        }
    }

    private boolean isHomogeneous()
    {
        PlaneMcu leadPlane = flight.getFlightData().getFlightPlanes().getFlightLeader();
        for (PlaneMcu plane : flight.getFlightData().getFlightPlanes().getPlanes())
        {
            if (!plane.getType().equals(leadPlane.getType()))
            {
                return false;
            }
        }
        
        return true;
    }

}
