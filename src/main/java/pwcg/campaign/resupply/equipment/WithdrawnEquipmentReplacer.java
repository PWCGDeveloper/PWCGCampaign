package pwcg.campaign.resupply.equipment;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneArchType;
import pwcg.campaign.plane.PlaneEquipmentFactory;
import pwcg.campaign.resupply.depot.EquipmentReplacementUtils;
import pwcg.campaign.squadron.Company;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.core.utils.RandomNumberGenerator;

public class WithdrawnEquipmentReplacer
{
    private Campaign campaign;
    private Equipment equipment;
    private Company squadron;
    
    public WithdrawnEquipmentReplacer(Campaign campaign, Equipment equipment, Company squadron)
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
            if (isWithdrawPlane(plane))
            {
                equipment.deactivateEquippedPlaneFromSquadron(plane.getSerialNumber(), campaign.getDate());
                ++planesRemoved;
            }
        }

        return planesRemoved;
    }
    
    private boolean isWithdrawPlane(EquippedPlane plane)
    {
        if (campaign.getDate().before(plane.getWithdrawal()))
        {
            return false;
        }
        
        if (plane.isEquipmentRequest())
        {
            return false;
        }
        
        return true;
    }
    
    private int replaceWithNewPlanes(int planesRemoved) throws PWCGException
    {
        int numberOfPlanesToAdd = calculatePlanesNeeded(planesRemoved);
        for (int i = 0; i < numberOfPlanesToAdd; ++i)
        {
            String planeTypeName = determinePlaneType();
            if (!planeTypeName.isEmpty())
            {
                addPlaneToSquadron(planeTypeName);
            }
        }
        
        return numberOfPlanesToAdd;
    }

    private int calculatePlanesNeeded(int planesRemoved)
    {
        int minNeeded = Company.MIN_REEQUIPMENT_SIZE - equipment.getActiveEquippedPlanes().size();
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
        if (!planeArchTypeName.isEmpty())
        {
            PlaneArchType planeArchType = PWCGContext.getInstance().getPlaneTypeFactory().getPlaneArchType(planeArchTypeName);
            String planeTypeName = EquipmentReplacementUtils.getTypeForReplacement(campaign.getDate(), planeArchType);
            return planeTypeName;
        }
        else
        {
            return "";
        }
    }
    
    private String chooseArchTypeForSquadron() throws PWCGException
    {
        List<String> archTypes = determineAvailableArchTypes();
        if (archTypes.size() > 0)
        {
            int index = RandomNumberGenerator.getRandom(archTypes.size());
            return archTypes.get(index);
        }
        else
        {
            return "";
        }
    }

    private List<String> determineAvailableArchTypes() throws PWCGException
    {
        List<String> availableArchTypes = new ArrayList<>();
        for (String planeArchTypeName : equipment.getArchTypes())
        {
            PlaneArchType planeArchType = PWCGContext.getInstance().getPlaneTypeFactory().getPlaneArchType(planeArchTypeName);
            String planeTypeName = EquipmentReplacementUtils.getTypeForReplacement(campaign.getDate(), planeArchType);
            if (planeTypeName != null && !planeTypeName.isEmpty())
            {
                availableArchTypes.add(planeArchTypeName);
            }
            else
            {
                PWCGLogger.log(LogLevel.DEBUG, "");
            }
        }
        return availableArchTypes;
    }

    private void addPlaneToSquadron(String planeTypeName) throws PWCGException
    {
        EquippedPlane equippedPlane = PlaneEquipmentFactory.makePlaneForSquadron(campaign, planeTypeName, squadron.getSquadronId());
        equipment.addEquippedPlaneToSquadron(campaign, squadron.getSquadronId(), equippedPlane);
    }
}
