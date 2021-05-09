package pwcg.campaign.utils;

import pwcg.core.location.Coordinate;
import pwcg.mission.target.TargetType;

public class TestDriver
{
    private static TestDriver testDriver = new TestDriver();

    private TestDriverInstance testDriverInstance = new TestDriverInstance();
    private boolean enabled = false;

    private TestDriver()
    {
        reset();
    }
    
    public static TestDriver getInstance()
    {
        return testDriver;
    }

    public void enableTestDriver()
    {
        this.enabled = true;
    }

    public void reset()
    {
        enabled = false;
        testDriverInstance = new TestDriverInstance();
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public boolean isCreatePlayerOnly()
    {
        if (enabled)
        {
            return testDriverInstance.isCreatePlayerOnly();
        }

        return false;
    }

    public boolean isWriteCampaignFile()
    {
        if (enabled)
        {
            return testDriverInstance.isWriteCampaignFile();
        }

        return false;
    }

    public boolean isDebugAARLogs()
    {
        return testDriverInstance.isDebugAARLogs();
    }

    public TargetType getTestPlayerTacticalTargetType()
    {
        return testDriverInstance.getTestPlayerTacticalTargetType();
    }

    public void setTestPlayerTacticalTargetType(TargetType testPlayerTacticalTargetType)
    {
        testDriverInstance.setTestPlayerTacticalTargetType(testPlayerTacticalTargetType);
    }

    public Coordinate getTestMissionCenter()
    {
        if (enabled)
        {
            return testDriverInstance.getTestMissionCenter();
        }
        return null;
    }


    public void setTestMissionCenter(Coordinate testMissionCenter)
    {
        testDriverInstance.setTestMissionCenter(testMissionCenter);
    }
}
