package pwcg.aar.inmission.phase2.logeval.crewmemberstatus;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogCrewMember;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.crewmember.SerialNumber.SerialNumberClassification;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;

public class AARPlayerStatusAdjuster
{
    private Campaign campaign;
    
    public AARPlayerStatusAdjuster(Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public void adjustForPlayer(LogCrewMember playerCrewMember) throws PWCGException 
    {
        if (SerialNumber.getSerialNumberClassification(playerCrewMember.getSerialNumber()) != SerialNumberClassification.PLAYER)
    	{
    		return;
    	}
    	
    	int maxPlayerInjury = getConfiguredMaxPlayerInjury();
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
    }

	private void playerIsNeverInjured(LogCrewMember playerCrewMember)
	{
		playerCrewMember.setStatus(CrewMemberStatus.STATUS_ACTIVE);
	}

	private void playerIsLightlyWounded(LogCrewMember playerCrewMember) throws PWCGException
	{
		if (playerCrewMember.getStatus() <= CrewMemberStatus.STATUS_WOUNDED)
		{
		    playerCrewMember.setStatus(CrewMemberStatus.STATUS_WOUNDED);
		}
	}

	private void playerIsBadlyWounded(LogCrewMember playerCrewMember) throws PWCGException
	{
		if (playerCrewMember.getStatus() <= CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED)
		{
		    playerCrewMember.setStatus(CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED);
		}
	}

	private int getConfiguredMaxPlayerInjury() throws PWCGException
	{
		ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        int maxPlayerInjury = configManager.getIntConfigParam(ConfigItemKeys.CrewMemberInjuryKey);
		return maxPlayerInjury;
	}
}
