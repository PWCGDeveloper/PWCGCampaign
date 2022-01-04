package pwcg.gui.campaign.crewmember;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.gui.campaign.home.CampaignHomeScreen;

public class CampaignAcePanel extends CampaignCrewMemberScreen
{
    private static final long serialVersionUID = 1L;

    public CampaignAcePanel(Campaign campaign, CampaignHomeScreen parent, Company company, CrewMember crewMember)
	{
		super(campaign, company, crewMember, parent);
		
		
		changeCrewMemberPictureAction = "Change Ace Picture";
		openMedalBoxAction = "Open Ace Medal Box:";
		openLogBookAction = "View Ace Log:";
	}
}
