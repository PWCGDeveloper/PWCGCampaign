package pwcg.gui.campaign.home;

import java.util.List;

import javafx.scene.layout.Pane;

import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public class CampaignHomeCenterPanelFactory
{
    public static Pane makeCampaignHomeCenterPanel(CampaignHomeScreen campaignHome, List<SquadronMember> sortedPilots) throws PWCGException  
    {
        CampaignHomeCenterPanel chalkboard = new CampaignHomeCenterPanel(campaignHome.getChalkboardSelector());
        chalkboard.makePanel(sortedPilots);
        return chalkboard;
    }
}
