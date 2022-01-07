package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;
import pwcg.gui.campaign.crewmember.CampaignHomeCrewMemberPanel;

public class CampaignHomeRightSquadronPanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    private ActionListener campaignHome;
    private Campaign campaign;
    
    public CampaignHomeRightSquadronPanel(Campaign campaign, ActionListener campaignHome)  
    {
        super();
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        
        this.campaign = campaign;
        this.campaignHome = campaignHome;
    }

    public void makePanel(List<CrewMember>sortedCrewMembers, int squadronId) throws PWCGException
    {
        JPanel crewMemberListPanel = makeCrewMemberPanel(sortedCrewMembers);
        this.add(crewMemberListPanel, BorderLayout.NORTH);

        JPanel plaquePanel = makePlaquePanel(squadronId);
        this.add(plaquePanel, BorderLayout.SOUTH);
    }

    private JPanel makeCrewMemberPanel(List<CrewMember>sortedCrewMembers) throws PWCGException
    {
        CampaignHomeCrewMemberPanel crewMemberList = new CampaignHomeCrewMemberPanel(campaignHome);
        crewMemberList.makePanel(sortedCrewMembers, "  Roster", "CampFlowCrewMember:");
        return crewMemberList;
    }

    private JPanel makePlaquePanel(int squadronId) throws PWCGException 
    {
        CampaignHomeCompanyPlaque squadronPlaque = new CampaignHomeCompanyPlaque(campaign);
        squadronPlaque.makeDescPanel(squadronId);
        return squadronPlaque;
    }
}
