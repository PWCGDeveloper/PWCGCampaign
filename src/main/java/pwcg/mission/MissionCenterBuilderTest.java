package pwcg.mission;

import pwcg.campaign.utils.TestDriver;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

public class MissionCenterBuilderTest implements IMissionCenterBuilder
{
    public MissionCenterBuilderTest()
    {
    }

    @Override
    public Coordinate findMissionCenter(int missionBoxRadius) throws PWCGException
    {
        return TestDriver.getInstance().getTestMissionCenter();
    }

}
