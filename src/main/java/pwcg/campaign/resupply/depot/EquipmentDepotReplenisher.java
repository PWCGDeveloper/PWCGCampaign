package pwcg.campaign.resupply.depot;

import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneArchType;
import pwcg.campaign.plane.PlaneEquipmentFactory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class EquipmentDepotReplenisher
{
	private Campaign campaign;
	
	public EquipmentDepotReplenisher(Campaign campaign)
	{
		this.campaign = campaign;
	}
	
	public void replenishDepotsForServices () throws PWCGException
	{
        for (Integer serviceId : campaign.getEquipmentManager().getServiceIdsForDepots())
		{
	        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(serviceId);
	        replenishReplacementDepotForService(service);
		}
	}

    private void replenishReplacementDepotForService(ArmedService service) throws PWCGException
    {
        List<Squadron> squadronsForService = getSquadronsForService(service);
        if (squadronsForService.size() > 0)
        {
            PlaneEquipmentFactory equipmentFactory = new PlaneEquipmentFactory(campaign);
            EquipmentDepot depo = campaign.getEquipmentManager().getEquipmentDepotForService(service.getServiceId());
            addReplacementPlanesForService(service, squadronsForService, equipmentFactory, depo);
        }
        else
        {
            // System.out.println("No squadrons for service " + service.getName());
        }
    }

    private List<Squadron> getSquadronsForService(ArmedService service) throws PWCGException 
    {
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        return squadronManager.getActiveSquadronsForService(campaign.getDate(), service);
    }

    private void addReplacementPlanesForService(
            ArmedService service, 
            List<Squadron> squadronsForService, 
            PlaneEquipmentFactory equipmentFactory,
            EquipmentDepot depot) throws PWCGException
    {
        replacePlanesInDepot(squadronsForService, equipmentFactory, depot);        
        updatePlaneReplacementPoints(service, depot);
    }

    private void updatePlaneReplacementPoints(ArmedService service, EquipmentDepot depot)
    {
        int newPoints = service.getDailyEquipmentReplacementRate();
        int remainingPoints = depot.getEquipmentPoints() % 10;
        int updatedEquipmentPoints = newPoints + remainingPoints;
        depot.setEquipmentPoints(updatedEquipmentPoints);
    }

    private void replacePlanesInDepot(List<Squadron> squadronsForService, PlaneEquipmentFactory equipmentFactory, EquipmentDepot equipmentDepot) throws PWCGException
    {
        EquipmentReplacementCalculator equipmentReplacementCalculator = new EquipmentReplacementCalculator(campaign);
        equipmentReplacementCalculator.createArchTypeForReplacementPlane(squadronsForService);

        int numPlanes = equipmentDepot.getEquipmentPoints() / 10;
        for (int i = 0; i < numPlanes; ++i)
        {
            PlaneArchType planeArchType = getArchTypeForReplacement(equipmentReplacementCalculator);
            String planeTypeName = EquipmentReplacementUtils.getTypeForReplacement(campaign.getDate(), planeArchType);
            EquippedPlane equippedPlane = equipmentFactory.makePlaneForDepot(planeTypeName);
            equipmentDepot.addPlaneToDepot(equippedPlane);
            equipmentDepot.setLastReplacementDate(campaign.getDate());
        }
    }

    private PlaneArchType getArchTypeForReplacement(EquipmentReplacementCalculator equipmentReplacementCalculator) throws PWCGException
    {
        String archTypeForReplacementPlane = "";
        if (equipmentReplacementCalculator.hasMoreForReplacement()) 
        {
            archTypeForReplacementPlane = equipmentReplacementCalculator.chooseArchTypeForReplacementByNeed();
        }
        else
        {
            archTypeForReplacementPlane = equipmentReplacementCalculator.chooseArchTypeForReplacementByUsage();
        }

        PlaneArchType planeArchType = PWCGContext.getInstance().getPlaneTypeFactory().getPlaneArchType(archTypeForReplacementPlane);
        return planeArchType;
    }
}
