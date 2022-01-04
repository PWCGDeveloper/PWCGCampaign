package pwcg.campaign.resupply;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.resupply.depot.EquipmentWeightCalculator;
import pwcg.campaign.tank.Equipment;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankArchType;
import pwcg.campaign.tank.TankEquipmentFactory;
import pwcg.campaign.tank.TankType;
import pwcg.core.exception.PWCGException;

public class InitialCompanyEquipper 
{
    private Campaign campaign;
    private Company squadron;
    private Equipment equipment = new Equipment();
    private int planesNeeded = Company.COMPANY_EQUIPMENT_SIZE;
    private EquipmentWeightCalculator equipmentWeightCalculator;

	public InitialCompanyEquipper(Campaign campaign, Company squadron, EquipmentWeightCalculator equipmentWeightCalculator) 
	{
        this.campaign = campaign;
        this.squadron = squadron;
        this.equipmentWeightCalculator = equipmentWeightCalculator;
	}

    public Equipment generateEquipment() throws PWCGException 
    {
        determinePlaneWeightsForSquadron();
        generatePlanesForSquadron();
        return equipment;
    }
    
    private void determinePlaneWeightsForSquadron() throws PWCGException
    {
        List<TankArchType> currentAircraftArchTypes = squadron.determineCurrentAircraftArchTypes(campaign.getDate());
        
        List<TankType> planeTypesInSquadron = new ArrayList<>();
        for (TankArchType planeArchType : currentAircraftArchTypes)
        {
            List<TankType> planeTypesForArchType = PWCGContext.getInstance().getTankTypeFactory().createActiveTankTypesForArchType(planeArchType.getTankArchTypeName(), campaign.getDate());
            planeTypesInSquadron.addAll(planeTypesForArchType);
        }
        
        equipmentWeightCalculator.determinePlaneWeightsForPlanes(planeTypesInSquadron);
    }

    private void generatePlanesForSquadron() throws PWCGException 
    {       
        
        for (int i = 0; i < planesNeeded; ++i)
        {
            String planeTypeName = equipmentWeightCalculator.getTankTypeFromWeight();
            
            EquippedTank equippedPlane = TankEquipmentFactory.makeTankForSquadron(campaign, planeTypeName, squadron.getCompanyId());
            equipment.addEquippedTankToCompany(campaign, squadron.getCompanyId(), equippedPlane);
        }
    }

}
