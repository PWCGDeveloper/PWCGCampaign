package pwcg.campaign.target;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class TargetTypeAttackGeneratorTest
{    
    @Mock private TargetTypeAvailabilityInputs targetTypeAvailabilityInputs;
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(false);

        Mockito.when(targetTypeAvailabilityInputs.getSide()).thenReturn(Side.AXIS);
        Mockito.when(targetTypeAvailabilityInputs.getTargetGeneralLocation()).thenReturn(new Coordinate(216336, 0, 184721));
        Mockito.when(targetTypeAvailabilityInputs.getPreferredDistance()).thenReturn(60000.0);
        Mockito.when(targetTypeAvailabilityInputs.getMaxDistance()).thenReturn(100000.0);        
    }
    
    @Test
    public void kubanTargetAvailabilityTest() throws PWCGException
    {
        Mockito.when(targetTypeAvailabilityInputs.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430401"));
        PWCGContextManager.getInstance().changeContext(FrontMapIdentifier.KUBAN_MAP);
        testPlaceAndTarget(TacticalTarget.TARGET_DRIFTER, true);
        testPlaceAndTarget(TacticalTarget.TARGET_SHIPPING, true);
    }
    
    @Test
    public void moscowNoTargetAvailabilityTest() throws PWCGException
    {
        Mockito.when(targetTypeAvailabilityInputs.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19411001"));
        PWCGContextManager.getInstance().changeContext(FrontMapIdentifier.MOSCOW_MAP);
        testPlaceAndTarget(TacticalTarget.TARGET_DRIFTER, false);
        testPlaceAndTarget(TacticalTarget.TARGET_SHIPPING, false);
    }

    private void testPlaceAndTarget(TacticalTarget targetType, boolean assertion) throws PWCGException
    {
        TargetTypeAttackGenerator targetTypeAttackGenerator = new TargetTypeAttackGenerator(targetTypeAvailabilityInputs);        
        targetTypeAttackGenerator.formTargetPriorities();
        List <TacticalTarget> preferredTargetTypes = targetTypeAttackGenerator.getPreferredTargetTypes();
        assert(preferredTargetTypes.contains(targetType) == assertion);
    }

}
