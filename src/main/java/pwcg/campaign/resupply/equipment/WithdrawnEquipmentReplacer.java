package pwcg.campaign.resupply.equipment;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneArchType;
import pwcg.campaign.plane.PlaneEquipmentFactory;
import pwcg.campaign.resupply.depo.EquipmentReplacementUtils;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class WithdrawnEquipmentReplacer
{
    private Campaign campaign;
    private Equipment equipment;
    private Squadron squadron;
    
    public WithdrawnEquipmentReplacer(Campaign campaign, Equipment equipment, Squadron squadron)
    {
        this.campaign = campaign;
        this.equipment = equipment;
        this.squadron = squadron;
    }
    
    public int replaceWithdrawnEquipment() throws PWCGException
    {
        int planesRemoved = removeWithdrawnPlanes();
        int planesAdded = replaceWithNewPlanes(planesRemoved);
        return planesAdded;
    }

    private int removeWithdrawnPlanes()
    {
        int planesRemoved = 0;
        for (EquippedPlane plane: equipment.getActiveEquippedPlanes().values())
        {
            if (campaign.getDate().after(plane.getWithdrawal()))
            {
                equipment.deactivateEquippedPlaneFromSquadron(plane.getSerialNumber(), campaign.getDate());
                ++planesRemoved;
            }
        }

        return planesRemoved;
    }
    
    private int replaceWithNewPlanes(int planesRemoved) throws PWCGException
    {
        int numberOfPlanesToAdd = calculatePlanesNeeded(planesRemoved);
        for (int i = 0; i < numberOfPlanesToAdd; ++i)
        {
            String planeTypeName = determinePlaneType();
            addPlaneToSquadron(planeTypeName);
        }
        
        return numberOfPlanesToAdd;
    }

    private int calculatePlanesNeeded(int planesRemoved)
    {
        int minNeeded = Squadron.MIN_REEQUIPMENT_SIZE - equipment.getActiveEquippedPlanes().size();
        int numNeeded = planesRemoved;
        if (minNeeded > planesRemoved)
        {
            numNeeded = minNeeded;
        }
        
        return numNeeded;
    }

    private String determinePlaneType() throws PWCGException
    {
        String planeArchTypeName = chooseArchTypeForSquadron();
        PlaneArchType planeArchType = PWCGContext.getInstance().getPlaneTypeFactory().getPlaneArchType(planeArchTypeName);
        String planeTypeName = EquipmentReplacementUtils.getTypeForReplacement(campaign.getDate(), planeArchType);
        return planeTypeName;
    }

    private String chooseArchTypeForSquadron()
    {
        List<String> archTypes = equipment.getArchTypes();
        int index = RandomNumberGenerator.getRandom(archTypes.size());
        return archTypes.get(index);
    }

    private void addPlaneToSquadron(String planeTypeName) throws PWCGException
    {
        PlaneEquipmentFactory equipmentFactory = new PlaneEquipmentFactory(campaign);
        EquippedPlane equippedPlane = equipmentFactory.makePlaneForSquadron(planeTypeName, squadron.getSquadronId());
        equipment.addEquippedPlane(equippedPlane);
    }
}
