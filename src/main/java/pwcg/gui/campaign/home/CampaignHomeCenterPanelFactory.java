package pwcg.gui.campaign.home;

import java.util.List;

import javax.swing.JPanel;

import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;

public class CampaignHomeCenterPanelFactory
{
    public static JPanel makeCampaignHomeCenterPanel(CampaignHomeScreen campaignHome, List<CrewMember> sortedCrewMembers) throws PWCGException  
    {
        CampaignHomeCenterPanel chalkboard = new CampaignHomeCenterPanel(campaignHome.getChalkboardSelector());
        chalkboard.makePanel(sortedCrewMembers);
        return chalkboard;
    }
}
