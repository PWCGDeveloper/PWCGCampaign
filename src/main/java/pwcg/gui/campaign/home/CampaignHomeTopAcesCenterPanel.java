package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;

import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.PwcgBorderFactory;
import pwcg.gui.utils.SpacerPanelFactory;

public class CampaignHomeTopAcesCenterPanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    public static final Integer ACE_VICTORY_SORT_CONSTANT = 100000;
    
    private CampaignHomeScreen campaignHome;

	public CampaignHomeTopAcesCenterPanel(CampaignHomeScreen campaignHome) throws PWCGException  
	{
        super();
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        
        this.campaignHome = campaignHome;
	}

    public void makePanel(List<CrewMember> acesToDisplay)  
    {
        try
        {
            this.add(campaignHome.getChalkboardSelector(), BorderLayout.NORTH);
 
            JPanel chalkBoardPanel = createCrewMemberListPanel(acesToDisplay);
            this.add(chalkBoardPanel, BorderLayout.CENTER);
            this.setBorder(PwcgBorderFactory.createCampaignHomeChalkboardBoxBorder());
            
            this.add(SpacerPanelFactory.createVerticalSpacerPanel(5), BorderLayout.SOUTH);
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private JPanel createCrewMemberListPanel(List<CrewMember> sortedCrewMembers) throws PWCGException
    {
        CampaignCrewMemberChalkboard crewMemberChalkboardBuilder = new CampaignCrewMemberChalkboard();
        crewMemberChalkboardBuilder.makePanels(sortedCrewMembers);
        return crewMemberChalkboardBuilder;
    }
}
