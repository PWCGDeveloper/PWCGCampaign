package pwcg.gui.campaign.home;

import java.util.List;

import javax.swing.JPanel;

import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public class CampaignHomeRightPanelFactory
{
    public static JPanel makeCampaignHomeSquadronRightPanel(CampaignHome campaignHome, List<SquadronMember> squadronMembers, int squadronId) throws PWCGException
    {
        CampaignHomeRightSquadronPanel squadronPanel = new CampaignHomeRightSquadronPanel(campaignHome);
        squadronPanel.makePanel(squadronMembers, squadronId);
        return squadronPanel;
    }
    
    public static JPanel makeCampaignHomeAcesRightPanel(CampaignHome campaignHome, List<SquadronMember> aces) throws PWCGException
    {
        CampaignHomeRightAcesPanel acesPanel = new CampaignHomeRightAcesPanel(campaignHome);
        acesPanel.makePanel(aces);
        return acesPanel;
    }
}
