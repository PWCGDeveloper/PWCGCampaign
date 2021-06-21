package pwcg.aar.inmission;

import java.util.Map;

import pwcg.aar.CampaignModeAARFactory;
import pwcg.aar.data.AARContext;
import pwcg.aar.data.AAREquipmentLosses;
import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.inmission.phase2.logeval.AARMissionEvaluationData;
import pwcg.aar.inmission.phase2.logeval.AARPhase2EvaluateCoordinator;
import pwcg.aar.inmission.phase3.reconcile.equipment.EquipmentResultsInMissionHandler;
import pwcg.aar.inmission.phase3.reconcile.personnel.PersonnelLossesInMissionHandler;
import pwcg.aar.inmission.phase3.reconcile.victories.IClaimResolver;
import pwcg.aar.inmission.phase3.reconcile.victories.ReconciledMissionVictoryData;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.PlayerDeclarations;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class AARCoordinatorInMission
{
    private Campaign campaign;
    private AARContext aarContext;

    public AARCoordinatorInMission (Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }
    
    public void coordinateInMissionAAR(Map<Integer, PlayerDeclarations> playerDeclarations) throws PWCGException
    {        
        evaluateMissionEvents();  
        reconcileVictories(playerDeclarations);
        personnelChangesInMission();
        equipmentChangesInMission();
    }

    private void evaluateMissionEvents() throws PWCGException
    {
        AARPhase2EvaluateCoordinator phase2Coordinator =  new AARPhase2EvaluateCoordinator(campaign, aarContext);
        AARMissionEvaluationData missionEvaluationData = phase2Coordinator.evaluateLogEvents();
        aarContext.setMissionEvaluationData(missionEvaluationData);
    }

    private void reconcileVictories(Map<Integer, PlayerDeclarations> playerDeclarations) throws PWCGException
    {
        IClaimResolver missionResolver = CampaignModeAARFactory.createClaimResolver(campaign, aarContext, playerDeclarations);
        ReconciledMissionVictoryData reconciledMissionVictoryData = missionResolver.resolvePlayerClaims();
        aarContext.setReconciledMissionVictoryData(reconciledMissionVictoryData);
    }

    private void personnelChangesInMission() throws PWCGException 
    {
        PersonnelLossesInMissionHandler personnelHandler = new PersonnelLossesInMissionHandler(campaign, aarContext.getMissionEvaluationData());
        AARPersonnelLosses personnelResultsInMission = personnelHandler.personellChanges();
        aarContext.addPersonnelLosses(personnelResultsInMission);
    }

    private void equipmentChangesInMission() throws PWCGException 
    {
        EquipmentResultsInMissionHandler equipmentHandler = new EquipmentResultsInMissionHandler(aarContext.getMissionEvaluationData());
        AAREquipmentLosses equipmentResultsInMission = equipmentHandler.equipmentChanges();
        aarContext.addEquipmentLossesInMission(equipmentResultsInMission);
    }
}
