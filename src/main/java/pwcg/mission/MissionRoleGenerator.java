package pwcg.mission;

import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.tank.PwcgRole;
import pwcg.core.exception.PWCGException;

public class MissionRoleGenerator
{

    public static PwcgRole getMissionRole(Campaign campaign, Map<Integer, PwcgRole> squadronRoleOverride, Company playerSquadron) throws PWCGException
    {
        PwcgRole missionRole = playerSquadron.getSquadronRoles().selectRoleForMission(campaign.getDate());
        if (squadronRoleOverride.containsKey(playerSquadron.getCompanyId()))
        {
            missionRole = squadronRoleOverride.get(playerSquadron.getCompanyId());
        }
        return missionRole;
    }

    public static PwcgRole getMissionRole(Campaign campaign, Company playerSquadron) throws PWCGException
    {
        PwcgRole missionRole = playerSquadron.getSquadronRoles().selectRoleForMission(campaign.getDate());
        return missionRole;
    }
}
