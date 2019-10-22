package pwcg.aar.campaign.update;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.AARCoordinator;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class CampaignEquipmentArchtypeChangeHandlerTest
{
    private Campaign campaign;
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaignForceCreation(SquadronTestProfile.JG_51_PROFILE_STALINGRAD);
    }
    
    @Test
    public void testArchtypeReplacement() throws PWCGException 
    {
        assertPlaneArchType("bf109");
        assertNotPlaneArchType("fw190");
        int daysOff = DateUtils.daysDifference(campaign.getDate(), DateUtils.getDateYYYYMMDD("19420514"));
        AARCoordinator aarCoordinator = AARCoordinator.getInstance();
        aarCoordinator.submitLeave(campaign, daysOff);
        assertNotPlaneArchType("bf109");
        assertPlaneArchType("fw190");
    }
    
    @Test
    public void testArchtypeNoReplacement() throws PWCGException 
    {
        assertPlaneArchType("bf109");
        assertNotPlaneArchType("fw190");
        int daysOff = DateUtils.daysDifference(campaign.getDate(), DateUtils.getDateYYYYMMDD("19420513"));
        AARCoordinator aarCoordinator = AARCoordinator.getInstance();
        aarCoordinator.submitLeave(campaign, daysOff);
        assertPlaneArchType("bf109");
        assertNotPlaneArchType("fw190");
    }
    
    private void assertPlaneArchType(String planeArchTypeName)
    {
    	Equipment equipment = campaign.getEquipmentManager().getEquipmentForSquadron(20111051);
        for (EquippedPlane plane : equipment.getActiveEquippedPlanes().values())
        {
            assert(plane.getArchType().equals(planeArchTypeName));
        }
    }
    
    private void assertNotPlaneArchType(String planeArchTypeName)
    {
    	Equipment equipment = campaign.getEquipmentManager().getEquipmentForSquadron(20111051);
        for (EquippedPlane plane : equipment.getActiveEquippedPlanes().values())
        {
            assert(!(plane.getArchType().equals(planeArchTypeName)));
        }
    }


}
