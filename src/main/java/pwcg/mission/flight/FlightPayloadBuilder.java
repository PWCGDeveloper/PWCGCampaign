package pwcg.mission.flight;

import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.plane.PlaneMCU;

public class FlightPayloadBuilder
{
    private Flight flight;
    
    public FlightPayloadBuilder (Flight flight)
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
        for (PlaneMCU plane : flight.getPlanes())
        {
            plane.buildPlanePayload(flight);
        }
    }

    private void setFlightPayloadHomogeneous() throws PWCGException
    {
        PlaneMCU leadPlane = flight.getLeadPlane();
        IPlanePayload payload = leadPlane.buildPlanePayload(flight);
        
        for (PlaneMCU plane : flight.getPlanes())
        {
            plane.setPlanePayload(payload);
        }
    }

    private void initializeFuel() throws PWCGException
    {
        for (PlaneMCU plane : flight.getPlanes())
        {
            if (!flight.isAirStart())
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
        PlaneMCU leadPlane = flight.getLeadPlane();
        for (PlaneMCU plane : flight.getPlanes())
        {
            if (!plane.getType().equals(leadPlane.getType()))
            {
                return false;
            }
        }
        
        return true;
    }

}
