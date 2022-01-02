package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;

import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;
import pwcg.gui.campaign.crewmember.CampaignHomeCrewMemberPanel;

public class CampaignHomeRightAcesPanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    private CampaignHomeScreen campaignHome;
    
    public CampaignHomeRightAcesPanel(CampaignHomeScreen campaignHome)  
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
        crewMemberList.makePanel(sortedCrewMembers, "  Aces", "CampFlowCrewMember:");
        return crewMemberList;
    }
}
