package pwcg.gui.rofmap.event;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class CampaignReportLeaveGUI extends CampaignDocumentGUI
{
	private static final long serialVersionUID = 1L;
    private Campaign campaign;
	
	public CampaignReportLeaveGUI(Campaign campaign)
	{
		super();
        this.campaign = campaign;
		makePanel() ;
	}

    protected String getHeaderText() throws PWCGException
    {
        String leaveHeaderText = "Request for Leave \n\n";
        return leaveHeaderText;
    }

    protected String getBodyText() throws PWCGException
    {
        Squadron squadron =  PWCGContextManager.getInstance().getSquadronManager().getSquadron(campaign.getSquadronId());
                
        String leaveText = "Squadron: " + squadron.determineDisplayName(campaign.getDate()) + "\n";
        leaveText += "Date: " + campaign.getDate() + "\n";        
        return leaveText;
    }

    @Override
    public void finished()
    {
    }
}
