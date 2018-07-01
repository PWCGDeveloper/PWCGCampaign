package pwcg.campaign;

import pwcg.campaign.personnel.PersonnelReplacementsService;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public class CampaignSerialNumberHelper
{
    
    public static  int setNextPilotSerialNumberForCampaign(CampaignPersonnelManager personnelManager) throws PWCGException
    {
        int lastSerialNumber = SerialNumber.AI_STARTING_SERIAL_NUMBER;
        for (SquadronPersonnel squadronPersonnel : personnelManager.getAllSquadronPersonnel())
        {
            for (SquadronMember squadronMember : squadronPersonnel.getActiveSquadronMembers().getSquadronMembers().values())
            {
                if (squadronMember.getSerialNumber() > lastSerialNumber)
                {
                    lastSerialNumber = squadronMember.getSerialNumber();
                }
            }
        }
        
        for (PersonnelReplacementsService personnelReplacementsService : personnelManager.getAllReplacements())
        {
            for (SquadronMember replacementSquadronMember : personnelReplacementsService.getReplacements().getSquadronMembers().values())
            {
                if (replacementSquadronMember.getSerialNumber() > lastSerialNumber)
                {
                    lastSerialNumber = replacementSquadronMember.getSerialNumber();
                }
            }
        }
        
        ++lastSerialNumber;
        return lastSerialNumber;
    }

    public static int setNextPlaneSerialNumberForCampaign(CampaignEquipmentManager equipmentManager)
    {
        int lastSerialNumber = SerialNumber.PLANE_STARTING_SERIAL_NUMBER;
        for (Equipment equipment : equipmentManager.getEquipmentAllSquadrons().values())
        {
            for (EquippedPlane equippedPlane : equipment.getEquippedPlanes().values())
            {
                if (equippedPlane.getSerialNumber() > lastSerialNumber)
                {
                    lastSerialNumber = equippedPlane.getSerialNumber();
                }
            }
        }
        
        for (Equipment equipment : equipmentManager.getEquipmentReplacements().values())
        {
            for (EquippedPlane equippedPlane : equipment.getEquippedPlanes().values())
            {
                if (equippedPlane.getSerialNumber() > lastSerialNumber)
                {
                    lastSerialNumber = equippedPlane.getSerialNumber();
                }
            }
        }
        
        ++lastSerialNumber;
        return lastSerialNumber;
    }

}
