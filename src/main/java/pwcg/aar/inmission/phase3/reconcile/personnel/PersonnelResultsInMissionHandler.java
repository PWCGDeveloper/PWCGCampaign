package pwcg.aar.inmission.phase3.reconcile.personnel;

import java.util.List;
import java.util.Map;

import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.inmission.phase2.logeval.AARMissionEvaluationData;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPilot;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.Ace;
import pwcg.core.exception.PWCGException;

public class PersonnelResultsInMissionHandler
{
    private Campaign campaign;
    private AARMissionEvaluationData evaluationData;
    private AARPersonnelLosses personnelResults;

    public PersonnelResultsInMissionHandler(Campaign campaign, AARMissionEvaluationData evaluationData)
    {
        this.campaign = campaign;
        this.evaluationData = evaluationData;
    }
    
    public AARPersonnelLosses personellChanges() throws PWCGException
    {
        handlePersonnelLosses(evaluationData.getPilotsInMission());
        handleAceLosses(evaluationData.getAceCrewsInMission());
        
        return personnelResults;
    }

    private void handlePersonnelLosses(List<LogPilot> pilotStatusList) throws PWCGException
    {
        PersonnelLossHandler personnelLossHandler = new PersonnelLossHandler(campaign);
        personnelResults = personnelLossHandler.pilotsShotDown(pilotStatusList);
     }

    private void handleAceLosses(List<LogPilot> aceStatusList) throws PWCGException
    {
        PersonnelAceLossInMissionHandler personnelAceLossHandler = new PersonnelAceLossInMissionHandler(campaign);
        Map<Integer, Ace> acesKilled = personnelAceLossHandler.acesShotDownInMission(aceStatusList);
        personnelResults.mergeAcesKilled(acesKilled);
    }    
}
