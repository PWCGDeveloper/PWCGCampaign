package pwcg.mission;

public enum MissionProfile
{
    DAY_TACTICAL_MISSION,
    NIGHT_TACTICAL_MISSION,
    DAY_STRATEGIC_MISSION,
    NIGHT_STRATEGIC_MISSION;    

    public boolean isNightMission() 
    {
        if (this == MissionProfile.NIGHT_TACTICAL_MISSION || this == MissionProfile.NIGHT_STRATEGIC_MISSION)
        {
            return true;
        }
        
        return false;
    }
}
