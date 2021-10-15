package pwcg.aar.inmission.phase2.logeval.pilotstatus;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.BehindEnemyLines;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
public class AARPilotStatusCapturedEvaluatorTest
{
    @Mock
    private BehindEnemyLines behindEnemyLines;

    public AARPilotStatusCapturedEvaluatorTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }

    /**
     * By setting the crash point 10 km deep odds of escape are zero.
     */
    @Test
    public void testCrewMemberAlwaysCaptured () throws PWCGException
    {
        Mockito.when(behindEnemyLines.isBehindEnemyLinesForCapture(
        		ArgumentMatchers.<FrontMapIdentifier>any(),
        		ArgumentMatchers.<Coordinate>any(),
        		ArgumentMatchers.<Side>any())).thenReturn(true);

        Mockito.when(behindEnemyLines.getDistanceBehindLines(
        		ArgumentMatchers.<FrontMapIdentifier>any(),
        		ArgumentMatchers.<Coordinate>any(),
        		ArgumentMatchers.<Side>any())).thenReturn(11.0);

        
        AARPilotStatusCapturedEvaluator aarPilotStatusCapturedEvaluator = new AARPilotStatusCapturedEvaluator(DateUtils.getDateYYYYMMDD("19170101"));
        aarPilotStatusCapturedEvaluator.setBehindEnemyLines(behindEnemyLines);
        for(int i = 0; i < 100; ++i)
        {
            boolean captured = aarPilotStatusCapturedEvaluator.isCrewMemberCaptured(FrontMapIdentifier.ARRAS_MAP, new Coordinate(), Side.AXIS);
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
        		ArgumentMatchers.<FrontMapIdentifier>any(),
        		ArgumentMatchers.<Coordinate>any(),
        		ArgumentMatchers.<Side>any())).thenReturn(true);

        Mockito.when(behindEnemyLines.getDistanceBehindLines(
        		ArgumentMatchers.<FrontMapIdentifier>any(),
        		ArgumentMatchers.<Coordinate>any(),
        		ArgumentMatchers.<Side>any())).thenReturn(5.0);

        
        AARPilotStatusCapturedEvaluator aarPilotStatusCapturedEvaluator = new AARPilotStatusCapturedEvaluator(DateUtils.getDateYYYYMMDD("19170101"));
        aarPilotStatusCapturedEvaluator.setBehindEnemyLines(behindEnemyLines);
        
        boolean wasCapturedAtLeastOnce = false;
        boolean escapedAtLeastOnce = false;
        for(int i = 0; i < 100; ++i)
        {
            boolean captured = aarPilotStatusCapturedEvaluator.isCrewMemberCaptured(FrontMapIdentifier.ARRAS_MAP, new Coordinate(), Side.AXIS);
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
        		ArgumentMatchers.<FrontMapIdentifier>any(),
        		ArgumentMatchers.<Coordinate>any(),
        		ArgumentMatchers.<Side>any())).thenReturn(false);
        
        AARPilotStatusCapturedEvaluator aarPilotStatusCapturedEvaluator = new AARPilotStatusCapturedEvaluator(DateUtils.getDateYYYYMMDD("19170101"));
        aarPilotStatusCapturedEvaluator.setBehindEnemyLines(behindEnemyLines);
        for(int i = 0; i < 100; ++i)
        {
            boolean captured = aarPilotStatusCapturedEvaluator.isCrewMemberCaptured(FrontMapIdentifier.ARRAS_MAP, new Coordinate(), Side.AXIS);
            assert(captured == false);
        }
    }
    
    /**
     * No landing point means the plane was still in the air, therefore not captured.
     */
    @Test
    public void testCrewMemberNotCapturedBecauseStillInTheAir () throws PWCGException
    {
        AARPilotStatusCapturedEvaluator aarPilotStatusCapturedEvaluator = new AARPilotStatusCapturedEvaluator(DateUtils.getDateYYYYMMDD("19170101"));
        aarPilotStatusCapturedEvaluator.setBehindEnemyLines(behindEnemyLines);
        for(int i = 0; i < 100; ++i)
        {
            boolean captured = aarPilotStatusCapturedEvaluator.isCrewMemberCaptured(FrontMapIdentifier.ARRAS_MAP, null, Side.AXIS);
            assert(captured == false);
        }
    }

}
