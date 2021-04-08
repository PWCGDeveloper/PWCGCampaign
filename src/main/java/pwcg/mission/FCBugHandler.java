package pwcg.mission;

import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;

public class FCBugHandler
{
    public static void fcBugs(Mission mission)
    {
        for (IFlight flight : mission.getMissionFlights().getAllAerialFlights())
        {
            for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
            {
                plane.setFuel(1.0);
                plane.setAiRTBDecision(0);
            }
        }
    }
}
