package pwcg.aar.inmission;

import java.util.Map;

import pwcg.aar.awards.CampaignMemberAwardsGeneratorInMission;
import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARPersonnelAwards;
import pwcg.aar.inmission.phase1.parse.AARLogEvaluationCoordinator;
import pwcg.aar.inmission.phase2.logeval.AARMissionEvaluationData;
import pwcg.aar.inmission.phase2.logeval.AARPhase2EvaluateCoordinator;
import pwcg.aar.inmission.phase3.reconcile.AARPhase3ReconcileCoordinator;
import pwcg.aar.inmission.phase3.reconcile.ReconciledInMissionData;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.PlayerDeclarations;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class AARCoordinatorInMission
{
    private Campaign campaign;
    private AARContext aarContext;

    public AARCoordinatorInMission (Campaign campaign, AARContext aarContext, AARLogEvaluationCoordinator logEvaluationCoordinator)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }
    
    public void coordinateInMissionAAR(Map<Integer, PlayerDeclarations> playerDeclarations) throws PWCGException
    {        
        phase2EvaluateLogEventToCampaignEvents();  
        phase3ResolveMissionResultsAndPlayerClaims(playerDeclarations);
        awardsInMission();
    }

    private void phase2EvaluateLogEventToCampaignEvents() throws PWCGException
    {
        AARPhase2EvaluateCoordinator phase2Coordinator =  new AARPhase2EvaluateCoordinator(campaign, aarContext);
        AARMissionEvaluationData missionEvaluationData = phase2Coordinator.evaluateLogEvents();
        aarContext.setMissionEvaluationData(missionEvaluationData);
    }

    private void phase3ResolveMissionResultsAndPlayerClaims(Map<Integer, PlayerDeclarations> playerDeclarations) throws PWCGException
    {
        AARPhase3ReconcileCoordinator phase3Coordinator = new AARPhase3ReconcileCoordinator(campaign, aarContext);
        ReconciledInMissionData reconciledInMissionData = phase3Coordinator.reconcileLogsWithAAR(playerDeclarations);
        aarContext.setReconciledInMissionData(reconciledInMissionData);
    }

    private void awardsInMission() throws PWCGException
    {
        CampaignMemberAwardsGeneratorInMission awardsGeneratorInMission = new CampaignMemberAwardsGeneratorInMission(campaign, aarContext);
        AARPersonnelAwards personnelAwards = awardsGeneratorInMission.createCampaignMemberAwards();
        aarContext.getReconciledInMissionData().setPersonnelAwards(personnelAwards);
    }
}
