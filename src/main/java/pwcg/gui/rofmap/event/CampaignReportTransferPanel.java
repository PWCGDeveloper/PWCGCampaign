package pwcg.gui.rofmap.event;

import pwcg.aar.ui.events.model.TransferEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.squadron.SquadronManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class CampaignReportTransferPanel extends AARDocumentIconPanel
{
    private Campaign campaign;
    private static final long serialVersionUID = 1L;
    private TransferEvent transferEvent;

    public CampaignReportTransferPanel(Campaign campaign, TransferEvent transferEvent) throws PWCGException
    {
        super();

        this.campaign = campaign;        
        this.transferEvent = transferEvent;        
        makePanel();
    }

    protected String getHeaderText() throws PWCGException
    {
        String transferHeaderText = "Notification of Transfer \n\n";
        return transferHeaderText;
    }

    protected String getBodyText() throws PWCGException
    {
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        Squadron fromSquadron = squadronManager.getSquadron(transferEvent.getTransferFrom());
        Squadron toSquadron = squadronManager.getSquadron(transferEvent.getTransferTo());

        String transferMessage = transferEvent.getPilotName() + " has been transferred" + "\n";
                        
        if (fromSquadron != null && toSquadron != null)
        {
            transferMessage = transferEvent.getPilotName() + " has been transferred from\n" + 
                            fromSquadron.determineDisplayName(campaign.getDate()) + 
                            " to " + toSquadron.determineDisplayName(campaign.getDate())+ "\n";
        }
        else if (toSquadron != null)
        {
            transferMessage = transferEvent.getPilotName() + 
                            " has been transferred to " + toSquadron.determineDisplayName(campaign.getDate()) + "\n";
        }
        else if (fromSquadron != null)
        {
            transferMessage = transferEvent.getPilotName() + 
                            " has been transferred from " + fromSquadron.determineDisplayName(campaign.getDate()) + "\n";
        }
        
        transferMessage += "Date: " + DateUtils.getDateStringPretty(transferEvent.getDate())+ "\n";


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
