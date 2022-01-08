package pwcg.campaign;

import java.util.List;

import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.io.json.CampaignEquipmentIOJson;
import pwcg.campaign.io.json.CampaignPersonnelIOJson;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.campaign.personnel.CrewMemberReplacementFactory;
import pwcg.campaign.resupply.depot.EquipmentReplacementUtils;
import pwcg.campaign.tank.Equipment;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankArchType;
import pwcg.campaign.tank.TankEquipmentFactory;
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
        CampaignPersonnelIOJson.writeSquadron(campaign, squadronPersonnel.getSquadron().getCompanyId());
    }

    private int calculatePersonnelToReplaceForSquadron(CompanyPersonnel squadronPersonnel) throws PWCGException
    {
        CrewMembers activeCrewMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
        int activeSquadronSize = activeCrewMembers.getActiveCount(campaign.getDate());
        int transfersNeededForFull = Company.COMPANY_STAFF_SIZE -  activeSquadronSize;
        int transfersNeededForViable = (Company.COMPANY_STAFF_SIZE / 2) -  activeSquadronSize;
        
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
            replacement.setSquadronId(squadronPersonnel.getSquadron().getCompanyId());
            squadronPersonnel.addCrewMember(replacement);
        }
    }

    private void emergencyResupplyEquipment() throws PWCGException
    {
        for (int squadronId : campaign.getEquipmentManager().getEquipmentAllCompanies().keySet())
        {
            Equipment squadronEquipment = campaign.getEquipmentManager().getEquipmentForCompany(squadronId);
            if (!squadronEquipment.isCompanyEquipmentViable())
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
        int activeSquadronEquipment = squadronEquipment.getActiveEquippedTanks().size();
        int equipmentNeededForFull = Company.COMPANY_EQUIPMENT_SIZE -  activeSquadronEquipment;
        int equipmentNeededForViable = (Company.COMPANY_EQUIPMENT_SIZE / 2) -  activeSquadronEquipment;
        
        int equipmentNeededForViableWithCushion = equipmentNeededForViable + 2;
        int extraEquipment = RandomNumberGenerator.getRandom(equipmentNeededForFull - equipmentNeededForViableWithCushion);
        int totalEquipment = equipmentNeededForViableWithCushion + extraEquipment;
        return totalEquipment;
    }

    private void replaceEquipmentForSquadron(Equipment squadronEquipment, int squadronId, int totalNewPlanes) throws PWCGException
    {
        String planeTypeName = determineTankTypeToAdd(squadronId);
        EquippedTank equippedPlane = TankEquipmentFactory.makePlaneForDepot(campaign, planeTypeName);
        squadronEquipment.addEquippedTankToCompany(campaign, squadronId, equippedPlane);
    }

    private String determineTankTypeToAdd(int squadronId) throws PWCGException
    {
        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(squadronId);
        List<String> activeArchTypes = squadron.getActiveArchTypes(campaign.getDate());
        int archTypeIndex = RandomNumberGenerator.getRandom(activeArchTypes.size());
        
        TankArchType planeArchType = PWCGContext.getInstance().getTankTypeFactory().getTankArchType(activeArchTypes.get(archTypeIndex));
        String planeTypeName = EquipmentReplacementUtils.getTypeForReplacement(campaign.getDate(), planeArchType);
        return planeTypeName;
    }
}
