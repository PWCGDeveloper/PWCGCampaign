package pwcg.campaign.resupply.depo;

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
            addReplacementPlanesForService(service, squadronsForService, equipmentFactory, depo);
        }
        else
        {
            System.out.println("No squadrons for service " + service.getName());
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
            EquipmentDepo depo) throws PWCGException
    {
        replacePlanesInDepo(squadronsForService, equipmentFactory, depo);        
        updatePlaneReplacementPoints(service, depo);
    }

    private void updatePlaneReplacementPoints(ArmedService service, EquipmentDepo depo)
    {
        int newPoints = service.getDailyEquipmentReplacementRate();
        int remainingPoints = depo.getEquipmentPoints() % 10;
        int updatedEquipmentPoints = newPoints + remainingPoints;
        depo.setEquipmentPoints(updatedEquipmentPoints);
    }

    private void replacePlanesInDepo(List<Squadron> squadronsForService, PlaneEquipmentFactory equipmentFactory, EquipmentDepo depo) throws PWCGException
    {
        EquipmentReplacementCalculator equipmentReplacementCalculator = new EquipmentReplacementCalculator(campaign);
        equipmentReplacementCalculator.createArchTypeForReplacementPlane(squadronsForService);

        int numPlanes = depo.getEquipmentPoints() / 10;
        for (int i = 0; i < numPlanes; ++i)
        {
            PlaneArchType planeArchType = getArchTypeForReplacement(equipmentReplacementCalculator);
            String planeTypeName = EquipmentReplacementUtils.getTypeForReplacement(campaign.getDate(), planeArchType);
            EquippedPlane equippedPlane = equipmentFactory.makePlaneForDepo(planeTypeName);
            depo.getEquipment().addEquippedPlane(equippedPlane);
            depo.setLastReplacementDate(campaign.getDate());
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
