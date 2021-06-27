package pwcg.campaign;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.PlaneTypeFactory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.product.bos.plane.BosPlaneAttributeMapping;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

public class CampaignEquipmentManagerTest
{    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.STALINGRAD_MAP);
    }
    
    @Test
    public void makeAircraftForSquadronTest () throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_STALINGRAD_FW190);
        Equipment equipment = campaign.getEquipmentManager().getEquipmentForSquadron(SquadronTestProfile.JG_51_PROFILE_STALINGRAD_FW190.getSquadronId());
        for (EquippedPlane plane : equipment.getActiveEquippedPlanes().values())
        {
            assert(plane.getType().contentEquals(BosPlaneAttributeMapping.FW190_A3.getPlaneType()));
        }
        

    }
    
    @Test
    public void replaceAircraftForSquadronTest () throws PWCGException
    {
        List<Integer> planesToReplace = new ArrayList<>();
        
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_STALINGRAD_FW190);
        Equipment equipment = campaign.getEquipmentManager().getEquipmentForSquadron(SquadronTestProfile.JG_51_PROFILE_STALINGRAD_FW190.getSquadronId());
        int count = 0;
        for (EquippedPlane plane : equipment.getActiveEquippedPlanes().values())
        {
            assert(plane.getType().contentEquals(BosPlaneAttributeMapping.FW190_A3.getPlaneType()));
            if (count == 0 || count == 3 || count == 6)
            {
                planesToReplace.add(plane.getSerialNumber());
            }
            ++count;
        }
        
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(SquadronTestProfile.JG_51_PROFILE_STALINGRAD_FW190.getSquadronId());
        PlaneTypeFactory planeTypeFactory = PWCGContext.getInstance().getPlaneTypeFactory();
        PlaneType planeType = planeTypeFactory.getPlaneById("bf109f4");
        campaign.getEquipmentManager().replaceAircraftForSquadron(squadron, planesToReplace, planeType.getDisplayName());
        
        int bf109Count = 0;
        for (EquippedPlane plane : equipment.getActiveEquippedPlanes().values())
        {
            if (plane.getType().contentEquals(BosPlaneAttributeMapping.BF109_F4.getPlaneType()))
            {
                ++bf109Count;
            }
        }
        assert(bf109Count == 3);
    }
}
