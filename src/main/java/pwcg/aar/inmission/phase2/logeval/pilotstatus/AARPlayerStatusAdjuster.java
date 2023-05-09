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
    public static final int MAX_INJURY_NONE = 1;
    public static final int MAX_INJURY_WOUNDED = 2;
    public static final int MAX_INJURY_SERIOUSLY_WOUNDED = 3;
    public static final int MAX_INJURY_DEAD = 4;
    
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
        
        adjustPlayerInjury(playerCrewMember);
    	
    	int maxPlayerInjury = getConfiguredMaxPlayerInjury();
        if (maxPlayerInjury == MAX_INJURY_NONE)
        {
            playerIsNeverInjured(playerCrewMember);
        }
        else if (maxPlayerInjury == MAX_INJURY_WOUNDED)
        {
            playerIsLightlyWounded(playerCrewMember);
        }
        else if (maxPlayerInjury == MAX_INJURY_SERIOUSLY_WOUNDED)
        {
            playerIsBadlyWounded(playerCrewMember);
        }
        else 
        {
            // Nothing - status is as per the mission, including capture or death
        }
    }

	private void adjustPlayerInjury(LogPilot playerCrewMember) throws PWCGException
    {
	    int playerInjuryAdjustment = getConfiguredPlayerInjuryAdjustment();
        int playerStatus = playerCrewMember.getStatus();
	    for (int statusAdjustment = 0; statusAdjustment < playerInjuryAdjustment; ++statusAdjustment)
	    {
            if (playerStatus == SquadronMemberStatus.STATUS_ACTIVE)
            {
                break;
            }
            else if (playerStatus < SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED)
            {
                playerStatus = SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED;
            }
            else if (playerStatus == SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED)
            {
                playerStatus = SquadronMemberStatus.STATUS_WOUNDED;
            }
            else if (playerStatus == SquadronMemberStatus.STATUS_WOUNDED)
            {
                playerStatus = SquadronMemberStatus.STATUS_ACTIVE;
            }
	    }
        playerCrewMember.setStatus(playerStatus);
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
		}
	}

	private void playerIsBadlyWounded(LogPilot playerCrewMember) throws PWCGException
	{
		if (playerCrewMember.getStatus() <= SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED)
		{
		    playerCrewMember.setStatus(SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED);
		}
	}

    private int getConfiguredMaxPlayerInjury() throws PWCGException
    {
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        int maxPlayerInjury = configManager.getIntConfigParam(ConfigItemKeys.PilotInjuryKey);
        return maxPlayerInjury;
    }

    private int getConfiguredPlayerInjuryAdjustment() throws PWCGException
    {
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        int playerInjuryAdjustment = configManager.getIntConfigParam(ConfigItemKeys.PilotInjuryAdjustKey);
        return playerInjuryAdjustment;
    }
}
