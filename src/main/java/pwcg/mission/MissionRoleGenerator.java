package pwcg.mission;

import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.plane.PwcgRole;
import pwcg.campaign.squadron.Company;
import pwcg.core.exception.PWCGException;

public class MissionRoleGenerator
{

    public static PwcgRole getMissionRole(Campaign campaign, Map<Integer, PwcgRole> squadronRoleOverride, Company playerSquadron) throws PWCGException
    {
        PwcgRole missionRole = playerSquadron.getSquadronRoles().selectRoleForMission(campaign.getDate());
        if (squadronRoleOverride.containsKey(playerSquadron.getSquadronId()))
        {
            missionRole = squadronRoleOverride.get(playerSquadron.getSquadronId());
        }
        return missionRole;
    }

    public static PwcgRole getMissionRole(Campaign campaign, Company playerSquadron) throws PWCGException
    {
        PwcgRole missionRole = playerSquadron.getSquadronRoles().selectRoleForMission(campaign.getDate());
        return missionRole;
    }
}
