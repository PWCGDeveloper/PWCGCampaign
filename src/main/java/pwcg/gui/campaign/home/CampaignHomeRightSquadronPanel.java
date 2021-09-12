package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.util.List;

import javafx.scene.layout.Pane;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.gui.campaign.pilot.CampaignHomePilotPanel;

public class CampaignHomeRightSquadronPanel extends Pane
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

    public void makePanel(List<SquadronMember>sortedPilots, int squadronId) throws PWCGException
    {
        Pane pilotListPanel = makePilotPanel(sortedPilots);
        this.add(pilotListPanel, BorderLayout.NORTH);

        Pane plaquePanel = makePlaquePanel(squadronId);
        this.add(plaquePanel, BorderLayout.SOUTH);
    }

    private Pane makePilotPanel(List<SquadronMember>sortedPilots) throws PWCGException
    {
        CampaignHomePilotPanel pilotList = new CampaignHomePilotPanel(campaignHome);
        pilotList.makePanel(sortedPilots, "  Roster", "CampFlowPilot:");
        return pilotList;
    }

    private Pane makePlaquePanel(int squadronId) throws PWCGException 
    {
        CampaignHomeSquadronPlaque squadronPlaque = new CampaignHomeSquadronPlaque(campaign);
        squadronPlaque.makeDescPanel(squadronId);
        return squadronPlaque;
    }
}
