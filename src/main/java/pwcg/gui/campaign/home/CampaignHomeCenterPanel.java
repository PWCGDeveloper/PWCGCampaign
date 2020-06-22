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
	
	private CampaignHome parent;
	
	public CampaignHomeCenterPanel(CampaignHome parent)  
	{
	    super();
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
	    this.parent = parent;
	}

    public void makePanel(List<SquadronMember> sortedPilots)  
    {
        try
        {
            this.setLayout(new BorderLayout()); 
            
            JPanel selectorPanel = createSelectorPanel();
            this.add(selectorPanel, BorderLayout.NORTH);
 
            JPanel chalkBoardPanel = createPilotListPanel(sortedPilots);
            this.add(chalkBoardPanel, BorderLayout.CENTER);
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private JPanel createSelectorPanel() throws PWCGException
    {
        ChalkboardSelector chalkboardSelector = new ChalkboardSelector(parent);
        chalkboardSelector.createSelectorPanel();
        return chalkboardSelector;
    }

    private JPanel createPilotListPanel(List<SquadronMember> sortedPilots) throws PWCGException
    {
        PilotChalkboardBuilder pilotChalkboardBuilder = new PilotChalkboardBuilder();
        return pilotChalkboardBuilder.createPilotListPanel(sortedPilots);
    }
}
