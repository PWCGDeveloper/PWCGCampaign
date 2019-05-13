package pwcg.mission.mcu.group;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.plane.PlaneMCU;

public class PlaneRemoverSinglePlayer extends PlaneRemoverCoop
{
    public PlaneRemoverSinglePlayer()
    {
        super();
    }

    public void initialize(Flight flight, PlaneMCU planeToRemove, PlaneMCU playerPlane) throws PWCGException 
    {
        super.initialize(flight, planeToRemove);
        
        // For single player we want to remove based on proximity to the player and not any plane
        // in a coalition.  Do this by adding an OL to the players plane.
        deletePlane.setObject(playerPlane.getEntity().getIndex());
    }
}
