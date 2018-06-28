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
    @Mock
    TargetTypeAvailabilityInputs targetTypeAvailabilityInputs;
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        PWCGContextManager.getInstance().changeContext(FrontMapIdentifier.KUBAN_MAP);

        Mockito.when(targetTypeAvailabilityInputs.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430401"));
        Mockito.when(targetTypeAvailabilityInputs.getSide()).thenReturn(Side.AXIS);
        Mockito.when(targetTypeAvailabilityInputs.getTargetGeneralLocation()).thenReturn(new Coordinate(216336, 0, 184721));
        Mockito.when(targetTypeAvailabilityInputs.getPreferredDistance()).thenReturn(60000.0);
        Mockito.when(targetTypeAvailabilityInputs.getMaxDistance()).thenReturn(100000.0);        
    }
    
    @Test
    public void kubanTargetAvailabilityTest() throws PWCGException
    {
        testPlaceAndTarget(TacticalTarget.TARGET_DRIFTER);
        testPlaceAndTarget(TacticalTarget.TARGET_TRANSPORT);
    }

    private void testPlaceAndTarget(TacticalTarget targetType) throws PWCGException
    {
        TargetTypeAttackGenerator targetTypeAttackGenerator = new TargetTypeAttackGenerator(targetTypeAvailabilityInputs);        
        targetTypeAttackGenerator.formTargetPriorities();
        List <TacticalTarget> preferredTargetTypes = targetTypeAttackGenerator.getPreferredTargetTypes();
        assert(preferredTargetTypes.contains(targetType) == true);
    }

}
