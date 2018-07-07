package pwcg.aar.tabulate.debrief;

import pwcg.aar.data.AARContext;
import pwcg.aar.outofmission.phase2.resupply.TransferRecord;
import pwcg.aar.ui.display.model.AARAceLeavePanelData;
import pwcg.aar.ui.events.model.AceLeaveEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMemberStatus;
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
        for (TransferRecord aceTransferRecord : aarContext.getReconciledOutOfMissionData().getResupplyData().getAcesTransferred().getSquadronMembersTransferredFromSquadron(campaign.getSquadronId()))
        {
            if (aceTransferRecord.getTransferTo() == SquadronMemberStatus.STATUS_ON_LEAVE)
            {
                AceLeaveEvent aceLeaveEvent = makeLeaveEvent(aceTransferRecord);
                aceLeavePanelData.addAceOnLeaveDuringElapsedTime(aceLeaveEvent);
            }
        }
        
        return aceLeavePanelData;
    }
    
    private AceLeaveEvent makeLeaveEvent(TransferRecord aceTransferRecord) throws PWCGException
    {
        AceLeaveEvent aceLeaveEvent = new AceLeaveEvent();
        aceLeaveEvent.setPilot(aceTransferRecord.getSquadronMember());
        aceLeaveEvent.setDate(campaign.getDate());
        aceLeaveEvent.setSquadron(SquadronMemberStatus.ON_LEAVE_STATUS);
        
        return aceLeaveEvent;
    }
}
