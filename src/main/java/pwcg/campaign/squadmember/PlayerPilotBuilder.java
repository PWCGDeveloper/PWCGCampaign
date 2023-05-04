package pwcg.campaign.squadmember;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.factory.CampaignModeFactory;
import pwcg.coop.CoopUserManager;
import pwcg.coop.model.CoopUser;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;

public class PlayerPilotBuilder
{
    private Campaign campaign;
    
    public PlayerPilotBuilder(Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public void buildPlayerPilot(String playerName, String squadronName, String rank, String coopUser) throws PWCGUserException, Exception, PWCGException
    {
        ISquadronMemberReplacer squadronMemberReplacer = CampaignModeFactory.makeSquadronMemberReplacer(campaign);
        SquadronMember newSquadronMember = squadronMemberReplacer.createPersona(playerName, rank, squadronName, coopUser);
        
        persistCampaignAndCoopUser(coopUser, newSquadronMember);
        setReferencePilotIfOnlyActivePlayerPilot();
    }

    private void persistCampaignAndCoopUser(String coopUser, SquadronMember newSquadronMember) throws PWCGException
    {
        campaign.write();        
        campaign.reopen();
        if (campaign.getCampaignData().getCampaignMode() != CampaignMode.CAMPAIGN_MODE_SINGLE)
        {
            createCoopUserAndPersona(campaign, newSquadronMember, coopUser);
        }
    }

    private void createCoopUserAndPersona(Campaign campaign, SquadronMember player, String coopUsername) throws PWCGException
    {
        if (campaign.getCampaignData().getCampaignMode() != CampaignMode.CAMPAIGN_MODE_SINGLE)
        {
            CoopUser coopUser = CoopUserManager.getIntance().getCoopUser(coopUsername);
            if (coopUser == null)
            {
                coopUser = CoopUserManager.getIntance().buildCoopUser(coopUsername);
            }
            
            CoopUserManager.getIntance().createCoopPersona(campaign.getName(), player, coopUsername);
        }
    }

    private void setReferencePilotIfOnlyActivePlayerPilot() throws PWCGException
    {
        SquadronMembers activePlayers = campaign.getPersonnelManager().getAllActivePlayers();
        if (activePlayers.getSquadronMemberList().size() == 1)
        {
            campaign.getCampaignData().setReferencePlayerSerialNumber(activePlayers.getSquadronMemberList().get(0).getSerialNumber());
        }        
    }
}
