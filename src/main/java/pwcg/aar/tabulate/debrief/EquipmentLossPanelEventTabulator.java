package pwcg.aar.tabulate.debrief;

import java.util.HashMap;
import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.aar.ui.display.model.AAREquipmentLossPanelData;
import pwcg.aar.ui.events.PlaneStatusEventGenerator;
import pwcg.aar.ui.events.model.PlaneStatusEvent;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class EquipmentLossPanelEventTabulator
{
    private Campaign campaign;
    private AARContext aarContext;

    private AAREquipmentLossPanelData equipmentLossPanelData = new AAREquipmentLossPanelData();

    public EquipmentLossPanelEventTabulator (Campaign campaign,AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }
        
    public AAREquipmentLossPanelData tabulateForAAREquipmentLossPanel() throws PWCGException
    {
        PlaneStatusEventGenerator planeLossEventGenerator = new PlaneStatusEventGenerator(campaign);
        Map<Integer, PlaneStatusEvent> allPlanesLost = determineAllPlanesLostInMission(planeLossEventGenerator);
        Map<Integer, PlaneStatusEvent> squadronPlanesLost = determinePlanesLostForSquadron(allPlanesLost);
        equipmentLossPanelData.setEquipmentLost(squadronPlanesLost);
                
        return equipmentLossPanelData;
    }

    private Map<Integer, PlaneStatusEvent> determineAllPlanesLostInMission(PlaneStatusEventGenerator planeLossEventGenerator) throws PWCGException
    {
        Map<Integer, PlaneStatusEvent> allPlanesLost = new HashMap<>();
        Map<Integer, PlaneStatusEvent> planesLostInMission = planeLossEventGenerator.createPlaneLossEvents(aarContext.getReconciledInMissionData().getEquipmentLossesInMission());
        allPlanesLost.putAll(planesLostInMission);
        
        Map<Integer, PlaneStatusEvent> planesLostElapsedTime = planeLossEventGenerator.createPlaneLossEvents(aarContext.getReconciledOutOfMissionData().getEquipmentLossesOutOfMission());
        allPlanesLost.putAll(planesLostElapsedTime);
        return allPlanesLost;
    }

    private Map<Integer, PlaneStatusEvent> determinePlanesLostForSquadron(Map<Integer, PlaneStatusEvent> allPlanesLost)
    {
        Map<Integer, PlaneStatusEvent> squadronPlanesLost = new HashMap<>();
        for (PlaneStatusEvent planeLostEvent : allPlanesLost.values())
        {
            if (planeLostEvent.getPlane().getSquadronId() == campaign.getSquadronId())
            {
                squadronPlanesLost.put(planeLostEvent.getPlane().getSerialNumber(), planeLostEvent);                
            }
        }
        return squadronPlanesLost;
    }
}
