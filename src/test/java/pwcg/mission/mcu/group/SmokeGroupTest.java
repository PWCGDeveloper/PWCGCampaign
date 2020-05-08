package pwcg.mission.mcu.group;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionFlightBuilder;

@RunWith(MockitoJUnitRunner.class)
public class SmokeGroupTest
{
    @Mock Mission mission;
    @Mock MissionFlightBuilder missionFlightBuilder;
    
    @Before
    public void setup() throws PWCGException
    {
        Mockito.when(mission.getMissionFlightBuilder()).thenReturn(missionFlightBuilder);
    }

    @Test
    public void testSmokeGroupCreation() throws PWCGException
    {
        
        Coordinate smokeEffectPosition = new Coordinate(100, 0, 100);
        
        SmokeGroup smokeGroup = new SmokeGroup();
        smokeGroup.buildSmokeGroup(mission, smokeEffectPosition, SmokeEffect.SMOKE_CITY);
        smokeGroup.buildSmokeGroup(mission, smokeEffectPosition, SmokeEffect.SMOKE_CITY_SMALL);
        smokeGroup.buildSmokeGroup(mission, smokeEffectPosition, SmokeEffect.SMOKE_VILLAGE);
        
        assert(smokeGroup.getSmokeEffects().size() == 3);
        assert(smokeGroup.getSmokeEffects().get(0).getScript().contains("city_fire.txt"));
        assert(smokeGroup.getSmokeEffects().get(1).getScript().contains("city_firesmall.txt"));
        assert(smokeGroup.getSmokeEffects().get(2).getScript().contains("villagesmoke.txt"));
        
        assert(MathUtils.calcDist(smokeGroup.getPosition(), smokeEffectPosition) < 5.0);
    }
}
