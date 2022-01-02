package pwcg.gui.rofmap.event;

import pwcg.aar.ui.events.model.AceLeaveEvent;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class CampaignReportAceLeaveGUI extends AARDocumentIconPanel
{
    private static final long serialVersionUID = 1L;
	private AceLeaveEvent aceLeaveEvent = null;

	public CampaignReportAceLeaveGUI(AceLeaveEvent aceLeaveEvent, Campaign campaign) throws PWCGException
	{
        super();

        this.aceLeaveEvent = aceLeaveEvent;
        makePanel();
	}

	@Override
    public void makePanel() throws PWCGException
    {        
    }

    protected String getHeaderText() throws PWCGException
    {
        String transferHeaderText = "Notification of Leave \n\n";
        return transferHeaderText;
    }

    protected String getBodyText() throws PWCGException
    {
        String transferMessage = "Squadron: " + aceLeaveEvent.getSquadronName() + "\n";
        transferMessage += "Date: " + DateUtils.getDateStringPretty(aceLeaveEvent.getDate()) + "\n";
        transferMessage += aceLeaveEvent.getCrewMemberName() + 
                        " has been granted leave from " + aceLeaveEvent.getSquadronName()  + "\n";


        return transferMessage;
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
