package pwcg.campaign;

import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.io.json.CampaignEquipmentIOJson;
import pwcg.campaign.io.json.CampaignPersonnelIOJson;
import pwcg.campaign.personnel.SquadronMemberFemaleGenerator;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronMemberReplacementFactory;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneArchType;
import pwcg.campaign.plane.PlaneEquipmentFactory;
import pwcg.campaign.resupply.depot.EquipmentReplacementUtils;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class EmergencyResupplyHandler
{
    private Campaign campaign;
    
    public EmergencyResupplyHandler(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public void emergencyResupply() throws PWCGException
    {
        emergencyResupplyPersonnel();
        emergencyResupplyEquipment();
    }

    private void emergencyResupplyPersonnel() throws PWCGException
    {
        for (SquadronPersonnel squadronPersonnel : campaign.getPersonnelManager().getCampaignPersonnel().values())
        {
            if (!squadronPersonnel.isSquadronPersonnelViable())
            {
                makeSquadronPersonnelViable(squadronPersonnel);
            }
        }
    }

    private void makeSquadronPersonnelViable(SquadronPersonnel squadronPersonnel) throws PWCGException
    {
        int totalTransfers = calculatePersonnelToReplaceForSquadron(squadronPersonnel);
        replacePersonnelForSquadron(squadronPersonnel, totalTransfers);
        CampaignPersonnelIOJson.writeSquadron(campaign, squadronPersonnel.getSquadron().getSquadronId());
    }

    private int calculatePersonnelToReplaceForSquadron(SquadronPersonnel squadronPersonnel) throws PWCGException
    {
        SquadronMembers activeSquadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        int activeSquadronSize = activeSquadronMembers.getActiveCount(campaign.getDate());
        int transfersNeededForFull = Squadron.SQUADRON_STAFF_SIZE -  activeSquadronSize;
        int transfersNeededForViable = (Squadron.SQUADRON_STAFF_SIZE / 2) -  activeSquadronSize;
        
        int transfersNeededForViableWithCushion = transfersNeededForViable + 2;
        int extraTransfers = RandomNumberGenerator.getRandom(transfersNeededForFull - transfersNeededForViableWithCushion);
        int totalTransfers = transfersNeededForViableWithCushion + extraTransfers;
        return totalTransfers;
    }

    private void replacePersonnelForSquadron(SquadronPersonnel squadronPersonnel, int totalTransfers) throws PWCGException
    {
        SquadronMemberReplacementFactory replacementFactory = new SquadronMemberReplacementFactory(
                campaign, squadronPersonnel.getSquadron().determineServiceForSquadron(campaign.getDate()));
        
        for (int i = 0; i < totalTransfers; ++i)
        {
            SquadronMember replacement = replacementFactory.createAIReplacementPilot();
            replacement.setSquadronId(squadronPersonnel.getSquadron().getSquadronId());
            SquadronMember convertedReplacement = SquadronMemberFemaleGenerator.convertToFemale(campaign, squadronPersonnel.getSquadron().getSquadronId(), replacement);
            squadronPersonnel.addSquadronMember(convertedReplacement);
        }
    }

    private void emergencyResupplyEquipment() throws PWCGException
    {
        for (int squadronId : campaign.getEquipmentManager().getEquipmentAllSquadrons().keySet())
        {
            Equipment squadronEquipment = campaign.getEquipmentManager().getEquipmentForSquadron(squadronId);
            if (!squadronEquipment.isSquadronEquipmentViable())
            {
                makeSquadronEquipmentViable(squadronEquipment, squadronId);
            }
        }
    }

    private void makeSquadronEquipmentViable(Equipment squadronEquipment, int squadronId) throws PWCGException
    {
        int totalNewPlaness = calculatePersonnelToReplaceForSquadron(squadronEquipment);
        replaceEquipmentForSquadron(squadronEquipment, squadronId, totalNewPlaness);
        CampaignEquipmentIOJson.writeEquipmentForSquadron(campaign, squadronId);
    }

    private int calculatePersonnelToReplaceForSquadron(Equipment squadronEquipment)
    {
        int activeSquadronEquipment = squadronEquipment.getActiveEquippedPlanes().size();
        int equipmentNeededForFull = Squadron.SQUADRON_EQUIPMENT_SIZE -  activeSquadronEquipment;
        int equipmentNeededForViable = (Squadron.SQUADRON_EQUIPMENT_SIZE / 2) -  activeSquadronEquipment;
        
        int equipmentNeededForViableWithCushion = equipmentNeededForViable + 2;
        int extraEquipment = RandomNumberGenerator.getRandom(equipmentNeededForFull - equipmentNeededForViableWithCushion);
        int totalEquipment = equipmentNeededForViableWithCushion + extraEquipment;
        return totalEquipment;
    }

    private void replaceEquipmentForSquadron(Equipment squadronEquipment, int squadronId, int totalNewPlanes) throws PWCGException
    {
        String planeTypeName = determinePlaneTypeToAdd(squadronId);
        PlaneEquipmentFactory equipmentFactory = new PlaneEquipmentFactory(campaign);
        EquippedPlane equippedPlane = equipmentFactory.makePlaneForDepot(planeTypeName);
        PWCGContext.getInstance().getPlaneMarkingManager().allocatePlaneIdCode(campaign, squadronId, squadronEquipment, equippedPlane);
        squadronEquipment.addEquippedPlane(equippedPlane);
    }

    private String determinePlaneTypeToAdd(int squadronId) throws PWCGException
    {
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronId);
        List<String> activeArchTypes = squadron.getActiveArchTypes(campaign.getDate());
        int archTypeIndex = RandomNumberGenerator.getRandom(activeArchTypes.size());
        
        PlaneArchType planeArchType = PWCGContext.getInstance().getPlaneTypeFactory().getPlaneArchType(activeArchTypes.get(archTypeIndex));
        String planeTypeName = EquipmentReplacementUtils.getTypeForReplacement(campaign.getDate(), planeArchType);
        return planeTypeName;
    }
}
