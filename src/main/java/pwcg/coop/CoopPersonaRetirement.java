package pwcg.coop;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;

class CoopPersonaRetirement
{
    public static void retirePersona(String campaignName, int personaToRemove) throws PWCGException
    {
        Campaign campaign = new Campaign(PWCGContext.getProduct());
        if (campaign.open(campaignName))              
        {
            if (campaign.getCampaignData().getCampaignMode() != CampaignMode.CAMPAIGN_MODE_SINGLE)
            {
                removePersonasFromCampaign(personaToRemove, campaign);
                campaign.write();
            }
        }
    }

    private static void removePersonasFromCampaign(int personaToRetire, Campaign campaign) throws PWCGException
    {
        SquadronMember squadronMemberToRetire = campaign.getPersonnelManager().getAnyCampaignMember(personaToRetire);
        if (squadronMemberToRetire != null)
        {
            squadronMemberToRetire.setPilotActiveStatus(SquadronMemberStatus.STATUS_RETIRED, campaign.getDate(), null);
        }
    }
}
