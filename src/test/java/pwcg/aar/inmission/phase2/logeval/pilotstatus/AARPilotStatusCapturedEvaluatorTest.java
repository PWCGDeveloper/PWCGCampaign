package pwcg.aar.inmission.phase2.logeval.pilotstatus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.BehindEnemyLines;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class AARPilotStatusCapturedEvaluatorTest
{
    @Mock
    private BehindEnemyLines behindEnemyLines;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
    }

    /**
     * By setting the crash point 10 km deep odds of escape are zero.
     */
    @Test
    public void testCrewMemberAlwaysCaptured () throws PWCGException
    {
        Mockito.when(behindEnemyLines.isBehindEnemyLinesForCapture(
        		Matchers.<FrontMapIdentifier>any(),
        		Matchers.<Coordinate>any(),
        		Matchers.<Side>any())).thenReturn(true);

        Mockito.when(behindEnemyLines.getDistanceBehindLines(
        		Matchers.<FrontMapIdentifier>any(),
        		Matchers.<Coordinate>any(),
        		Matchers.<Side>any())).thenReturn(11.0);

        
        AARPilotStatusCapturedEvaluator aarPilotStatusCapturedEvaluator = new AARPilotStatusCapturedEvaluator(DateUtils.getDateYYYYMMDD("19170101"));
        aarPilotStatusCapturedEvaluator.setBehindEnemyLines(behindEnemyLines);
        for(int i = 0; i < 100; ++i)
        {
            boolean captured = aarPilotStatusCapturedEvaluator.isCrewMemberCaptured(FrontMapIdentifier.FRANCE_MAP, new Coordinate(), Side.AXIS);
            assert(captured == true);
        }
    }

    /**
     * By setting the crash point five km deep odds of escape are 50%.
     */
    @Test
    public void testCrewMemberNotCapturedBecauseEscaped () throws PWCGException
    {
        Mockito.when(behindEnemyLines.isBehindEnemyLinesForCapture(
        		Matchers.<FrontMapIdentifier>any(),
        		Matchers.<Coordinate>any(),
        		Matchers.<Side>any())).thenReturn(true);

        Mockito.when(behindEnemyLines.getDistanceBehindLines(
        		Matchers.<FrontMapIdentifier>any(),
        		Matchers.<Coordinate>any(),
        		Matchers.<Side>any())).thenReturn(5.0);

        
        AARPilotStatusCapturedEvaluator aarPilotStatusCapturedEvaluator = new AARPilotStatusCapturedEvaluator(DateUtils.getDateYYYYMMDD("19170101"));
        aarPilotStatusCapturedEvaluator.setBehindEnemyLines(behindEnemyLines);
        
        boolean wasCapturedAtLeastOnce = false;
        boolean escapedAtLeastOnce = false;
        for(int i = 0; i < 100; ++i)
        {
            boolean captured = aarPilotStatusCapturedEvaluator.isCrewMemberCaptured(FrontMapIdentifier.FRANCE_MAP, new Coordinate(), Side.AXIS);
            if (captured)
            {
                wasCapturedAtLeastOnce = true;
            }
            else
            {
                escapedAtLeastOnce = true;
            }
        }

        assert(wasCapturedAtLeastOnce == true);
        assert(escapedAtLeastOnce == true);
    }
    
    /**
     * By setting the crash point inside friendly lines the pilot is not captured.
     */
    @Test
    public void testCrewMemberNotCapturedBecauseNotBehindLines () throws PWCGException
    {
        Mockito.when(behindEnemyLines.isBehindEnemyLinesForCapture(
        		Matchers.<FrontMapIdentifier>any(),
        		Matchers.<Coordinate>any(),
        		Matchers.<Side>any())).thenReturn(false);

        Mockito.when(behindEnemyLines.getDistanceBehindLines(
        		Matchers.<FrontMapIdentifier>any(),
        		Matchers.<Coordinate>any(),
        		Matchers.<Side>any())).thenThrow(new PWCGException("Should not do this if behindfriendly lines"));

        
        AARPilotStatusCapturedEvaluator aarPilotStatusCapturedEvaluator = new AARPilotStatusCapturedEvaluator(DateUtils.getDateYYYYMMDD("19170101"));
        aarPilotStatusCapturedEvaluator.setBehindEnemyLines(behindEnemyLines);
        for(int i = 0; i < 100; ++i)
        {
            boolean captured = aarPilotStatusCapturedEvaluator.isCrewMemberCaptured(FrontMapIdentifier.FRANCE_MAP, new Coordinate(), Side.AXIS);
            assert(captured == false);
        }
    }
    
    /**
     * No landing point means the plane was still in the air, therefore not captured.
     */
    @Test
    public void testCrewMemberNotCapturedBecauseStillInTheAir () throws PWCGException
    {
        Mockito.when(behindEnemyLines.isBehindEnemyLinesForCapture(
        		Matchers.<FrontMapIdentifier>any(),
        		Matchers.<Coordinate>any(),
        		Matchers.<Side>any())).thenThrow(new PWCGException("Should not do this if behindfriendly lines"));

        Mockito.when(behindEnemyLines.getDistanceBehindLines(
        		Matchers.<FrontMapIdentifier>any(),
        		Matchers.<Coordinate>any(),
        		Matchers.<Side>any())).thenThrow(new PWCGException("Should not do this if behindfriendly lines"));

        
        AARPilotStatusCapturedEvaluator aarPilotStatusCapturedEvaluator = new AARPilotStatusCapturedEvaluator(DateUtils.getDateYYYYMMDD("19170101"));
        aarPilotStatusCapturedEvaluator.setBehindEnemyLines(behindEnemyLines);
        for(int i = 0; i < 100; ++i)
        {
            boolean captured = aarPilotStatusCapturedEvaluator.isCrewMemberCaptured(FrontMapIdentifier.FRANCE_MAP, null, Side.AXIS);
            assert(captured == false);
        }
    }

}
