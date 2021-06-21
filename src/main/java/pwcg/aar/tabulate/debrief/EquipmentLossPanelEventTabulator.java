package pwcg.aar.tabulate.debrief;

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
        Map<Integer, PlaneStatusEvent> allPlanesLost = planeLossEventGenerator.createPlaneLossEvents(aarContext.getEquipmentLosses());
        equipmentLossPanelData.setEquipmentLost(allPlanesLost);
                
        return equipmentLossPanelData;
    }
}
