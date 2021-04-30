package pwcg.campaign.resupply;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneArchType;
import pwcg.campaign.plane.PlaneEquipmentFactory;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.resupply.depot.EquipmentWeightCalculator;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class InitialSquadronEquipper 
{
    private Campaign campaign;
    private Squadron squadron;
    private Equipment equipment = new Equipment();
    private int planesNeeded = Squadron.SQUADRON_EQUIPMENT_SIZE;
    private EquipmentWeightCalculator equipmentWeightCalculator;

	public InitialSquadronEquipper(Campaign campaign, Squadron squadron, EquipmentWeightCalculator equipmentWeightCalculator) 
	{
        this.campaign = campaign;
        this.squadron = squadron;
        this.equipmentWeightCalculator = equipmentWeightCalculator;
	}

    public Equipment generateEquipment() throws PWCGException 
    {
        determinePlaneWeightsForSquadron();
        generatePlanesForSquadron();
        return equipment;
    }
    
    private void determinePlaneWeightsForSquadron() throws PWCGException
    {
        List<PlaneArchType> currentAircraftArchTypes = squadron.determineCurrentAircraftArchTypes(campaign.getDate());
        
        List<PlaneType> planeTypesInSquadron = new ArrayList<>();
        for (PlaneArchType planeArchType : currentAircraftArchTypes)
        {
            List<PlaneType> planeTypesForArchType = PWCGContext.getInstance().getPlaneTypeFactory().createActivePlaneTypesForArchType(planeArchType.getPlaneArchTypeName(), campaign.getDate());
            planeTypesInSquadron.addAll(planeTypesForArchType);
        }
        
        equipmentWeightCalculator.determinePlaneWeightsForPlanes(planeTypesInSquadron);
    }

    private void generatePlanesForSquadron() throws PWCGException 
    {       
        
        for (int i = 0; i < planesNeeded; ++i)
        {
            String planeTypeName = equipmentWeightCalculator.getPlaneTypeFromWeight();
            
            EquippedPlane equippedPlane = PlaneEquipmentFactory.makePlaneForSquadron(campaign, planeTypeName, squadron.getSquadronId());
            equipment.addEquippedPlaneToSquadron(campaign, squadron.getSquadronId(), equippedPlane);
        }
    }

}
