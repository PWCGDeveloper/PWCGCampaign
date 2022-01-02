package pwcg.campaign.resupply.depot;

import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneArchType;
import pwcg.campaign.plane.PlaneEquipmentFactory;
import pwcg.campaign.squadron.Company;
import pwcg.campaign.squadron.SquadronManager;
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
        List<Company> squadronsForService = getSquadronsForService(service);
        if (squadronsForService.size() > 0)
        {
            EquipmentDepot depo = campaign.getEquipmentManager().getEquipmentDepotForService(service.getServiceId());
            addReplacementPlanesForService(service, squadronsForService, depo);
        }
    }

    private List<Company> getSquadronsForService(ArmedService service) throws PWCGException 
    {
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        return squadronManager.getActiveSquadronsForService(campaign.getDate(), service);
    }

    private void addReplacementPlanesForService(
            ArmedService service, 
            List<Company> squadronsForService, 
            EquipmentDepot depot) throws PWCGException
    {
        replacePlanesInDepot(squadronsForService, depot);        
        updatePlaneReplacementPoints(service, depot);
    }

    private void updatePlaneReplacementPoints(ArmedService service, EquipmentDepot depot) throws PWCGException
    {
        int newPoints = service.getDailyEquipmentReplacementRate(campaign.getDate());
        int remainingPoints = depot.getEquipmentPoints() % EquipmentDepot.NUM_POINTS_PER_PLANE;
        int updatedEquipmentPoints = newPoints + remainingPoints;
        depot.setEquipmentPoints(updatedEquipmentPoints);
    }

    private void replacePlanesInDepot(List<Company> squadronsForService, EquipmentDepot equipmentDepot) throws PWCGException
    {
        EquipmentReplacementCalculator equipmentReplacementCalculator = new EquipmentReplacementCalculator(campaign);
        equipmentReplacementCalculator.createArchTypeForReplacementPlane(squadronsForService);

        int numPlanes = equipmentDepot.getEquipmentPoints() / EquipmentDepot.NUM_POINTS_PER_PLANE;
        for (int i = 0; i < numPlanes; ++i)
        {
            PlaneArchType planeArchType = getArchTypeForReplacement(equipmentReplacementCalculator);
            replacePlaneByArchType(equipmentDepot, planeArchType);
        }
    }

    private void replacePlaneByArchType(EquipmentDepot equipmentDepot, PlaneArchType planeArchType) throws PWCGException
    {
        String planeTypeName = EquipmentReplacementUtils.getTypeForReplacement(campaign.getDate(), planeArchType);
        EquippedPlane equippedPlane = PlaneEquipmentFactory.makePlaneForDepot(campaign, planeTypeName);
        equipmentDepot.addPlaneToDepot(equippedPlane);
        equipmentDepot.setLastReplacementDate(campaign.getDate());
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
