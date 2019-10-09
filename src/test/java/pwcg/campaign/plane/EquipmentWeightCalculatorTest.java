package pwcg.campaign.plane;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.resupply.depo.EquipmentWeightCalculator;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class EquipmentWeightCalculatorTest
{
    @Mock Campaign campaign;
    
    @Before 
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);      
    }

    @Test
    public void testEquipSquadronGermanFighter() throws PWCGException
    {
        PlaneType me109F4 = PWCGContext.getInstance().getPlaneTypeFactory().getPlaneById("bf109f4");
        PlaneType me109G2 = PWCGContext.getInstance().getPlaneTypeFactory().getPlaneById("bf109g2");
        List<PlaneType> planeTypes = new ArrayList<>();
        planeTypes.add(me109F4);
        planeTypes.add(me109G2);
        
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420501"));
        EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(campaign.getDate());
        equipmentWeightCalculator.determinePlaneWeightsForPlanes(planeTypes);
        
        Map<String, Integer> weightedPlaneOdds = equipmentWeightCalculator.getWeightedPlaneOdds();
        assert(weightedPlaneOdds.get("bf109f4") == 100);
        assert(weightedPlaneOdds.get("bf109g2") == 30);
        
        int me109F4Count = 0;
        int me109G2Count = 0;
        for (int i = 0; i < 10000; ++i)
        {
            String planeTypeName = equipmentWeightCalculator.getPlaneTypeFromWeight();
            if (planeTypeName.equals("bf109f4"))
            {
                ++me109F4Count;
            }
            else if (planeTypeName.equals("bf109g2"))
            {
                ++me109G2Count;
            }
            else
            {
                throw new PWCGException("Unexpected plane from plane calculator: " + planeTypeName);
            }
        }
        
        assert(me109F4Count > 7000 && me109F4Count < 8500);
        assert(me109G2Count > 1500 && me109G2Count < 3000);
    }
}
