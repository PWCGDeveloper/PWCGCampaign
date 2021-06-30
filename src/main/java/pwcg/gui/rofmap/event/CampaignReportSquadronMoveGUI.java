package pwcg.gui.rofmap.event;

import javax.swing.JPanel;

import pwcg.aar.ui.events.model.SquadronMoveEvent;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class CampaignReportSquadronMoveGUI extends AARDocumentIconPanel
{
	private static final long serialVersionUID = 1L;
	private SquadronMoveEvent squadronMoveEvent = null;

	public CampaignReportSquadronMoveGUI(SquadronMoveEvent squadronMoveEvent) throws PWCGException
	{
		super();

        this.squadronMoveEvent = squadronMoveEvent;
		makePanel();		
	}

    protected String getHeaderText() throws PWCGException
    {
        String transferHeaderText = "Notification of Squadron Relocation \n\n";
        return transferHeaderText;
    }

    protected String getBodyText() throws PWCGException
    {
        String squadronMoveText = "Squadron: " + squadronMoveEvent.getSquadronName() + "\n";
        squadronMoveText += "Date: " + DateUtils.getDateStringPretty(squadronMoveEvent.getDate()) + "\n";
        squadronMoveText += squadronMoveEvent.getSquadronName() + 
                        " has been moved to " + squadronMoveEvent.getNewAirfield() + ".\n";   
        
        
        return squadronMoveText;
	}

    @Override
    public void finished()
    {
    }

    @Override
    protected String getFooterImagePath() throws PWCGException
    {
        return "";
    }
}
