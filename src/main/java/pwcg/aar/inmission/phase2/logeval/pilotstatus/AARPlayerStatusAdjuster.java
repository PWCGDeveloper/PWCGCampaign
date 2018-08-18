package pwcg.aar.inmission.phase2.logeval.pilotstatus;

import java.util.Date;

import pwcg.aar.campaigndate.AARTimePassedAfterWounds;
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
    private Campaign campaign;
    
    public AARPlayerStatusAdjuster(Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public void adjustForPlayer(LogPilot playerCrewMember) throws PWCGException 
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

	private void playerIsNeverInjured(LogPilot playerCrewMember)
	{
		playerCrewMember.setStatus(SquadronMemberStatus.STATUS_ACTIVE);
	}

	private void playerIsLightlyWounded(LogPilot playerCrewMember) throws PWCGException
	{
		if (playerCrewMember.getStatus() <= SquadronMemberStatus.STATUS_WOUNDED)
		{
		    playerCrewMember.setStatus(SquadronMemberStatus.STATUS_WOUNDED);
            setPlayerWoundedTime(playerCrewMember);
		}
	}

	private void playerIsBadlyWounded(LogPilot playerCrewMember) throws PWCGException
	{
		if (playerCrewMember.getStatus() <= SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED)
		{
		    playerCrewMember.setStatus(SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED);
		    setPlayerWoundedTime(playerCrewMember);
		}
	}

	private int getConfiguredMaxPlayerInjury() throws PWCGException
	{
		ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        int maxPlayerInjury = configManager.getIntConfigParam(ConfigItemKeys.PilotInjuryKey);
		return maxPlayerInjury;
	}


    private void setPlayerWoundedTime(LogPilot playerCrewMember) throws PWCGException
    {
        AARTimePassedAfterWounds newDateCalculator = new AARTimePassedAfterWounds(campaign);
        Date woundedDate = newDateCalculator.calcDateOfRecovery(playerCrewMember);
        playerCrewMember.setDateOfReturn(woundedDate);
    }
}
