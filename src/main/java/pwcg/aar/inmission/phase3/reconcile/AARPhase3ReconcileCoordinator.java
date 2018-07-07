package pwcg.aar.inmission.phase3.reconcile;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AAREquipmentLosses;
import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.inmission.phase2.logeval.AARMissionEvaluationData;
import pwcg.aar.inmission.phase3.reconcile.equipment.EquipmentResultsInMissionHandler;
import pwcg.aar.inmission.phase3.reconcile .personnel.PersonnelResultsInMissionHandler;
import pwcg.aar.inmission.phase3.reconcile.victories.ClaimDenier;
import pwcg.aar.inmission.phase3.reconcile.victories.ClaimResolver;
import pwcg.aar.inmission.phase3.reconcile.victories.PlayerDeclarations;
import pwcg.aar.inmission.phase3.reconcile.victories.ReconciledVictoryData;
import pwcg.aar.inmission.phase3.reconcile.victories.VerifiedVictoryGenerator;
import pwcg.aar.prelim.PwcgMissionData;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
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
    
    
    public ReconciledInMissionData reconcileLogsWithAAR(PlayerDeclarations playerDeclarations) throws PWCGException
    {
        reconcileVictories(playerDeclarations);
        personnelChangesInMission();
        equipmentChangesInMission();
        return reconciledInMissionData;
    }

    private void reconcileVictories(PlayerDeclarations playerDeclarations) throws PWCGException
    {
        ClaimResolver missionResolver = createClaimResolver(campaign, aarContext.getMissionEvaluationData(), aarContext.getPreliminaryData().getPwcgMissionData());
        ReconciledVictoryData reconciledVictoryData = missionResolver.resolvePlayerClaims(playerDeclarations);
        reconciledInMissionData.setReconciledVictoryData(reconciledVictoryData);
    }

    private ClaimResolver createClaimResolver(Campaign campaign, AARMissionEvaluationData evaluationData, PwcgMissionData pwcgMissionData) throws PWCGException
    {
        ClaimDenier claimDenier = new ClaimDenier(campaign, PWCGContextManager.getInstance().getPlaneTypeFactory());
        VerifiedVictoryGenerator verifiedVictoryGenerator = new VerifiedVictoryGenerator(campaign, aarContext);
       return new ClaimResolver(campaign, verifiedVictoryGenerator, claimDenier);
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
