package pwcg.aar.campaign.update;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneArchType;
import pwcg.campaign.plane.PlaneEquipmentFactory;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class CampaignEquipmentArchtypeChangeHandler 
{
    private Campaign campaign;
    private Date newDate;
    private Set<Squadron> squadronsToEquip = new HashSet<>();
	
	public CampaignEquipmentArchtypeChangeHandler(Campaign campaign, Date newDate)
	{
        this.campaign = campaign;
        this.newDate = newDate;
	}
	
	public void updateCampaignEquipmentForArchtypeChange () throws PWCGException
	{
        removeOutdatedArchTypes();
        addNewArchTypes();
	}

    private void removeOutdatedArchTypes() throws PWCGException
    {
        SquadronManager squadronManager = PWCGContextManager.getInstance().getSquadronManager();
        for (Squadron squadron : squadronManager.getActiveSquadrons(campaign.getDate()))
        {
            Equipment squadronEquipment = campaign.getEquipmentManager().getEquipmentForSquadron(squadron.getSquadronId());
            for (EquippedPlane plane : squadronEquipment.getEquippedPlanes().values())
            {
                boolean isActiveArchType = squadron.isPlaneInActiveSquadronArchTypes(newDate, plane);
                
                if (!isActiveArchType)
                {
                    squadronEquipment.removeEquippedPlane(plane.getSerialNumber());
                    squadronsToEquip.add(squadron);
                }
            }
        }
    }

    private void addNewArchTypes() throws PWCGException
    {
        for (Squadron squadron : squadronsToEquip)
        {
            Equipment squadronEquipment = campaign.getEquipmentManager().getEquipmentForSquadron(squadron.getSquadronId());
            int numPlanesNeeded = Squadron.SQUADRON_EQUIPMENT_SIZE - squadronEquipment.getEquippedPlanes().size();

            PlaneType bestPlaneType = getBestPlaneTypeForSquadron(squadron);
            for (int i = 0; i < numPlanesNeeded; ++i)
            {
                PlaneEquipmentFactory equipmentFactory = new PlaneEquipmentFactory(campaign);
                EquippedPlane replacementPlane = equipmentFactory.makePlaneForSquadron(bestPlaneType.getType(), squadron.getSquadronId());
                squadronEquipment.addEquippedPlane(replacementPlane);
            }
        }
    }
    
    private PlaneType getBestPlaneTypeForSquadron(Squadron squadron) throws PWCGException
    {
        List<PlaneType> planeTypesForSquadron = new ArrayList<>();
        for (PlaneArchType archType : squadron.determineCurrentAircraftArchTypes(newDate))
        {
            List<PlaneType> planeTypesForArchType = archType.getActiveMemberPlaneTypes(newDate);
            planeTypesForSquadron.addAll(planeTypesForArchType);
        }
        
        PlaneType bestPlaneType = planeTypesForSquadron.get(0);
        for (PlaneType planeType : planeTypesForSquadron)
        {
            if (planeType.getGoodness() > bestPlaneType.getGoodness())
            {
                bestPlaneType = planeType;
            }
        }
        
        return bestPlaneType;
    }
}
