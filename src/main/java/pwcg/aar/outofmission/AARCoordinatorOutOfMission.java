package pwcg.aar.outofmission;

import pwcg.aar.data.AARContext;
import pwcg.aar.outofmission.phase1.elapsedtime.AAROutOfMissionPhase1EventCoordinator;
import pwcg.aar.outofmission.phase1.elapsedtime.ReconciledOutOfMissionData;
import pwcg.aar.outofmission.phase2.transfer.AARPhaseOutOfMissionPhase2TransferCoordinator;
import pwcg.aar.outofmission.phase2.transfer.AARTransferData;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class AARCoordinatorOutOfMission
{
    private Campaign campaign;
    private AARContext aarContext;

    public AARCoordinatorOutOfMission(Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }

    public void coordinateOutOfMissionAAR() throws PWCGException
    {
    	outOfMissionEvents();  
    	transfers();
    }

    private void outOfMissionEvents() throws PWCGException
    {
        AAROutOfMissionPhase1EventCoordinator outOfMissionCoordinator = new AAROutOfMissionPhase1EventCoordinator(campaign, aarContext);
        ReconciledOutOfMissionData reconciledOutOfMissionData = outOfMissionCoordinator.reconcileOutOfMission();
        aarContext.setReconciledOutMissionData(reconciledOutOfMissionData);
    }

    private void transfers() throws PWCGException
    {
        AARPhaseOutOfMissionPhase2TransferCoordinator transferCoordinator = new AARPhaseOutOfMissionPhase2TransferCoordinator(campaign, aarContext);
        AARTransferData transferData = transferCoordinator.handleTransfers();
        aarContext.getReconciledOutOfMissionData().setTransferData(transferData);
    }
}
