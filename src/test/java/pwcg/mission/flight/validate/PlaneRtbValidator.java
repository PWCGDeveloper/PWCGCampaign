package pwcg.mission.flight.validate;

import org.junit.jupiter.api.Assertions;

import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;

public class PlaneRtbValidator
{
    public static void verifyPlaneRtbDisabled(Mission mission)
    {
        for (IFlight flight : mission.getFlights().getAllAerialFlights())
        {
            for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
            {
                Assertions.assertTrue (plane.getAiRTBDecision() == 0);
            }
        }
    }

    public static void verifyPlaneRtbEnabled(Mission mission)
    {
        for (IFlight flight : mission.getFlights().getAllAerialFlights())
        {
            for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
            {
                Assertions.assertTrue (plane.getAiRTBDecision() == 1);
            }
        }
    }

}
