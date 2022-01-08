package pwcg.campaign;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.resupply.depot.EquipmentDepot;
import pwcg.campaign.resupply.depot.EquipmentDepotInitializer;
import pwcg.campaign.tank.Equipment;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankEquipmentFactory;
import pwcg.campaign.tank.TankStatus;
import pwcg.campaign.tank.TankType;
import pwcg.campaign.tank.TankTypeFactory;
import pwcg.core.exception.PWCGException;

public class CampaignEquipmentManager
{
    private Campaign campaign;
    private Map<Integer, Equipment> equipmentAllCompanies = new HashMap<>();
    private Map<Integer, EquipmentDepot> equipmentDepotsForServices = new HashMap<>();

    public CampaignEquipmentManager(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public Equipment getEquipmentForCompany(Integer companyId)
    {
        return equipmentAllCompanies.get(companyId);
    }
    
    public boolean hasEquipmentDepo (int serviceId)
    {
        return equipmentDepotsForServices.containsKey(serviceId);
    }

    public EquipmentDepot getEquipmentDepotForService(Integer serviceId) throws PWCGException
    {
        if (!equipmentDepotsForServices.containsKey(serviceId))
        {
            ArmedService armedService = ArmedServiceFactory.createServiceManager().getArmedServiceById(serviceId, campaign.getDate());
            createEquipmentDepot(armedService);
        }
        return equipmentDepotsForServices.get(serviceId);
    }

    public void addEquipmentForCompany(Integer companyId, Equipment equipmentForCompany)
    {
        equipmentAllCompanies.put(companyId, equipmentForCompany);
    }

    public void addEquipmentDepotForService(Integer serviceId, EquipmentDepot replacementEquipmentForService)
    {
        equipmentDepotsForServices.put(serviceId, replacementEquipmentForService);
    }

    public Map<Integer, Equipment> getEquipmentAllCompanies()
    {
        return equipmentAllCompanies;
    }
    
    public List<Integer> getServiceIdsForDepots()
    {
        return new ArrayList<Integer>(equipmentDepotsForServices.keySet());
    }
    
    public EquippedTank getAnyTankWithPreference(Integer serialNumber) throws PWCGException
    {
        EquippedTank equippedTank = getTankFromAnyCompany(serialNumber);
        if (equippedTank == null)
        {
            equippedTank = getTankFromAnyDepo(serialNumber);
        }
        
        if (equippedTank == null)
        {
            throw new PWCGException ("Unable to locate equipped tank for serial number anywhere" + serialNumber);
        }
        
        return equippedTank;
    }

    private EquippedTank getTankFromAnyCompany(Integer serialNumber) throws PWCGException
    {
        for (Equipment equipment : equipmentAllCompanies.values())
        {
            EquippedTank equippedTank = equipment.getEquippedTank(serialNumber);
            if (equippedTank != null)
            {
                return equippedTank;
            }        
        }
        
        return null;
    }

    private EquippedTank getTankFromAnyDepo(Integer serialNumber) throws PWCGException
    {
        for (EquipmentDepot equipmentDepot : equipmentDepotsForServices.values())
        {
            EquippedTank equippedTank = equipmentDepot.getAnyTankInDepot(serialNumber);
            if (equippedTank != null)
            {
                return equippedTank;
            }
        }
        return null;
    }

    public EquippedTank getAnyActiveTankFromCompany(Integer companyId) throws PWCGException
    {
        Equipment equipment = equipmentAllCompanies.get(companyId);
        for (EquippedTank equippedTank : equipment.getActiveEquippedTanks().values())
        {
            return equippedTank;
        }

        throw new PWCGException ("Unable to locate active equipped tank for company " + companyId);
    }

    public EquippedTank destroyTankFromCompany(int companyId, Date date) throws PWCGException
    {
        EquippedTank destroyedPlane = getAnyActiveTankFromCompany(companyId);
        destroyedPlane.setPlaneStatus(TankStatus.STATUS_DESTROYED);
        destroyedPlane.setDateRemovedFromService(date);
        return destroyedPlane;
    }

    public EquippedTank destroyTank(int serialNumber, Date date) throws PWCGException
    {
        EquippedTank destroyedPlane = getAnyTankWithPreference(serialNumber);
        destroyedPlane.setPlaneStatus(TankStatus.STATUS_DESTROYED);
        destroyedPlane.setDateRemovedFromService(date);
        return destroyedPlane;
    }
    

    public int getReplacementCount() throws PWCGException
    {
        int replacementCount = 0;
        for (EquipmentDepot replacementService : equipmentDepotsForServices.values())
        {
            replacementCount += replacementService.getDepotSize();
        }
        
        return replacementCount;
    }
    
    
    public void createEquipmentDepot(ArmedService armedService) throws PWCGException
    {
        EquipmentDepotInitializer depotInitializer = new EquipmentDepotInitializer(campaign, armedService);
        Equipment equipment = depotInitializer.createReplacementPoolForService();
        EquipmentDepot depot = new EquipmentDepot();
        depot.setEquipmentPoints(armedService.getDailyEquipmentReplacementRate(campaign.getDate()) * 2);
        depot.setLastReplacementDate(campaign.getDate());
        depot.setEquippment(equipment);
        campaign.getEquipmentManager().addEquipmentDepotForService(armedService.getServiceId(), depot);
    }


    public void actOnEquipmentRequest(Company company, List<Integer> serialNumbersOfChangedPlanes, String tankTypeToChangeTo) throws PWCGException
    {
        for (int serialNumber : serialNumbersOfChangedPlanes)
        {
            this.destroyTank(serialNumber, campaign.getDate());
        }
        
        Equipment companyEquipment = equipmentAllCompanies.get(company.getCompanyId());
        for (int i = 0; i < serialNumbersOfChangedPlanes.size(); ++i)
        {
            TankTypeFactory tankTypeFactory = PWCGContext.getInstance().getTankTypeFactory();
            TankType tankType = tankTypeFactory.getPlaneByDisplayName(tankTypeToChangeTo);
            EquippedTank equippedTank = TankEquipmentFactory.makeTankForSquadron(campaign, tankType.getType(), company.getCompanyId());
            equippedTank.setEquipmentRequest(true);
            companyEquipment.addEquippedTankToCompany(campaign, company.getCompanyId(), equippedTank);
        }
    }

}
