package pwcg.gui.campaign.home;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;

public class CampaignHomeRightPanelFactory
{
    public static JPanel makeCampaignHomeSquadronRightPanel(Campaign campaign, ActionListener actionListener, List<CrewMember> squadronMembers, int squadronId) throws PWCGException
    {
        CampaignHomeRightSquadronPanel squadronPanel = new CampaignHomeRightSquadronPanel(campaign, actionListener);
        squadronPanel.makePanel(squadronMembers, squadronId);
        return squadronPanel;
    }
    
    public static JPanel makeCampaignHomeCrewMembersRightPanel(ActionListener actionListener, List<CrewMember> squadronMembers) throws PWCGException
    {
        CampaignHomeRightSquadronNoPlaquePanel squadronPanel = new CampaignHomeRightSquadronNoPlaquePanel(actionListener);
        squadronPanel.makePanel(squadronMembers);
        return squadronPanel;
    }
    
    public static JPanel makeCampaignHomeAcesRightPanel(CampaignHomeScreen campaignHome, List<CrewMember> aces) throws PWCGException
    {
        CampaignHomeRightAcesPanel acesPanel = new CampaignHomeRightAcesPanel(campaignHome);
        acesPanel.makePanel(aces);
        return acesPanel;
    }
}
