package pwcg.aar.outofmission.phase2.awards;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;

public class MissionsFlownCalculator
{
	public static int calculateMissionsFlown(Campaign campaign, CrewMember crewMember) throws PWCGException 
    {        
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        int missionCreditedPerMissionFlown = configManager.getIntConfigParam(ConfigItemKeys.MissionsCreditedKey);        

        if (missionCreditedPerMissionFlown < 1)
        {
            missionCreditedPerMissionFlown = 1;
        }

        int updatedMissionsFlown = crewMember.getBattlesFought() + missionCreditedPerMissionFlown;
        return updatedMissionsFlown;
    }
}
