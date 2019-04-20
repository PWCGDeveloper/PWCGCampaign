package pwcg.aar.inmission.phase3.reconcile;

import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AAREquipmentLosses;
import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.inmission.phase3.reconcile.equipment.EquipmentResultsInMissionHandler;
import pwcg.aar.inmission.phase3.reconcile .personnel.PersonnelResultsInMissionHandler;
import pwcg.aar.inmission.phase3.reconcile.victories.ClaimResolverFactory;
import pwcg.aar.inmission.phase3.reconcile.victories.IClaimResolver;
import pwcg.aar.inmission.phase3.reconcile.victories.ReconciledVictoryData;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.PlayerDeclarations;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class AARPhase3ReconcileCoordinator
{
    private Campaign campaign;
    private AARContext aarContext; 
    private ReconciledInMissionData reconciledInMissionData = new ReconciledInMissionData();
    
    public AARPhase3ReconcileCoordinator(
                    Campaign campaign, 
                    AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }
    
    
    public ReconciledInMissionData reconcileLogsWithAAR(Map<Integer, PlayerDeclarations> playerDeclarations) throws PWCGException
    {
        reconcileVictories(playerDeclarations);
        personnelChangesInMission();
        equipmentChangesInMission();
        return reconciledInMissionData;
    }

    private void reconcileVictories(Map<Integer, PlayerDeclarations> playerDeclarations) throws PWCGException
    {
        IClaimResolver missionResolver = ClaimResolverFactory.createClaimResolver(campaign, aarContext, playerDeclarations);
        ReconciledVictoryData reconciledVictoryData = missionResolver.resolvePlayerClaims();
        reconciledInMissionData.setReconciledVictoryData(reconciledVictoryData);
    }

    private void personnelChangesInMission() throws PWCGException 
    {
        PersonnelResultsInMissionHandler personnelHandler = new PersonnelResultsInMissionHandler(campaign, aarContext.getMissionEvaluationData());
        AARPersonnelLosses personnelResultsInMission = personnelHandler.personellChanges();
        reconciledInMissionData.setPersonnelLossesInMission(personnelResultsInMission);
    }

    private void equipmentChangesInMission() throws PWCGException 
    {
        EquipmentResultsInMissionHandler equipmentHandler = new EquipmentResultsInMissionHandler(campaign, aarContext.getMissionEvaluationData());
        AAREquipmentLosses equipmentResultsInMission = equipmentHandler.equipmentChanges();
        reconciledInMissionData.setEquipmentLossesInMission(equipmentResultsInMission);
    }
}
