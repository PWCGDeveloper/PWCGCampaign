package pwcg.gui.campaign.pilot;

import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;

public class CampaignAcePanel extends CampaignPilotPanelSet
{
    private static final long serialVersionUID = 1L;

    public CampaignAcePanel(Squadron squad, SquadronMember pilot)
	{
		super(squad, pilot);
		
		
		changePilotPictureAction = "Change Ace Picture";
		openMedalBoxAction = "Open Ace Medal Box:";
		openLogBookAction = "View Ace Log:";
	}
}
