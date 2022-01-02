package pwcg.coop;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.core.exception.PWCGException;

class CoopPersonaRetirement
{
    public static void retirePersona(String campaignName, int personaToRemove) throws PWCGException
    {
        Campaign campaign = new Campaign();
        if (campaign.open(campaignName))              
        {
            PWCGContext.getInstance().setCampaign(campaign);
            if (campaign.getCampaignData().getCampaignMode() != CampaignMode.CAMPAIGN_MODE_SINGLE)
            {
                removePersonasFromCampaign(personaToRemove, campaign);
                campaign.write();
            }
        }
    }

    private static void removePersonasFromCampaign(int personaToRetire, Campaign campaign) throws PWCGException
    {
        CrewMember squadronMemberToRetire = campaign.getPersonnelManager().getAnyCampaignMember(personaToRetire);
        if (squadronMemberToRetire != null)
        {
            squadronMemberToRetire.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_RETIRED, campaign.getDate(), null);
        }
    }
}
