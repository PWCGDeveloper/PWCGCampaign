package pwcg.mission;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.options.MissionOptions;

/**
 * Convert a mission to Coop
 * 
 * @author Patrick Wilson
 *
 */
public class MissionCoopConverter
{
    /**
     * Set coop parameters
     * 
     * @
     */

    public void convertToCoop(MissionFlightBuilder missionFlightBuilder) 
    {
        MissionOptions missionOptions = PWCGContextManager.getInstance().getCurrentMap().getMissionOptions();
        missionOptions.setMissionType(MissionOptions.COOP_MISSION);

        for (Flight flight : missionFlightBuilder.getAllAerialFlights())
        {
            for (PlaneMCU plane : flight.getPlanes())
            {
                plane.setCoopStart(1);
            }
        }
    }
}
