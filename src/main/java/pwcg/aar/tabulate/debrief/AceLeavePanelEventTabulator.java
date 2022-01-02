package pwcg.aar.tabulate.debrief;

import pwcg.aar.data.AARContext;
import pwcg.aar.ui.display.model.AARAceLeavePanelData;
import pwcg.aar.ui.events.model.AceLeaveEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.resupply.personnel.TransferRecord;
import pwcg.core.exception.PWCGException;

public class AceLeavePanelEventTabulator 
{
    private Campaign campaign;
    private AARContext aarContext;

    private AARAceLeavePanelData aceLeavePanelData = new AARAceLeavePanelData();

    public AceLeavePanelEventTabulator (Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }
        
    public AARAceLeavePanelData tabulateForAARAceLeavePanel() throws PWCGException
    {
        for (TransferRecord aceTransferRecord : aarContext.getResupplyData().getAcesTransferred().
                        getCrewMembersTransferred())
        {
            if (aceTransferRecord.getTransferTo() == CrewMemberStatus.STATUS_ON_LEAVE)
            {
                AceLeaveEvent aceLeaveEvent = makeLeaveEvent(aceTransferRecord);
                aceLeavePanelData.addAceOnLeaveDuringElapsedTime(aceLeaveEvent);
            }
        }
        
        return aceLeavePanelData;
    }
    
    private AceLeaveEvent makeLeaveEvent(TransferRecord aceTransferRecord) throws PWCGException
    {
        int aceSerialNumber = aceTransferRecord.getCrewMember().getSerialNumber();
        int aceSquadronId = aceTransferRecord.getCrewMember().getCompanyId();
        boolean isNewsworthy = false;
        AceLeaveEvent aceLeaveEvent = new AceLeaveEvent(campaign, CrewMemberStatus.ON_LEAVE_STATUS, aceSquadronId, aceSerialNumber, campaign.getDate(), isNewsworthy);

        return aceLeaveEvent;
    }
}
