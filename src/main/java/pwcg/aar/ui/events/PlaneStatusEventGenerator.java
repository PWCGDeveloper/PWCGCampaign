package pwcg.aar.ui.events;

import java.util.HashMap;
import java.util.Map;

import pwcg.aar.data.AAREquipmentLosses;
import pwcg.aar.ui.events.model.PlaneStatusEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneStatus;
import pwcg.core.exception.PWCGException;

public class PlaneStatusEventGenerator
{    
    private Campaign campaign;
	private Map<Integer, PlaneStatusEvent> planeStatusEvents = new HashMap<>();

    public PlaneStatusEventGenerator (Campaign campaign)
    {
        this.campaign = campaign;
    }

    public Map<Integer, PlaneStatusEvent> createPlaneLossEvents(AAREquipmentLosses equipmentLossesInMission) throws PWCGException
    {
        for (EquippedPlane equippedPlane : equipmentLossesInMission.getPlanesDestroyed().values())
        {
            PlaneStatusEvent equippedPlaneLostEvent = makePlaneLostEvent(equippedPlane);
            planeStatusEvents.put(equippedPlane.getSerialNumber(), equippedPlaneLostEvent);
        }
        
        for (EquippedPlane equippedPlane : equipmentLossesInMission.getPlanesDestroyed().values())
        {
            PlaneStatusEvent equippedPlaneLostEvent = makePlaneLostEvent(equippedPlane);
            planeStatusEvents.put(equippedPlane.getSerialNumber(), equippedPlaneLostEvent);
        }
        
        return planeStatusEvents;
    }

    private PlaneStatusEvent makePlaneLostEvent(EquippedPlane equippedPlane) throws PWCGException
    {
        boolean isNewsworthy = true;
        PlaneStatusEvent planeStatusEvent = new PlaneStatusEvent(equippedPlane, PlaneStatus.STATUS_DESTROYED, campaign.getDate(), isNewsworthy);
        return planeStatusEvent;
    }
}
