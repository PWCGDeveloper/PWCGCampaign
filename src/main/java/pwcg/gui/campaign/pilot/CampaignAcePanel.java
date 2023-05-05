package pwcg.gui.campaign.pilot;

import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.gui.campaign.home.CampaignHomeScreen;

public class CampaignAcePanel extends CampaignPilotScreen
{
    private static final long serialVersionUID = 1L;

    public CampaignAcePanel(CampaignHomeScreen parent, Squadron squad, SquadronMember pilot)
	{
		super(squad, pilot, parent);
		
		
		changePilotPictureAction = "Change Ace Picture";
		openMedalBoxAction = "Open Ace Medal Box:";
		openLogBookAction = "View Ace Log:";
	}
}
