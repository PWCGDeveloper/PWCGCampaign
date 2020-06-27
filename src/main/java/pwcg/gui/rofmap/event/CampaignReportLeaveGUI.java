package pwcg.gui.rofmap.event;

import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
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
    public boolean isShouldDisplay()
    {
        return shouldDisplay;
    }

    @Override
    public JPanel getPanel()
    {
        return this;
    }
}
