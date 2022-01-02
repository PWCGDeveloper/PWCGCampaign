package pwcg.aar.inmission.phase3.reconcile.personnel;

import java.util.List;

import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.inmission.phase2.logeval.AARMissionEvaluationData;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogCrewMember;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class PersonnelLossesInMissionHandler
{
    private Campaign campaign;
    private AARMissionEvaluationData evaluationData;
    private AARPersonnelLosses personnelLosses;

    public PersonnelLossesInMissionHandler(Campaign campaign, AARMissionEvaluationData evaluationData)
    {
        this.campaign = campaign;
        this.evaluationData = evaluationData;
    }
    
    public AARPersonnelLosses personellChanges() throws PWCGException
    {
        handlePersonnelLosses(evaluationData.getCrewMembersInMission());        
        return personnelLosses;
    }

    private void handlePersonnelLosses(List<LogCrewMember> crewMemberStatusList) throws PWCGException
    {
        PersonnelLossHandler personnelLossHandler = new PersonnelLossHandler(campaign);
        personnelLosses = personnelLossHandler.crewMembersShotDown(crewMemberStatusList);
     }
}
