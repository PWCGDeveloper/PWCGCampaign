package pwcg.campaign.resupply.depot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.Equipment;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankArchType;
import pwcg.campaign.tank.TankEquipmentFactory;
import pwcg.campaign.tank.TankType;
import pwcg.core.exception.PWCGException;

public class EquipmentArchTypeChangeHandler 
{
    private Campaign campaign;
    private Date newDate;
    private Set<Company> squadronsToEquip = new HashSet<>();
	
	public EquipmentArchTypeChangeHandler(Campaign campaign, Date newDate)
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
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
        for (Company squadron : squadronManager.getActiveCompanies(campaign.getDate()))
        {
            Equipment squadronEquipment = campaign.getEquipmentManager().getEquipmentForCompany(squadron.getCompanyId());
            if (squadronEquipment != null)
            {
                Set<Integer> planesToRemove = new HashSet<>();
                for (EquippedTank plane : squadronEquipment.getActiveEquippedTanks().values())
                {
                    boolean isActiveArchType = squadron.isPlaneInActiveSquadronArchTypes(newDate, plane);
                    if (!isActiveArchType)
                    {
                        planesToRemove.add(plane.getSerialNumber());
                        squadronsToEquip.add(squadron);
                    }
                }

                for (Integer planeSerialNumber : planesToRemove)
                {
                    squadronEquipment.deactivateEquippedTankFromCompany(planeSerialNumber, campaign.getDate());
                }
            }            
        }
    }

    private void addNewArchTypes() throws PWCGException
    {
        for (Company squadron : squadronsToEquip)
        {
            Equipment squadronEquipment = campaign.getEquipmentManager().getEquipmentForCompany(squadron.getCompanyId());
            int numPlanesNeeded = Company.COMPANY_EQUIPMENT_SIZE - squadronEquipment.getActiveEquippedTanks().size();

            TankType bestTankType = getBestTankTypeForSquadron(squadron);
            for (int i = 0; i < numPlanesNeeded; ++i)
            {
                EquippedTank replacementPlane = TankEquipmentFactory.makeTankForSquadron(campaign, bestTankType.getType(), squadron.getCompanyId());
                squadronEquipment.addEquippedTankToCompany(campaign, squadron.getCompanyId(), replacementPlane);
            }
        }
    }
    
    private TankType getBestTankTypeForSquadron(Company squadron) throws PWCGException
    {
        List<TankType> planeTypesForSquadron = new ArrayList<>();
        for (TankArchType archType : squadron.determineCurrentAircraftArchTypes(newDate))
        {
            List<TankType> planeTypesForArchType = archType.getActiveMemberTankTypes(newDate);
            planeTypesForSquadron.addAll(planeTypesForArchType);
        }
        
        TankType bestTankType = planeTypesForSquadron.get(0);
        for (TankType planeType : planeTypesForSquadron)
        {
            if (planeType.getGoodness() > bestTankType.getGoodness())
            {
                bestTankType = planeType;
            }
        }
        
        return bestTankType;
    }
}
