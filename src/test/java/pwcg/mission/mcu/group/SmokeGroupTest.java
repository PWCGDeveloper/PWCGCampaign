package pwcg.mission.mcu.group;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.mcu.McuCheckZone;

@RunWith(MockitoJUnitRunner.class)
public class SmokeGroupTest
{    
    @Before
    public void setup() throws PWCGException
    {
    }

    @Test
    public void testSmokeGroupCreation() throws PWCGException
    {
        
        Coordinate smokeEffectPosition = new Coordinate(100, 0, 100);
        
        SmokeGroup smokeGroup = new SmokeGroup(Arrays.asList(100, 101, 102));
        smokeGroup.buildSmokeGroup(smokeEffectPosition, 90, SmokeEffect.SMOKE_CITY);
        smokeGroup.buildSmokeGroup(smokeEffectPosition, 120, SmokeEffect.SMOKE_CITY_SMALL);
        smokeGroup.buildSmokeGroup(smokeEffectPosition, 180, SmokeEffect.SMOKE_VILLAGE);
        
        assert(smokeGroup.getSmokeEffects().size() == 3);
        assert(smokeGroup.getSmokeEffects().get(0).getScript().contains("city_fire_loop.txt"));
        assert(smokeGroup.getSmokeEffects().get(1).getScript().contains("city_firesmall_loop.txt"));
        assert(smokeGroup.getSmokeEffects().get(2).getScript().contains("villagesmoke_loop.txt"));
        
        assert(smokeGroup.getSmokeEffects().get(0).getOrientation().getyOri() == 90);
        assert(smokeGroup.getSmokeEffects().get(1).getOrientation().getyOri() == 120);
        assert(smokeGroup.getSmokeEffects().get(2).getOrientation().getyOri() == 180);

        McuCheckZone checkZone = smokeGroup.getSmokeStartCheckZone();
        assert(checkZone.getObjects().size() == 3);
        
        assert(MathUtils.calcDist(smokeGroup.getPosition(), smokeEffectPosition) < 5.0);
    }
}
