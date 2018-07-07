package pwcg.aar.campaign.update;

import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.plane.EquipmentReplacement;
import pwcg.campaign.plane.EquipmentWeightCalculator;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneArchType;
import pwcg.campaign.plane.PlaneEquipmentFactory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class CampaignEquipmentReplacementUpdater 
{
	private Campaign campaign;
	
	public CampaignEquipmentReplacementUpdater(Campaign campaign)
	{
		this.campaign = campaign;
	}
	
	public void updateCampaignEquipmentReplacements () throws PWCGException
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
            EquipmentReplacement replaceMentEquipmentForService = campaign.getEquipmentManager().getEquipmentReplacementsForService(service.getServiceId());
            addReplacementPlanes(service, squadronsForService, equipmentFactory, replaceMentEquipmentForService);
        }
        else
        {
            System.out.println("No squadrons for service " + service.getName());
        }
    }

    private void addReplacementPlanes(
            ArmedService service, 
            List<Squadron> squadronsForService, 
            PlaneEquipmentFactory equipmentFactory,
            EquipmentReplacement replaceMentEquipmentForService) throws PWCGException
    {
        int numPlanes = replaceMentEquipmentForService.getEquipmentPoints() / 10;
        for (int i = 0; i < numPlanes; ++i)
        {
            PlaneArchType planeArchType = getArchTypeForReplacement(service, squadronsForService);
            String planeTypeName = getTypeForReplacement(planeArchType);
            EquippedPlane equippedPlane = equipmentFactory.makePlaneForDepo(planeTypeName);
            replaceMentEquipmentForService.getEquipment().addEquippedPlane(equippedPlane);
        }
        
        int newPoints = service.getDailyEquipmentReplacementRate();
        int remainingPoints = replaceMentEquipmentForService.getEquipmentPoints() % 10;
        int updatedEquipmentPoints = newPoints + remainingPoints;
        replaceMentEquipmentForService.setEquipmentPoints(updatedEquipmentPoints);
    }

    private PlaneArchType getArchTypeForReplacement(ArmedService service, List<Squadron> squadronsForService) throws PWCGException
    {        
        EquipmentArchtypeFinder equipmentArchtypeReplacementFinder = new EquipmentArchtypeFinder(campaign);
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

    private List<Squadron> getSquadronsForService(ArmedService service) throws PWCGException 
    {
        SquadronManager squadronManager = PWCGContextManager.getInstance().getSquadronManager();
        return squadronManager.getActiveSquadronsForService(campaign.getDate(), service);
    }
}
