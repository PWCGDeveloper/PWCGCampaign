package pwcg.gui.campaign.home;

import java.util.List;

import javax.swing.JPanel;

import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public class CampaignHomeCenterPanelFactory
{
    public static JPanel makeCampaignHomeCenterPanel(CampaignHome campaignHome, List<SquadronMember> sortedPilots) throws PWCGException  
    {
        CampaignHomeCenterPanel chalkboard = new CampaignHomeCenterPanel(campaignHome);
        chalkboard.makePanel(sortedPilots);
        return chalkboard;
    }
}
