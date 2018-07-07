package pwcg.aar.inmission.phase3.reconcile.equipment;

import pwcg.aar.data.AAREquipmentLosses;
import pwcg.aar.inmission.phase2.logeval.AARMissionEvaluationData;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignEquipmentManager;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneStatus;
import pwcg.core.exception.PWCGException;

public class EquipmentResultsInMissionHandler
{
    private AAREquipmentLosses equipmentLosses = new AAREquipmentLosses();
    private Campaign campaign;
    private AARMissionEvaluationData evaluationData;

    public EquipmentResultsInMissionHandler(Campaign campaign, AARMissionEvaluationData evaluationData)
    {
        this.campaign = campaign;
        this.evaluationData = evaluationData;
    }

    public AAREquipmentLosses equipmentChanges() throws PWCGException
    {
        for (LogPlane logPlane : evaluationData.getPlaneAiEntities().values())
        {
            if (logPlane.getPlaneStatus() == PlaneStatus.STATUS_DESTROYED)
            {
                CampaignEquipmentManager campaignEquipmentManager = campaign.getEquipmentManager();            
                EquippedPlane shotDownPlane = campaignEquipmentManager.destroyPlane(logPlane.getPlaneSerialNumber(), campaign.getDate());
                equipmentLosses.addPlaneDestroyed(shotDownPlane);
            }
        }

        return equipmentLosses;
    }

}
