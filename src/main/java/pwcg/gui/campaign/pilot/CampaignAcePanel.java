package pwcg.gui.campaign.pilot;

import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.gui.campaign.home.CampaignHomeGUI;

public class CampaignAcePanel extends CampaignPilotPanelSet
{
    private static final long serialVersionUID = 1L;

    public CampaignAcePanel(CampaignHomeGUI parent, Squadron squad, SquadronMember pilot)
	{
		super(squad, pilot, parent);
		
		
		changePilotPictureAction = "Change Ace Picture";
		openMedalBoxAction = "Open Ace Medal Box:";
		openLogBookAction = "View Ace Log:";
	}
}
