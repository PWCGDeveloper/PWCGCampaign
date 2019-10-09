package pwcg.mission;

import pwcg.campaign.context.PWCGContext;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.options.MissionOptions;


public class MissionCoopConverter
{

    public void convertToCoop(MissionFlightBuilder missionFlightBuilder) throws PWCGException 
    {
        MissionOptions missionOptions = PWCGContext.getInstance().getCurrentMap().getMissionOptions();
        missionOptions.setMissionType(MissionOptions.COOP_MISSION);

        for (Flight flight : missionFlightBuilder.getAllAerialFlights())
        {
            for (PlaneMCU plane : flight.getPlanes())
            {
                if (plane.isActivePlayerPlane())
                {
                    plane.setCoopStart(1);
                    plane.setAiLevel(AiSkillLevel.ACE);
                }
                else
                {
                    plane.setCoopStart(0);
                }
            }
        }
    }
}
