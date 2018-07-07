package pwcg.aar.campaign.update;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.EquipmentReplacement;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignCacheBoS;

@RunWith(MockitoJUnitRunner.class)
public class CampaignEquipmentReplacementUpdaterTest 
{
	private Campaign campaign;
	
    @Before
    public void setup() throws PWCGException
    {
    	PWCGContextManager.setRoF(false);
        campaign = CampaignCache.makeCampaign(CampaignCacheBoS.KG53_PROFILE);
    }
    
    @Test
    public void testUpdateWithReplacementsGermans() throws PWCGException 
    {
        Map<Integer, Integer> replacementsAvailableBefore = determineReplacementsAvailableByService();
        CampaignEquipmentReplacementUpdater equipmentReplacementUpdater  = new CampaignEquipmentReplacementUpdater(campaign);
        equipmentReplacementUpdater.updateCampaignEquipmentReplacements();
        Map<Integer, Integer> replacementsAvailableAfter = determineReplacementsAvailableByService();
        validateReplacements(replacementsAvailableBefore, replacementsAvailableAfter);
    }

    private Map<Integer, Integer> determineReplacementsAvailableByService()
    {
        Map<Integer, Integer> replacementsAvailable = new HashMap<>();
        for (Integer serviceId : campaign.getEquipmentManager().getEquipmentReplacements().keySet())
        {
            EquipmentReplacement replacementEquipmentForService = campaign.getEquipmentManager().getEquipmentReplacementsForService(serviceId);
            replacementsAvailable.put(serviceId, replacementEquipmentForService.getEquipment().getEquippedPlanes().size());
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
