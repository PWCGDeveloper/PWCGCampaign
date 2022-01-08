package pwcg.campaign;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.company.Company;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.PlaneAttributeMapping;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.TankType;
import pwcg.campaign.plane.TankTypeFactory;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CampaignEquipmentManagerTest
{    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.STALINGRAD_MAP);
    }
    
    @Test
    public void makeAircraftForSquadronTest () throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.GROSS_DEUTSCHLAND_PROFILE_FW190);
        Equipment equipment = campaign.getEquipmentManager().getEquipmentForCompany(SquadronTestProfile.GROSS_DEUTSCHLAND_PROFILE_FW190.getCompanyId());
        for (EquippedTank plane : equipment.getActiveEquippedTanks().values())
        {
            assert(plane.getType().contentEquals(PlaneAttributeMapping.FW190_A3.getTankType()));
        }
        

    }
    
    @Test
    public void replaceAircraftForSquadronTest () throws PWCGException
    {
        List<Integer> planesToReplace = new ArrayList<>();
        
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.GROSS_DEUTSCHLAND_PROFILE_FW190);
        Equipment equipment = campaign.getEquipmentManager().getEquipmentForCompany(SquadronTestProfile.GROSS_DEUTSCHLAND_PROFILE_FW190.getCompanyId());
        int count = 0;
        for (EquippedTank plane : equipment.getActiveEquippedTanks().values())
        {
            assert(plane.getType().contentEquals(PlaneAttributeMapping.FW190_A3.getTankType()));
            if (count == 0 || count == 3 || count == 6)
            {
                planesToReplace.add(plane.getSerialNumber());
            }
            ++count;
        }
        
        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(SquadronTestProfile.GROSS_DEUTSCHLAND_PROFILE_FW190.getCompanyId());
        TankTypeFactory planeTypeFactory = PWCGContext.getInstance().getTankTypeFactory();
        TankType planeType = planeTypeFactory.getPlaneById("bf109f4");
        campaign.getEquipmentManager().actOnEquipmentRequest(squadron, planesToReplace, planeType.getDisplayName());
        
        int bf109Count = 0;
        for (EquippedTank plane : equipment.getActiveEquippedTanks().values())
        {
            if (plane.getType().contentEquals(PlaneAttributeMapping.BF109_F4.getTankType()))
            {
                ++bf109Count;
            }
        }
        assert(bf109Count == 3);
    }
}
