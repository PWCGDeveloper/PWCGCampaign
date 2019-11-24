package pwcg.coop;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.coop.model.CoopPersona;
import pwcg.core.exception.PWCGException;

class CoopPersonaRetirement
{
    public void retirePersona(CoopPersona personaToRemove) throws PWCGException
    {
        Campaign campaign = new Campaign();
        if (campaign.open(personaToRemove.getCampaignName()))              
        {
            PWCGContext.getInstance().setCampaign(campaign);
            if (campaign.getCampaignData().getCampaignMode() != CampaignMode.CAMPAIGN_MODE_SINGLE)
            {
                removePersonasFromCampaign(personaToRemove, campaign);
                campaign.write();
            }
        }
    }

    private void removePersonasFromCampaign(CoopPersona personaToRetire, Campaign campaign) throws PWCGException
    {
        SquadronMember squadronMemberToRetire = campaign.getPersonnelManager().getAnyCampaignMember(personaToRetire.getSerialNumber());
        if (squadronMemberToRetire != null)
        {
            squadronMemberToRetire.setPilotActiveStatus(SquadronMemberStatus.STATUS_RETIRED, campaign.getDate(), null);
        }
    }
}
