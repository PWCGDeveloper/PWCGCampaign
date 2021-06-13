package pwcg.mission.options;

public enum MissionType 
{
    SINGLE_MISSION(0),
    COOP_MISSION(1),
    SINGLE_AAA_MISSION(0),
    COOP_AAA_MISSION(1);

    
    private int missionTypeCode;
    
    private MissionType(int missionTypeCode)
    {
        this.missionTypeCode = missionTypeCode;
    }

    public int getMissionTypeCode()
    {
        return missionTypeCode;
    }
}
