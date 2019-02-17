package pwcg.campaign.resupply.depo;

import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneEquipmentFactory;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class EquipmentDepoInitializer
{
    private Campaign campaign;
    private ArmedService service;
    private Equipment equipment = new Equipment();
    private PlaneEquipmentFactory equipmentFactory;

    public EquipmentDepoInitializer(Campaign campaign, ArmedService service) 
    {
        this.campaign = campaign;
        this.service = service;
        this.equipmentFactory = new PlaneEquipmentFactory(campaign);
    }

    public Equipment createReplacementPoolForService() throws PWCGException
    {
        List<Squadron> activeSquadronsForService = PWCGContextManager.getInstance().getSquadronManager().getFlyableSquadronsByService(service, campaign.getDate());
        for (Squadron squadron : activeSquadronsForService)
        {
            EquipmentWeightCalculator equipmentWeightCalculator = createPlaneCalculator(squadron);            
            makeReplacementPlanesForSquadron(equipmentWeightCalculator);
        }
        return equipment;
    }

    private EquipmentWeightCalculator createPlaneCalculator(Squadron squadron) throws PWCGException
    {
        List<PlaneType> planeTypesForSquadron = squadron.determineCurrentAircraftList(campaign.getDate());
        EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(campaign.getDate());
        equipmentWeightCalculator.determinePlaneWeightsForPlanes(planeTypesForSquadron);
        return equipmentWeightCalculator;
    }

    private void makeReplacementPlanesForSquadron(EquipmentWeightCalculator equipmentWeightCalculator) throws PWCGException
    {
        int numPlanes = service.getDailyEquipmentReplacementRate() / 10;
        if (numPlanes < 1)
        {
            numPlanes = 1;
        }
        
        for (int i = 0; i < numPlanes; ++i)
        {
            String planeTypeName = equipmentWeightCalculator.getPlaneTypeFromWeight();
            EquippedPlane equippedPlane = equipmentFactory.makePlaneForDepo(planeTypeName);
            equipment.addEquippedPlane(equippedPlane);
        }
    }
}
