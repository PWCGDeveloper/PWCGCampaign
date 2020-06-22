package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.gui.campaign.pilot.CampaignHomePilotPanel;

public class CampaignHomeRightSquadronPanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    private CampaignHome campaignHome;
    private Campaign campaign;
    
    public CampaignHomeRightSquadronPanel(CampaignHome campaignHome)  
    {
        super();
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        
        this.campaign = campaignHome.getCampaign();
        this.campaignHome = campaignHome;
    }

    public void makePanel(List<SquadronMember>sortedPilots, int squadronId) throws PWCGException
    {
        JPanel pilotListPanel = makePilotPanel(sortedPilots);
        this.add(pilotListPanel, BorderLayout.NORTH);

        JPanel plaquePanel = makePlaquePanel(squadronId);
        this.add(plaquePanel, BorderLayout.SOUTH);
    }

    private JPanel makePilotPanel(List<SquadronMember>sortedPilots) throws PWCGException
    {
        CampaignHomePilotPanel pilotList = new CampaignHomePilotPanel(campaignHome);
        pilotList.makePanel(sortedPilots, "  Roster", "CampFlowPilot:");
        return pilotList;
    }

    private JPanel makePlaquePanel(int squadronId) throws PWCGException 
    {
        CampaignHomeSquadronPlaque squadronPlaque = new CampaignHomeSquadronPlaque(campaign);
        squadronPlaque.makeDescPanel(squadronId);
        return squadronPlaque;
    }

}
