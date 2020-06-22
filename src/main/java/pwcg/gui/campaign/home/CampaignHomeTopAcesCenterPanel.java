package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;

import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.dialogs.ErrorDialog;

public class CampaignHomeTopAcesCenterPanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    public static final Integer ACE_VICTORY_SORT_CONSTANT = 100000;
    
    private CampaignHome parent;

	public CampaignHomeTopAcesCenterPanel(CampaignHome parent) throws PWCGException  
	{
        super();
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        
        this.parent = parent;
	}

    public void makePanel(List<SquadronMember> acesToDisplay)  
    {
        try
        {
            JPanel selectorPanel = createSelectorPanel();
            this.add(selectorPanel, BorderLayout.NORTH);
 
            JPanel chalkBoardPanel = createPilotListPanel(acesToDisplay);
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
