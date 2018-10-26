package pwcg.campaign.resupply.depo;

import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneArchType;
import pwcg.campaign.plane.PlaneEquipmentFactory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class EquipmentDepoReplenisher
{
	private Campaign campaign;
	
	public EquipmentDepoReplenisher(Campaign campaign)
	{
		this.campaign = campaign;
	}
	
	public void replenishDeposForServices () throws PWCGException
	{
		for (Integer serviceId : campaign.getEquipmentManager().getEquipmentReplacements().keySet())
		{
	        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(serviceId);
	        replenishReplacementDepoForService(service);
		}
	}

    private void replenishReplacementDepoForService(ArmedService service) throws PWCGException
    {
        List<Squadron> squadronsForService = getSquadronsForService(service);
        if (squadronsForService.size() > 0)
        {
            PlaneEquipmentFactory equipmentFactory = new PlaneEquipmentFactory(campaign);
            EquipmentDepo depo = campaign.getEquipmentManager().getEquipmentReplacementsForService(service.getServiceId());
            addReplacementPlanes(service, squadronsForService, equipmentFactory, depo);
        }
        else
        {
            System.out.println("No squadrons for service " + service.getName());
        }
    }

    private List<Squadron> getSquadronsForService(ArmedService service) throws PWCGException 
    {
        SquadronManager squadronManager = PWCGContextManager.getInstance().getSquadronManager();
        return squadronManager.getActiveSquadronsForService(campaign.getDate(), service);
    }

    private void addReplacementPlanes(
            ArmedService service, 
            List<Squadron> squadronsForService, 
            PlaneEquipmentFactory equipmentFactory,
            EquipmentDepo depo) throws PWCGException
    {
        int numPlanes = depo.getEquipmentPoints() / 10;
        for (int i = 0; i < numPlanes; ++i)
        {
            PlaneArchType planeArchType = getArchTypeForReplacement(service, squadronsForService);
            String planeTypeName = getTypeForReplacement(planeArchType);
            EquippedPlane equippedPlane = equipmentFactory.makePlaneForDepo(planeTypeName);
            depo.getEquipment().addEquippedPlane(equippedPlane);
            depo.setLastReplacementDate(campaign.getDate());
        }
        
        int newPoints = service.getDailyEquipmentReplacementRate();
        int remainingPoints = depo.getEquipmentPoints() % 10;
        int updatedEquipmentPoints = newPoints + remainingPoints;
        depo.setEquipmentPoints(updatedEquipmentPoints);
    }

    private PlaneArchType getArchTypeForReplacement(ArmedService service, List<Squadron> squadronsForService) throws PWCGException
    {        
        EquipmentArchTypeFinder equipmentArchtypeReplacementFinder = new EquipmentArchTypeFinder(campaign);
        String archTypeForReplacementPlane = equipmentArchtypeReplacementFinder.getArchTypeForReplacementPlane(squadronsForService);
        PlaneArchType planeArchType = PWCGContextManager.getInstance().getPlaneTypeFactory().getPlaneArchType(archTypeForReplacementPlane);
        return planeArchType;
    }

    private String getTypeForReplacement(PlaneArchType planeArchType) throws PWCGException
    {
        EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(campaign);
        equipmentWeightCalculator.determinePlaneWeightsForPlanes(planeArchType.getActiveMemberPlaneTypes(campaign.getDate()));
        String planeTypeName = equipmentWeightCalculator.getPlaneTypeFromWeight();
        return planeTypeName;
    }
}
