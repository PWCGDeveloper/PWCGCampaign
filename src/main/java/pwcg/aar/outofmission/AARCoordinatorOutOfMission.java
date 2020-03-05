package pwcg.aar.outofmission;

import pwcg.aar.data.AARContext;
import pwcg.aar.outofmission.phase1.elapsedtime.AAROutOfMissionEventCoordinator;
import pwcg.aar.outofmission.phase1.elapsedtime.ReconciledOutOfMissionData;
import pwcg.aar.outofmission.phase2.resupply.AARResupplyCoordinator;
import pwcg.aar.outofmission.phase2.resupply.AARResupplyData;
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
    	resupply();
    }

    private void outOfMissionEvents() throws PWCGException
    {
        AAROutOfMissionEventCoordinator outOfMissionCoordinator = new AAROutOfMissionEventCoordinator(campaign, aarContext);
        ReconciledOutOfMissionData reconciledOutOfMissionData = outOfMissionCoordinator.reconcileOutOfMission();
        aarContext.setReconciledOutMissionData(reconciledOutOfMissionData);
    }

    private void resupply() throws PWCGException
    {
        AARResupplyCoordinator transferCoordinator = new AARResupplyCoordinator(campaign, aarContext);
        AARResupplyData transferData = transferCoordinator.handleResupply();
        aarContext.getReconciledOutOfMissionData().setResupplyData(transferData);
    }
}
