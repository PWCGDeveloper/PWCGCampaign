package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.util.List;

import javafx.scene.layout.Pane;

import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.gui.campaign.pilot.CampaignHomePilotPanel;

public class CampaignHomeRightAcesPanel extends Pane
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

    public void makePanel(List<SquadronMember>sortedPilots) throws PWCGException
    {
        Pane pilotListPanel = makePilotPanel(sortedPilots);
        this.add(pilotListPanel, BorderLayout.NORTH);
    }

    private Pane makePilotPanel(List<SquadronMember>sortedPilots) throws PWCGException
    {
        CampaignHomePilotPanel pilotList = new CampaignHomePilotPanel(campaignHome);
        pilotList.makePanel(sortedPilots, "  Aces", "CampFlowPilot:");
        return pilotList;
    }
}
