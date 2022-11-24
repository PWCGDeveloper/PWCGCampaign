package pwcg.aar.campaign.update;

import pwcg.aar.data.CampaignUpdateData;
import pwcg.campaign.Campaign;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneStatus;
import pwcg.campaign.resupply.equipment.EquipmentResupplyRecord;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;

public class EquipmentUpdater 
{
	private Campaign campaign;
    private CampaignUpdateData campaignUpdateData;

	public EquipmentUpdater (Campaign campaign, CampaignUpdateData campaignUpdateData) 
	{
        this.campaign = campaign;
        this.campaignUpdateData = campaignUpdateData;
	}
	
    public void equipmentUpdatesForSquadrons() throws PWCGException 
    {
        equipmentRemovals();
        equipmentAdditions();
    }

    private void equipmentRemovals() throws PWCGException
    {
        for (Integer planeSerialNumber : campaignUpdateData.getEquipmentLosses().getPlanesDestroyed().keySet())
        {
            try
            {
                EquippedPlane equippedPlane = campaign.getEquipmentManager().getAnyPlaneWithPreference(planeSerialNumber);
                equippedPlane.setPlaneStatus(PlaneStatus.STATUS_DESTROYED);
                equippedPlane.setDateRemovedFromService(campaign.getDate());
            }
            catch (Exception e)
            {
                PWCGLogger.logException(e);
            }
        }
    }

    private void equipmentAdditions() throws PWCGException
    {
        for (EquipmentResupplyRecord equipmentResupplyRecord : campaignUpdateData.getResupplyData().getEquipmentResupplyData().getEquipmentResupplied())
        {
            Equipment equipment = campaign.getEquipmentManager().getEquipmentForSquadron(equipmentResupplyRecord.getTransferTo());
            EquippedPlane replacementPlane = equipmentResupplyRecord.getEquippedPlane();
            replacementPlane.setSquadronId(equipmentResupplyRecord.getTransferTo());
            replacementPlane.setPlaneStatus(PlaneStatus.STATUS_DEPLOYED);
            equipment.addEquippedPlaneToSquadron(campaign, equipmentResupplyRecord.getTransferTo(), replacementPlane);
        }
    }
 }
