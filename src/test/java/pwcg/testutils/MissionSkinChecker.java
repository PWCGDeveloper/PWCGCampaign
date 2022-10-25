package pwcg.testutils;

import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;

public class MissionSkinChecker
{
    static public boolean verifyPlanesHaveSkins (Mission mission) throws PWCGException
    {
        for (IFlight flight : mission.getFlights().getAllAerialFlights())
        {
            for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
            {
                if (plane.getSkin() == null || plane.getSkin().getSkinName().isEmpty())
                {
                    throw new PWCGException("No skin for plane " + plane.getType() + " in flight type " + flight.getFlightType());
                }
                else
                {
                    System.out.println(plane.getSkin().getSkinName());
                }
            }
        }
        
        return false;
    }
}
