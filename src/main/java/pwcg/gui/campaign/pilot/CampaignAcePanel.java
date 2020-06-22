package pwcg.gui.campaign.pilot;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.gui.campaign.home.CampaignHome;

public class CampaignAcePanel extends CampaignPilotPanelSet
{
    private static final long serialVersionUID = 1L;

    public CampaignAcePanel(Campaign campaign, CampaignHome parent, Squadron squad, SquadronMember pilot)
	{
		super(campaign, squad, pilot, parent);
		
		
		changePilotPictureAction = "Change Ace Picture";
		openMedalBoxAction = "Open Ace Medal Box:";
		openLogBookAction = "View Ace Log:";
	}
}
