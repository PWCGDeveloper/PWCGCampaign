package pwcg.mission.mcu.group;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;

public class PlaneRemoverSinglePlayer extends PlaneRemoverCoop
{
    public PlaneRemoverSinglePlayer()
    {
        super();
    }

    public void initialize(IFlight flight, PlaneMcu planeToRemove, PlaneMcu playerPlane) throws PWCGException 
    {
        super.initialize(flight, planeToRemove);
        
        // For single player we want to remove based on proximity to the player and not any plane
        // in a coalition.  Do this by adding an OL to the players plane.
        outOfEnemyRangeProximity.setObject(playerPlane.getEntity().getIndex());
    }
}
