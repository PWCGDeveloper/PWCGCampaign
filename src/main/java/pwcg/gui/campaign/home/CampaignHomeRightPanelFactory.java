package pwcg.gui.campaign.home;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public class CampaignHomeRightPanelFactory
{
    public static JPanel makeCampaignHomeSquadronRightPanel(Campaign campaign, ActionListener actionListener, List<SquadronMember> squadronMembers, int squadronId) throws PWCGException
    {
        CampaignHomeRightSquadronPanel squadronPanel = new CampaignHomeRightSquadronPanel(campaign, actionListener);
        squadronPanel.makePanel(squadronMembers, squadronId);
        return squadronPanel;
    }
    
    public static JPanel makeCampaignHomePilotsRightPanel(Campaign campaign,ActionListener actionListener, List<SquadronMember> squadronMembers) throws PWCGException
    {
        CampaignHomeRightSquadronNoPlaquePanel squadronPanel = new CampaignHomeRightSquadronNoPlaquePanel(campaign, actionListener);
        squadronPanel.makePanel(squadronMembers);
        return squadronPanel;
    }
    
    public static JPanel makeCampaignHomeAcesRightPanel(Campaign campaign, CampaignHomeScreen campaignHome, List<SquadronMember> aces) throws PWCGException
    {
        CampaignHomeRightAcesPanel acesPanel = new CampaignHomeRightAcesPanel(campaign, campaignHome);
        acesPanel.makePanel(aces);
        return acesPanel;
    }
}
