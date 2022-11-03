package pwcg.gui.rofmap.event;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class CampaignReportLeaveGUI extends AARDocumentIconPanel
{
	private static final long serialVersionUID = 1L;
    private Campaign campaign;

	public CampaignReportLeaveGUI(Campaign campaign) throws PWCGException
	{
		super(campaign);
        this.campaign = campaign;
        this.shouldDisplay = true;
		makePanel() ;
	}

    protected String getHeaderText() throws PWCGException
    {
        String leaveHeaderText = "Request for Leave \n\n";
        return leaveHeaderText;
    }

    protected String getBodyText() throws PWCGException
    {
        Squadron squadron =  PWCGContext.getInstance().getSquadronManager().getSquadron(campaign.findReferencePlayer().getSquadronId());
                
        String leaveText = "Squadron: " + squadron.determineDisplayName(campaign.getDate()) + "\n";
        leaveText += "Date: " + campaign.getDate() + "\n";        
        return leaveText;
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
