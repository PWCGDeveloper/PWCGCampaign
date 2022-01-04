package pwcg.campaign.plane;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.resupply.depot.EquipmentWeightCalculator;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
public class EquipmentWeightCalculatorTest
{
    @Mock Campaign campaign;
    
    public EquipmentWeightCalculatorTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);      
    }

    @Test
    public void testEquipSquadronGermanFighter() throws PWCGException
    {
        TankType me109F4 = PWCGContext.getInstance().getTankTypeFactory().getPlaneById("bf109f4");
        TankType me109G2 = PWCGContext.getInstance().getTankTypeFactory().getPlaneById("bf109g2");
        List<TankType> planeTypes = new ArrayList<>();
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
            String planeTypeName = equipmentWeightCalculator.getTankTypeFromWeight();
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
