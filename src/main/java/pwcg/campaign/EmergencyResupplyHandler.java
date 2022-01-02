package pwcg.campaign;

import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.io.json.CampaignEquipmentIOJson;
import pwcg.campaign.io.json.CampaignPersonnelIOJson;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.CrewMemberFemaleGenerator;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.campaign.personnel.CrewMemberReplacementFactory;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneArchType;
import pwcg.campaign.plane.PlaneEquipmentFactory;
import pwcg.campaign.resupply.depot.EquipmentReplacementUtils;
import pwcg.campaign.squadron.Company;
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
        for (CompanyPersonnel squadronPersonnel : campaign.getPersonnelManager().getCampaignPersonnel().values())
        {
            if (!squadronPersonnel.isSquadronPersonnelViable())
            {
                makeSquadronPersonnelViable(squadronPersonnel);
            }
        }
    }

    private void makeSquadronPersonnelViable(CompanyPersonnel squadronPersonnel) throws PWCGException
    {
        int totalTransfers = calculatePersonnelToReplaceForSquadron(squadronPersonnel);
        replacePersonnelForSquadron(squadronPersonnel, totalTransfers);
        CampaignPersonnelIOJson.writeSquadron(campaign, squadronPersonnel.getSquadron().getSquadronId());
    }

    private int calculatePersonnelToReplaceForSquadron(CompanyPersonnel squadronPersonnel) throws PWCGException
    {
        CrewMembers activeCrewMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
        int activeSquadronSize = activeCrewMembers.getActiveCount(campaign.getDate());
        int transfersNeededForFull = Company.SQUADRON_STAFF_SIZE -  activeSquadronSize;
        int transfersNeededForViable = (Company.SQUADRON_STAFF_SIZE / 2) -  activeSquadronSize;
        
        int transfersNeededForViableWithCushion = transfersNeededForViable + 2;
        int extraTransfers = RandomNumberGenerator.getRandom(transfersNeededForFull - transfersNeededForViableWithCushion);
        int totalTransfers = transfersNeededForViableWithCushion + extraTransfers;
        return totalTransfers;
    }

    private void replacePersonnelForSquadron(CompanyPersonnel squadronPersonnel, int totalTransfers) throws PWCGException
    {
        CrewMemberReplacementFactory replacementFactory = new CrewMemberReplacementFactory(
                campaign, squadronPersonnel.getSquadron().determineServiceForSquadron(campaign.getDate()));
        
        for (int i = 0; i < totalTransfers; ++i)
        {
            CrewMember replacement = replacementFactory.createAIReplacementCrewMember();
            replacement.setSquadronId(squadronPersonnel.getSquadron().getSquadronId());
            CrewMember convertedReplacement = CrewMemberFemaleGenerator.convertToFemale(campaign, squadronPersonnel.getSquadron().getSquadronId(), replacement);
            squadronPersonnel.addCrewMember(convertedReplacement);
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
        int equipmentNeededForFull = Company.SQUADRON_EQUIPMENT_SIZE -  activeSquadronEquipment;
        int equipmentNeededForViable = (Company.SQUADRON_EQUIPMENT_SIZE / 2) -  activeSquadronEquipment;
        
        int equipmentNeededForViableWithCushion = equipmentNeededForViable + 2;
        int extraEquipment = RandomNumberGenerator.getRandom(equipmentNeededForFull - equipmentNeededForViableWithCushion);
        int totalEquipment = equipmentNeededForViableWithCushion + extraEquipment;
        return totalEquipment;
    }

    private void replaceEquipmentForSquadron(Equipment squadronEquipment, int squadronId, int totalNewPlanes) throws PWCGException
    {
        String planeTypeName = determinePlaneTypeToAdd(squadronId);
        EquippedPlane equippedPlane = PlaneEquipmentFactory.makePlaneForDepot(campaign, planeTypeName);
        squadronEquipment.addEquippedPlaneToSquadron(campaign, squadronId, equippedPlane);
    }

    private String determinePlaneTypeToAdd(int squadronId) throws PWCGException
    {
        Company squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronId);
        List<String> activeArchTypes = squadron.getActiveArchTypes(campaign.getDate());
        int archTypeIndex = RandomNumberGenerator.getRandom(activeArchTypes.size());
        
        PlaneArchType planeArchType = PWCGContext.getInstance().getPlaneTypeFactory().getPlaneArchType(activeArchTypes.get(archTypeIndex));
        String planeTypeName = EquipmentReplacementUtils.getTypeForReplacement(campaign.getDate(), planeArchType);
        return planeTypeName;
    }
}
