package pwcg.campaign.skirmish;

import pwcg.mission.target.TargetType;

public class SkirmishTargetTypeMatcher
{
    public static boolean isTargetTypeMatchesSkirmishProfile(SkirmishProfileType profileType, TargetType targetType)
    {
        if (profileType == SkirmishProfileType.SKIRMISH_PROFILE_ANTI_SHIPPING)
        {
            if (targetType == TargetType.TARGET_SHIPPING)
            {
                return true;
            }
        }
        
        if (profileType == SkirmishProfileType.SKIRMISH_PROFILE_ANTI_INFANTRY)
        {
            if (targetType == TargetType.TARGET_INFANTRY)
            {
                return true;
            }
        }
        
        if (profileType == SkirmishProfileType.SKIRMISH_PROFILE_ANTI_RIVER_TRAFFIC)
        {
            if (targetType == TargetType.TARGET_DRIFTER)
            {
                return true;
            }
        }
        
        if (profileType == SkirmishProfileType.SKIRMISH_PROFILE_ANTI_RAIL)
        {
            if (targetType == TargetType.TARGET_RAIL)
            {
                return true;
            }
        }
        
        if (profileType == SkirmishProfileType.SKIRMISH_PROFILE_AIRFIELD)
        {
            if (targetType == TargetType.TARGET_AIRFIELD)
            {
                return true;
            }
        }
        
        if (profileType == SkirmishProfileType.SKIRMISH_PROFILE_CARPET_BOMB)
        {
            if (targetType == TargetType.TARGET_CITY)
            {
                return true;
            }
        }
        
        return false;
    }

}
