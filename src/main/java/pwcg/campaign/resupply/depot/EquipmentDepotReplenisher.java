package pwcg.campaign.resupply.depot;

import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankArchType;
import pwcg.campaign.tank.TankEquipmentFactory;
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
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
        return squadronManager.getActiveCompaniesForService(campaign.getDate(), service);
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
            TankArchType planeArchType = getArchTypeForReplacement(equipmentReplacementCalculator);
            replacePlaneByArchType(equipmentDepot, planeArchType);
        }
    }

    private void replacePlaneByArchType(EquipmentDepot equipmentDepot, TankArchType planeArchType) throws PWCGException
    {
        String planeTypeName = EquipmentReplacementUtils.getTypeForReplacement(campaign.getDate(), planeArchType);
        EquippedTank equippedPlane = TankEquipmentFactory.makePlaneForDepot(campaign, planeTypeName);
        equipmentDepot.addPlaneToDepot(equippedPlane);
        equipmentDepot.setLastReplacementDate(campaign.getDate());
    }

    private TankArchType getArchTypeForReplacement(EquipmentReplacementCalculator equipmentReplacementCalculator) throws PWCGException
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

        TankArchType planeArchType = PWCGContext.getInstance().getTankTypeFactory().getTankArchType(archTypeForReplacementPlane);
        return planeArchType;
    }
}
