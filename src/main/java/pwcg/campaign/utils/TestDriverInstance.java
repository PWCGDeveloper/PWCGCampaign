package pwcg.campaign.utils;

import pwcg.core.location.Coordinate;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.target.TargetType;

public class TestDriverInstance
{
    private boolean createPlayerOnly = false;
    private boolean writeCampaignFile = true;
    private boolean debugAARLogs = false;

    private TargetType testPlayerTacticalTargetType = TargetType.TARGET_NONE;
    private Coordinate testMissionCenter = null;

    public TestDriverInstance()
    {
    }

    public boolean isCreatePlayerOnly()
    {
        return this.createPlayerOnly;
    }

    public boolean isWriteCampaignFile()
    {
        return writeCampaignFile;
    }

    public boolean isDebugAARLogs()
    {
        return debugAARLogs;
    }

    public TargetType getTestPlayerTacticalTargetType()
    {
        return testPlayerTacticalTargetType;
    }

    public void setTestPlayerTacticalTargetType(TargetType testPlayerTacticalTargetType)
    {
        this.testPlayerTacticalTargetType = testPlayerTacticalTargetType;
    }

    public Coordinate getTestMissionCenter()
    {
        return testMissionCenter;
    }

    public void setTestMissionCenter(Coordinate testMissionCenter)
    {
        this.testMissionCenter = testMissionCenter;
    }

    public void setCreatePlayerOnly(boolean createPlayerOnly)
    {
        this.createPlayerOnly = createPlayerOnly;
    }

    public void setWriteCampaignFile(boolean writeCampaignFile)
    {
        this.writeCampaignFile = writeCampaignFile;
    }

    public void setDebugAARLogs(boolean debugAARLogs)
    {
        this.debugAARLogs = debugAARLogs;
    }



    public class TestFlightType
    {
        public FlightTypes playerFlightType = FlightTypes.ANY;
        public FlightTypes aiFlightType = FlightTypes.ANY;
    }
}
