package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.gui.campaign.pilot.CampaignHomePilotPanel;

public class CampaignHomeRightSquadronNoPlaquePanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    private Campaign campaign;
    private ActionListener campaignHome;
    
    public CampaignHomeRightSquadronNoPlaquePanel(Campaign campaign, ActionListener campaignHome)  
    {
        super();
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        
        this.campaign = campaign;
        this.campaignHome = campaignHome;
    }

    public void makePanel(List<SquadronMember>sortedPilots) throws PWCGException
    {
        JPanel pilotListPanel = makePilotPanel(sortedPilots);
        this.add(pilotListPanel, BorderLayout.NORTH);
    }

    private JPanel makePilotPanel(List<SquadronMember>sortedPilots) throws PWCGException
    {
        CampaignHomePilotPanel pilotList = new CampaignHomePilotPanel(campaign, campaignHome);
        pilotList.makePanel(sortedPilots, "  Roster", "CampFlowPilot:");
        return pilotList;
    }
}
