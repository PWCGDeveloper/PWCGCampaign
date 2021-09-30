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
        for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
        {
            plane.buildPlanePayload(flight, flight.getCampaign().getDate());
        }
    }

    private void setFlightPayloadHomogeneous() throws PWCGException
    {
        PlaneMcu leadPlane = flight.getFlightPlanes().getFlightLeader();
        IPlanePayload payload = leadPlane.buildPlanePayload(flight, flight.getCampaign().getDate());
        
        for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
        {
            plane.setPlanePayload(payload);
        }
    }

    private void initializeFuel() throws PWCGException
    {
        double fuelLoad = Fuel.calculateFuelForFlight(flight);
        for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
        {
            plane.setFuel(fuelLoad);
        }
    }

    private boolean isHomogeneous()
    {
        PlaneMcu leadPlane = flight.getFlightPlanes().getFlightLeader();
        for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
        {
            if (!plane.getType().equals(leadPlane.getType()))
            {
                return false;
            }
        }
        
        return true;
    }

}
