package pwcg.mission.flight;

import java.util.ArrayList;
import java.util.List;

import pwcg.mission.flight.plane.PlaneMcu;

public class FlightElement
{
    private List<PlaneMcu> planes = new ArrayList<>();
    
    public List<PlaneMcu> getPlanesInElement()
    {
        return planes;
    }
    
    public void addPlaneToElement(PlaneMcu plane)
    {
        planes.add(plane);
    }
}
