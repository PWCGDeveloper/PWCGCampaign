package pwcg.gui.rofmap.event;

import pwcg.aar.ui.events.model.SquadronMoveEvent;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class CampaignReportSquadronMoveGUI extends CampaignDocumentGUI
{
	private static final long serialVersionUID = 1L;
	private SquadronMoveEvent squadronMoveEvent = null;
	private Campaign campaign;

	public CampaignReportSquadronMoveGUI(SquadronMoveEvent squadronMoveEvent, Campaign campaign)
	{
		super();

        this.campaign = campaign;
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
        String squadronMoveText = "Squadron: " + squadronMoveEvent.getSquadron().determineDisplayName(campaign.getDate()) + "\n";
        squadronMoveText += "Date: " + DateUtils.getDateStringPretty(squadronMoveEvent.getDate()) + "\n";
        squadronMoveText += squadronMoveEvent.getSquadron().determineDisplayName(squadronMoveEvent.getDate()) + 
                        " has been moved to " + squadronMoveEvent.getNewAirfield().getName() + ".\n";   
        
        
        return squadronMoveText;
	}


    @Override
    public void finished()
    {
    }
}
