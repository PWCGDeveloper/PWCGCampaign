package pwcg.gui.rofmap.event;

import pwcg.aar.ui.events.model.TransferEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.PWCGContext;
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
        this.shouldDisplay = true;
        makePanel();
    }

    protected String getHeaderText() throws PWCGException
    {
        String transferHeaderText = "Notification of Transfer \n\n";
        return transferHeaderText;
    }

    protected String getBodyText() throws PWCGException
    {
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
        Company fromSquadron = squadronManager.getCompany(transferEvent.getTransferFrom());
        Company toSquadron = squadronManager.getCompany(transferEvent.getTransferTo());

        String transferMessage = transferEvent.getCrewMemberName() + " has been transferred" + "\n";
                        
        if (fromSquadron != null && toSquadron != null)
        {
            transferMessage = transferEvent.getCrewMemberName() + " has been transferred from\n" + 
                            fromSquadron.determineDisplayName(campaign.getDate()) + 
                            " to " + toSquadron.determineDisplayName(campaign.getDate())+ "\n";
        }
        else if (toSquadron != null)
        {
            transferMessage = transferEvent.getCrewMemberName() + 
                            " has been transferred to " + toSquadron.determineDisplayName(campaign.getDate()) + "\n";
        }
        else if (fromSquadron != null)
        {
            transferMessage = transferEvent.getCrewMemberName() + 
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
