package pwcg.aar.inmission.phase3.reconcile.equipment;

import pwcg.aar.data.AAREquipmentLosses;
import pwcg.aar.inmission.phase2.logeval.AARMissionEvaluationData;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.campaign.tank.TankStatus;
import pwcg.core.exception.PWCGException;

public class EquipmentResultsInMissionHandler
{
    private AAREquipmentLosses equipmentLosses = new AAREquipmentLosses();
    private AARMissionEvaluationData evaluationData;

    public EquipmentResultsInMissionHandler(AARMissionEvaluationData evaluationData)
    {
        this.evaluationData = evaluationData;
    }

    public AAREquipmentLosses equipmentChanges() throws PWCGException
    {
        for (LogPlane logPlane : evaluationData.getPlaneAiEntities().values())
        {
            if (logPlane.getPlaneStatus() == TankStatus.STATUS_DESTROYED)
            {
                equipmentLosses.addPlaneDestroyed(logPlane);
            }
        }

        return equipmentLosses;
    }

}
