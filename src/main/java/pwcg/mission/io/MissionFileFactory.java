package pwcg.mission.io;

import pwcg.campaign.api.IMissionFile;
import pwcg.mission.Mission;

public class MissionFileFactory
{
    public static IMissionFile createMissionFile(Mission mission)
    {
        return new MissionFileWriter(mission);
    }
}
