package pwcg.mission.io;

import pwcg.campaign.api.IMissionFile;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.ww1.io.RoFMissionFile;
import pwcg.campaign.ww2.io.BoSMissionFile;
import pwcg.mission.Mission;

public class MissionFileFactory
{
    public static IMissionFile createMissionFile(Mission mission)
    {
        if (PWCGContextManager.isRoF())
        {
            return new RoFMissionFile(mission);
        }
        else
        {
            return new BoSMissionFile(mission);
        }
    }
}
