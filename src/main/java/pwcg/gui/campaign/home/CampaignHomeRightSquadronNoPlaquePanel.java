package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JPanel;

import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;
import pwcg.gui.campaign.crewmember.CampaignHomeCrewMemberPanel;

public class CampaignHomeRightSquadronNoPlaquePanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    private ActionListener campaignHome;
    
    public CampaignHomeRightSquadronNoPlaquePanel(ActionListener campaignHome)  
    {
        super();
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        
        this.campaignHome = campaignHome;
    }

    public void makePanel(List<CrewMember>sortedCrewMembers) throws PWCGException
    {
        JPanel crewMemberListPanel = makeCrewMemberPanel(sortedCrewMembers);
        this.add(crewMemberListPanel, BorderLayout.NORTH);
    }

    private JPanel makeCrewMemberPanel(List<CrewMember>sortedCrewMembers) throws PWCGException
    {
        CampaignHomeCrewMemberPanel crewMemberList = new CampaignHomeCrewMemberPanel(campaignHome);
        crewMemberList.makePanel(sortedCrewMembers, "  Roster", "CampFlowCrewMember:");
        return crewMemberList;
    }
}
