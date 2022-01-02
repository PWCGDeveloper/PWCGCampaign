package pwcg.aar.inmission.phase3.reconcile.victories.coop;

import java.util.List;
import java.util.Map;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.aar.inmission.phase3.reconcile.victories.IClaimResolver;
import pwcg.aar.inmission.phase3.reconcile.victories.ReconciledMissionVictoryData;
import pwcg.aar.inmission.phase3.reconcile.victories.common.ConfirmedVictories;
import pwcg.aar.inmission.phase3.reconcile.victories.common.GroundDeclarationResolver;
import pwcg.aar.inmission.phase3.reconcile.victories.common.VictoryEventHandler;
import pwcg.aar.inmission.phase3.reconcile.victories.common.VictorySorter;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.Victory;
import pwcg.core.exception.PWCGException;

public class ClaimResolverCompetitiveCoop implements IClaimResolver
{
    private Campaign campaign;
    private List<LogVictory> logVictories;
    private ReconciledMissionVictoryData reconciledMissionData = new ReconciledMissionVictoryData();


    public ClaimResolverCompetitiveCoop (Campaign campaign, List<LogVictory> logVictories)
    {
        this.campaign = campaign;
        this.logVictories = logVictories;
    }

    @Override
    public ReconciledMissionVictoryData resolvePlayerClaims() throws PWCGException
    {
        VictorySorter victorySorter = createVictorySorter();
        ConfirmedVictories verifiedAirMissionResultVictorys = createAirVictories(victorySorter);
        ConfirmedVictories verifiedGroundMissionResultVictorys = createGroundVictories(victorySorter);
        ConfirmedVictories verifiedMissionResultVictorys = mergeVictories(verifiedAirMissionResultVictorys, verifiedGroundMissionResultVictorys);
        createVictoriesByCrewMember(verifiedMissionResultVictorys);
        return reconciledMissionData;
    }

    private VictorySorter createVictorySorter() throws PWCGException
    {
        VictorySorter victorySorter = new VictorySorter();
        victorySorter.sortVictories(logVictories);
        return victorySorter;
    }

    private ConfirmedVictories createAirVictories(VictorySorter victorySorter)
    {
        CoopAirDeclarationResolver airDeclarationResolver = new CoopAirDeclarationResolver(victorySorter);
        ConfirmedVictories verifiedAirMissionResultVictorys = airDeclarationResolver.determineCoopAirResults();
        return verifiedAirMissionResultVictorys;
    }

    private ConfirmedVictories createGroundVictories(VictorySorter victorySorter)
    {
        GroundDeclarationResolver groundDeclarationResolver = new GroundDeclarationResolver(victorySorter);
        ConfirmedVictories verifiedGroundMissionResultVictorys = groundDeclarationResolver.determineGroundResults();
        return verifiedGroundMissionResultVictorys;
    }

    private ConfirmedVictories mergeVictories(ConfirmedVictories verifiedAirMissionResultVictorys, ConfirmedVictories verifiedGroundMissionResultVictorys)
    {
        ConfirmedVictories verifiedMissionResultVictorys = new ConfirmedVictories();
        verifiedMissionResultVictorys.addConfirmedVictories(verifiedAirMissionResultVictorys);
        verifiedMissionResultVictorys.addConfirmedVictories(verifiedGroundMissionResultVictorys);
        return verifiedMissionResultVictorys;
    }

    private void createVictoriesByCrewMember(ConfirmedVictories verifiedMissionResultVictorys) throws PWCGException
    {
        VictoryEventHandler victoryHandler = new VictoryEventHandler(campaign);
        Map<Integer, List<Victory>> victories = victoryHandler.recordVictories(verifiedMissionResultVictorys);
        reconciledMissionData.addVictoryAwards(victories);
    }
}
