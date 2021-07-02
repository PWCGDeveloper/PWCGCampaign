package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AAREquipmentLosses;
import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public class AARSimulatedMission
{
    private Campaign campaign;
    private AARContext aarContext;

    public AARSimulatedMission(Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }

    public void simulateMissionEvents() throws PWCGException
    {
        outOfMissionVictoriesAndLosses();
    }
    
    private void outOfMissionVictoriesAndLosses() throws PWCGException
    {
        OutOfMissionVictoryEventHandler victoryEventHandler = new OutOfMissionVictoryEventHandler(campaign, aarContext);
        OutOfMissionVictoryData victoriesOutOMission = victoryEventHandler.generateOutOfMissionVictories();

        aarContext.getPersonnelAcheivements().mergeVictories(victoriesOutOMission.getVictoryAwardsBySquadronMember());
        outOfMissionLosses(victoriesOutOMission.getShotDownPilots(), victoriesOutOMission.getShotDownPlanes());
    }

    private void outOfMissionLosses(Map<Integer, SquadronMember> shotDownPilots, Map<Integer, LogPlane> shotDownPlanes) throws PWCGException 
    {
        OutOfMissionLossHandler lossHandler = new  OutOfMissionLossHandler(campaign, aarContext);
        lossHandler.lossesOutOfMission(shotDownPilots, shotDownPlanes);
        
        AARPersonnelLosses personnelLosses = lossHandler.getOutOfMissionPersonnelLosses();
        aarContext.getPersonnelLosses().merge(personnelLosses);
        
        AAREquipmentLosses equipmentLosses = lossHandler.getOutOfMissionEquipmentLosses();
        aarContext.getEquipmentLosses().merge(equipmentLosses);
    }
}
