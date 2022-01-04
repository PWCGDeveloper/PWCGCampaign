package pwcg.campaign.resupply.depot;

import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.Equipment;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankEquipmentFactory;
import pwcg.campaign.tank.TankType;
import pwcg.core.exception.PWCGException;

public class EquipmentDepotInitializer
{
    private Campaign campaign;
    private ArmedService service;
    private Equipment equipment = new Equipment();

    public EquipmentDepotInitializer(Campaign campaign, ArmedService service) 
    {
        this.campaign = campaign;
        this.service = service;
    }

    public Equipment createReplacementPoolForService() throws PWCGException
    {
        List<Company> activeSquadronsForService = PWCGContext.getInstance().getCompanyManager().getActiveCompaniesForService(campaign.getDate(), service);
        for (Company squadron : activeSquadronsForService)
        {
            EquipmentWeightCalculator equipmentWeightCalculator = createPlaneCalculator(squadron);            
            makeReplacementPlanesForSquadron(equipmentWeightCalculator);
        }
        return equipment;
    }

    private EquipmentWeightCalculator createPlaneCalculator(Company squadron) throws PWCGException
    {
        List<TankType> planeTypesForSquadron = squadron.determineCurrentAircraftList(campaign.getDate());
        EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(campaign.getDate());
        equipmentWeightCalculator.determinePlaneWeightsForPlanes(planeTypesForSquadron);
        return equipmentWeightCalculator;
    }

    private void makeReplacementPlanesForSquadron(EquipmentWeightCalculator equipmentWeightCalculator) throws PWCGException
    {
        int numPlanes = service.getDailyEquipmentReplacementRate(campaign.getDate()) / EquipmentDepot.NUM_POINTS_PER_PLANE;
        if (numPlanes < 1)
        {
            numPlanes = 1;
        }
        
        for (int i = 0; i < numPlanes; ++i)
        {
            String planeTypeName = equipmentWeightCalculator.getTankTypeFromWeight();
            EquippedTank equippedPlane = TankEquipmentFactory.makePlaneForDepot(campaign, planeTypeName);
            equipment.addEPlaneToDepot(equippedPlane);
        }
    }
}
