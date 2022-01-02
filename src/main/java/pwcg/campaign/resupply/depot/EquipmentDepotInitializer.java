package pwcg.campaign.resupply.depot;

import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneEquipmentFactory;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.squadron.Company;
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
        List<Company> activeSquadronsForService = PWCGContext.getInstance().getSquadronManager().getActiveSquadronsForService(campaign.getDate(), service);
        for (Company squadron : activeSquadronsForService)
        {
            EquipmentWeightCalculator equipmentWeightCalculator = createPlaneCalculator(squadron);            
            makeReplacementPlanesForSquadron(equipmentWeightCalculator);
        }
        return equipment;
    }

    private EquipmentWeightCalculator createPlaneCalculator(Company squadron) throws PWCGException
    {
        List<PlaneType> planeTypesForSquadron = squadron.determineCurrentAircraftList(campaign.getDate());
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
            String planeTypeName = equipmentWeightCalculator.getPlaneTypeFromWeight();
            EquippedPlane equippedPlane = PlaneEquipmentFactory.makePlaneForDepot(campaign, planeTypeName);
            equipment.addEPlaneToDepot(equippedPlane);
        }
    }
}
