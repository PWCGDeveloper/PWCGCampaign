package pwcg.aar.campaign.update;

import pwcg.aar.data.CampaignUpdateData;
import pwcg.campaign.Campaign;
import pwcg.campaign.resupply.equipment.EquipmentResupplyRecord;
import pwcg.campaign.tank.Equipment;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankStatus;
import pwcg.core.exception.PWCGException;

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
            EquippedTank equippedPlane = campaign.getEquipmentManager().getAnyTankWithPreference(planeSerialNumber);
            equippedPlane.setPlaneStatus(TankStatus.STATUS_DESTROYED);
            equippedPlane.setDateRemovedFromService(campaign.getDate());
        }
    }

    private void equipmentAdditions() throws PWCGException
    {
        for (EquipmentResupplyRecord equipmentResupplyRecord : campaignUpdateData.getResupplyData().getEquipmentResupplyData().getEquipmentResupplied())
        {
            Equipment equipment = campaign.getEquipmentManager().getEquipmentForCompany(equipmentResupplyRecord.getTransferTo());
            EquippedTank replacementPlane = equipmentResupplyRecord.getEquippedPlane();
            replacementPlane.setSquadronId(equipmentResupplyRecord.getTransferTo());
            replacementPlane.setPlaneStatus(TankStatus.STATUS_DEPLOYED);
            equipment.addEquippedTankToCompany(campaign, equipmentResupplyRecord.getTransferTo(), replacementPlane);
        }
    }
 }
