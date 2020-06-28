package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;

import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.PwcgBorderFactory;
import pwcg.gui.utils.SpacerPanelFactory;

public class CampaignHomeTopAcesCenterPanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    public static final Integer ACE_VICTORY_SORT_CONSTANT = 100000;
    
    private CampaignHome campaignHome;

	public CampaignHomeTopAcesCenterPanel(CampaignHome campaignHome) throws PWCGException  
	{
        super();
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        
        this.campaignHome = campaignHome;
	}

    public void makePanel(List<SquadronMember> acesToDisplay)  
    {
        try
        {
            this.add(campaignHome.getChalkboardSelector(), BorderLayout.NORTH);
 
            JPanel chalkBoardPanel = createPilotListPanel(acesToDisplay);
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

    private JPanel createPilotListPanel(List<SquadronMember> sortedPilots) throws PWCGException
    {
        PilotChalkboardBuilder pilotChalkboardBuilder = new PilotChalkboardBuilder();
        return pilotChalkboardBuilder.createPilotListPanel(sortedPilots);
    }
}
