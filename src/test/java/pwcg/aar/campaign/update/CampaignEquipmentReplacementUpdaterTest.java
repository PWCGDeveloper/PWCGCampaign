package pwcg.aar.campaign.update;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.plane.PlaneArchType;
import pwcg.campaign.plane.SquadronPlaneAssignment;
import pwcg.campaign.resupply.depo.EquipmentDepo;
import pwcg.campaign.resupply.depo.EquipmentDepoReplenisher;
import pwcg.campaign.resupply.depo.EquipmentReplacementUtils;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadrontTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class CampaignEquipmentReplacementUpdaterTest 
{
	private Campaign campaign;
	
    @Before
    public void setup() throws PWCGException
    {
    	PWCGContextManager.setRoF(false);
        campaign = CampaignCache.makeCampaignForceCreation(SquadrontTestProfile.KG53_PROFILE);
    }
    
    @Test
    public void testArchTypesInProductionForServiceLife() throws PWCGException 
    {
        SquadronManager squadronmanager = PWCGContextManager.getInstance().getSquadronManager();
        for (Squadron squadron: squadronmanager.getAllSquadrons())
        {
            for (SquadronPlaneAssignment planeAssignment: squadron.getPlaneAssignments())
            {
                PlaneArchType planeArchType = PWCGContextManager.getInstance().getPlaneTypeFactory().getPlaneArchType(planeAssignment.getArchType());
                String selectedPlaneType = EquipmentReplacementUtils.getTypeForReplacement(planeAssignment.getSquadronWithdrawal(), planeArchType);
                assert(selectedPlaneType.length() > 0);
            }
        }
    }

    @Test
    public void testUpdateWithReplacementsGermans() throws PWCGException 
    {
        Map<Integer, Integer> replacementsAvailableBefore = determineReplacementsAvailableByService();
        EquipmentDepoReplenisher equipmentReplacementUpdater  = new EquipmentDepoReplenisher(campaign);
        equipmentReplacementUpdater.replenishDeposForServices();
        Map<Integer, Integer> replacementsAvailableAfter = determineReplacementsAvailableByService();
        validateReplacements(replacementsAvailableBefore, replacementsAvailableAfter);
    }

    private Map<Integer, Integer> determineReplacementsAvailableByService()
    {
        Map<Integer, Integer> replacementsAvailable = new HashMap<>();
        for (Integer serviceId : campaign.getEquipmentManager().getEquipmentReplacements().keySet())
        {
            EquipmentDepo replacementEquipmentForService = campaign.getEquipmentManager().getEquipmentReplacementsForService(serviceId);
            replacementsAvailable.put(serviceId, replacementEquipmentForService.getEquipment().getAvailableDepoPlanes().size());
        }
        return replacementsAvailable;
    }
    
    private void validateReplacements(Map<Integer, Integer> replacementsAvailableBefore, Map<Integer, Integer> replacementsAvailableAfter) throws PWCGException
    {
        for (Integer serviceId : replacementsAvailableBefore.keySet())
        {
            assert(replacementsAvailableAfter.get(serviceId) >= replacementsAvailableBefore.get(serviceId));
        }
    }
}
