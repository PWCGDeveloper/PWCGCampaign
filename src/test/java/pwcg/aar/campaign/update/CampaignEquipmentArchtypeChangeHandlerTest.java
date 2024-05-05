package pwcg.aar.campaign.update;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.AARCoordinator;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestCampaignFactoryBuilder;

@ExtendWith(MockitoExtension.class)
public class CampaignEquipmentArchtypeChangeHandlerTest
{    
    public CampaignEquipmentArchtypeChangeHandlerTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }
    
    @Test
    public void testArchtypeReplacement() throws PWCGException 
    {
        Campaign campaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.JG_51_PROFILE_STALINGRAD);
        assertPlaneArchType(campaign, "bf109");
        assertNotPlaneArchType(campaign, "fw190");
        int daysOff = DateUtils.daysDifference(campaign.getDate(), DateUtils.getDateYYYYMMDD("19420514"));
        AARCoordinator aarCoordinator = AARCoordinator.getInstance();
        aarCoordinator.submitLeave(campaign, daysOff);
        assertNotPlaneArchType(campaign, "bf109");
        assertPlaneArchType(campaign, "fw190");
    }
    
    @Test
    public void testArchtypeNoReplacement() throws PWCGException 
    {
        Campaign campaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.JG_51_PROFILE_STALINGRAD);
        assertPlaneArchType(campaign, "bf109");
        assertNotPlaneArchType(campaign, "fw190");
        int daysOff = DateUtils.daysDifference(campaign.getDate(), DateUtils.getDateYYYYMMDD("19420513"));
        AARCoordinator aarCoordinator = AARCoordinator.getInstance();
        aarCoordinator.submitLeave(campaign, daysOff);
        assertPlaneArchType(campaign, "bf109");
        assertNotPlaneArchType(campaign, "fw190");
    }
    
    private void assertPlaneArchType(Campaign campaign, String planeArchTypeName)
    {
    	Equipment equipment = campaign.getEquipmentManager().getEquipmentForSquadron(20111051);
        for (EquippedPlane plane : equipment.getActiveEquippedPlanes().values())
        {
            assert(plane.getArchType().equals(planeArchTypeName));
        }
    }
    
    private void assertNotPlaneArchType(Campaign campaign, String planeArchTypeName)
    {
    	Equipment equipment = campaign.getEquipmentManager().getEquipmentForSquadron(20111051);
        for (EquippedPlane plane : equipment.getActiveEquippedPlanes().values())
        {
            assert(!(plane.getArchType().equals(planeArchTypeName)));
        }
    }


}
