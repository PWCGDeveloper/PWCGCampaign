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

public class CampaignHomeCenterPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private ChalkboardSelector chalkboardSelector;

	public CampaignHomeCenterPanel(ChalkboardSelector chalkboardSelector)  
	{
	    super();
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
	    this.chalkboardSelector = chalkboardSelector;
	}

    public void makePanel(List<CrewMember> sortedCrewMembers)  
    {
        try
        {            
            this.add(chalkboardSelector, BorderLayout.NORTH);
 
            JPanel chalkBoardPanel = createCrewMemberListPanel(sortedCrewMembers);
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
