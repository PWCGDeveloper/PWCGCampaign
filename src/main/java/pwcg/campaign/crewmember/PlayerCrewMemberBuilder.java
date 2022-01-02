package pwcg.campaign.crewmember;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CampaignModeFactory;
import pwcg.coop.CoopUserManager;
import pwcg.coop.model.CoopUser;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;

public class PlayerCrewMemberBuilder
{
    private Campaign campaign;
    
    public PlayerCrewMemberBuilder(Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public void buildPlayerCrewMember(String playerName, String squadronName, String rank, String coopUser) throws PWCGUserException, Exception, PWCGException
    {
        ICrewMemberReplacer squadronMemberReplacer = CampaignModeFactory.makeCrewMemberReplacer(campaign);
        CrewMember newCrewMember = squadronMemberReplacer.createPersona(playerName, rank, squadronName, coopUser);
        
        persistCampaignAndCoopUser(coopUser, newCrewMember);
        setReferenceCrewMemberIfOnlyActivePlayerCrewMember();
    }

    private void persistCampaignAndCoopUser(String coopUser, CrewMember newCrewMember) throws PWCGException
    {
        campaign.write();        
        campaign.open(campaign.getCampaignData().getName());
        PWCGContext.getInstance().setCampaign(campaign);
        if (campaign.getCampaignData().getCampaignMode() != CampaignMode.CAMPAIGN_MODE_SINGLE)
        {
            createCoopUserAndPersona(campaign, newCrewMember, coopUser);
        }
    }

    private void createCoopUserAndPersona(Campaign campaign, CrewMember player, String coopUsername) throws PWCGException
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

    private void setReferenceCrewMemberIfOnlyActivePlayerCrewMember() throws PWCGException
    {
        CrewMembers activePlayers = campaign.getPersonnelManager().getAllActivePlayers();
        if (activePlayers.getCrewMemberList().size() == 1)
        {
            campaign.getCampaignData().setReferencePlayerSerialNumber(activePlayers.getCrewMemberList().get(0).getSerialNumber());
        }        
    }
}
