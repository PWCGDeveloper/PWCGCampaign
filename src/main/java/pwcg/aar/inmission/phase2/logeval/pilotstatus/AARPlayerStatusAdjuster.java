package pwcg.aar.inmission.phase2.logeval.pilotstatus;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPilot;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SerialNumber.SerialNumberClassification;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;

public class AARPlayerStatusAdjuster
{
    public void adjustForPlayer(Campaign campaign, LogPilot playerCrewMember) throws PWCGException 
    {
        if (SerialNumber.getSerialNumberClassification(playerCrewMember.getSerialNumber()) != SerialNumberClassification.PLAYER)
    	{
    		return;
    	}
    	
    	int maxPlayerInjury = getConfiguredMaxPlayerInjury(campaign);
        if (maxPlayerInjury == 1)
        {
            playerIsNeverInjured(playerCrewMember);
        }
        else if (maxPlayerInjury == 2)
        {
            playerIsLightlyWounded(playerCrewMember);
        }
        else if (maxPlayerInjury == 3)
        {
            playerIsBadlyWounded(playerCrewMember);
        }
        else 
        {
            // Nothing - status is as per the mission, including capture or death
        }
        
        if (playerCrewMember.getStatus() <= SquadronMemberStatus.STATUS_CAPTURED)
        {
            campaign.setCampaignStatus(playerCrewMember.getStatus());
        }
    }

	private void playerIsNeverInjured(LogPilot playerCrewMember)
	{
		playerCrewMember.setStatus(SquadronMemberStatus.STATUS_ACTIVE);
	}

	private void playerIsLightlyWounded(LogPilot playerCrewMember)
	{
		if (playerCrewMember.getStatus() <= SquadronMemberStatus.STATUS_WOUNDED)
		{
		    playerCrewMember.setStatus(SquadronMemberStatus.STATUS_WOUNDED);
		}
	}

	private void playerIsBadlyWounded(LogPilot playerCrewMember)
	{
		if (playerCrewMember.getStatus() <= SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED)
		{
		    playerCrewMember.setStatus(SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED);
		}
	}

	private int getConfiguredMaxPlayerInjury(Campaign campaign) throws PWCGException
	{
		ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        int maxPlayerInjury = configManager.getIntConfigParam(ConfigItemKeys.PilotInjuryKey);
		return maxPlayerInjury;
	}

}
