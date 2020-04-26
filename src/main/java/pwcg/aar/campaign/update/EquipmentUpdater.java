package pwcg.aar.campaign.update;

import pwcg.aar.data.AARContext;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneStatus;
import pwcg.campaign.resupply.equipment.EquipmentResupplyRecord;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;

public class EquipmentUpdater 
{
	private Campaign campaign;
    private AARContext aarContext;

	public EquipmentUpdater (Campaign campaign, AARContext aarContext) 
	{
        this.campaign = campaign;
        this.aarContext = aarContext;
	}
	
    public void equipmentUpdatesForSquadrons() throws PWCGException 
    {
        equipmentRemovals();
        equipmentAdditions();
    }

    private void equipmentRemovals() throws PWCGException
    {
        for (Integer planeSerialNumber : aarContext.getCampaignUpdateData().getEquipmentLosses().getPlanesDestroyed().keySet())
        {
            EquippedPlane equippedPlane = campaign.getEquipmentManager().getPlaneFromAnySquadron(planeSerialNumber);
            equippedPlane.setPlaneStatus(PlaneStatus.STATUS_DESTROYED);
            equippedPlane.setDateRemovedFromService(campaign.getDate());
        }
    }

    private void equipmentAdditions()
    {
        for (EquipmentResupplyRecord equipmentResupplyRecord : aarContext.getCampaignUpdateData().getResupplyData().getEquipmentResupplyData().getEquipmentResupplied())
        {
            Equipment equipment = campaign.getEquipmentManager().getEquipmentForSquadron(equipmentResupplyRecord.getTransferTo());
            EquippedPlane replacementPlane = equipmentResupplyRecord.getEquippedPlane();
            replacementPlane.setSquadronId(equipmentResupplyRecord.getTransferTo());
            replacementPlane.setPlaneStatus(PlaneStatus.STATUS_DEPLOYED);
            try {
                PWCGContext.getInstance().getPlaneMarkingManager().allocatePlaneIdCode(campaign, equipmentResupplyRecord.getTransferTo(), equipment, replacementPlane);
            }
            catch (PWCGException e)
            {
                PWCGLogger.logException(e);
            }
            equipment.addEquippedPlane(replacementPlane);
        }
    }
 }
