package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;

import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.dialogs.ErrorDialog;

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

    public void makePanel(List<SquadronMember> sortedPilots)  
    {
        try
        {            
            this.add(chalkboardSelector, BorderLayout.NORTH);
 
            JPanel chalkBoardPanel = createPilotListPanel(sortedPilots);
            this.add(chalkBoardPanel, BorderLayout.CENTER);
            this.add(CampaignHomeBottomSpacerBuilder.createLowerSpacerPanel(), BorderLayout.SOUTH);
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
