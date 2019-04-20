package pwcg.gui.rofmap.event;

import pwcg.aar.ui.events.model.AceLeaveEvent;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class CampaignReportAceLeaveGUI extends CampaignDocumentGUI
{
    private static final long serialVersionUID = 1L;
	private AceLeaveEvent aceLeaveEvent = null;

	public CampaignReportAceLeaveGUI(AceLeaveEvent aceLeaveEvent, Campaign campaign)
	{
        super();

        this.aceLeaveEvent = aceLeaveEvent;

        makePanel();        
	}

    protected String getHeaderText() throws PWCGException
    {
        String transferHeaderText = "Notification of Leave \n\n";
        return transferHeaderText;
    }

    protected String getBodyText() throws PWCGException
    {
        String transferMessage = "Squadron: " + aceLeaveEvent.getSquadron() + "\n";
        transferMessage += "Date: " + DateUtils.getDateStringPretty(aceLeaveEvent.getDate()) + "\n";
        transferMessage += aceLeaveEvent.getPilotName() + 
                        " has been granted leave from " + aceLeaveEvent.getSquadron()  + "\n";


        return transferMessage;
    }

    @Override
    public void finished()
    {
    }
}
