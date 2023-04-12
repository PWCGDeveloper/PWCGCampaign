package pwcg.campaign.context;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.MathUtils;

@ExtendWith(MockitoExtension.class)
public class BehindEnemyLinesTest
{
    @Mock Campaign campaign;
    
	private BehindEnemyLines behindEnemyLines;
	private Coordinate referenceCoordinateOnAlliedLines =  new Coordinate(302064.0, 0.0, 100959.0);
	private FrontMapIdentifier mapId = FrontMapIdentifier.WESTERN_FRONT_MAP;
	
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        Mockito.when(campaign.getCampaignMap()).thenReturn(mapId);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19170601"));
    	behindEnemyLines = new BehindEnemyLines(campaign);
    }

    @Test
    public void testInNoMansLand () throws PWCGException
    {            	
        Coordinate noMansLand = MathUtils.calcNextCoord(mapId, referenceCoordinateOnAlliedLines, 90, 3000);
    	boolean isBehindLines = behindEnemyLines.isBehindEnemyLinesForCapture(noMansLand, Side.ALLIED);
    	Assertions.assertTrue (isBehindLines == false);
    	Assertions.assertTrue (behindEnemyLines.getReasonCode().equals("NML"));
    }

    @Test
    public void testFriendlyTerritory () throws PWCGException
    {            	
        Coordinate behindFriendlyLines = MathUtils.calcNextCoord(mapId, referenceCoordinateOnAlliedLines, 270, 3000);
    	boolean isBehindLines = behindEnemyLines.isBehindEnemyLinesForCapture(behindFriendlyLines, Side.ALLIED);
    	Assertions.assertTrue (isBehindLines == false);
    	Assertions.assertTrue (behindEnemyLines.getReasonCode().equals("Friendly Territory"));
    }

    @Test
    public void testNearGroup () throws PWCGException
    {            	
        Coordinate noeuxLesMines =  new Coordinate(263140.0, 0.0, 84168.0);
    	boolean isBehindLines = behindEnemyLines.isBehindEnemyLinesForCapture(noeuxLesMines, Side.AXIS);
    	Assertions.assertTrue (isBehindLines == false);
    }

    @Test
    public void testNearAirfield () throws PWCGException
    {            	
        Coordinate lille =  new Coordinate(277806.0, 0.0, 112789.0);
        boolean isBehindLines = behindEnemyLines.isBehindEnemyLinesForCapture(lille, Side.AXIS);
        Assertions.assertTrue (isBehindLines == false);
    }

    @Test
    public void testBehindEnemyLines () throws PWCGException
    {            	
        Coordinate noMansLand = MathUtils.calcNextCoord(mapId, referenceCoordinateOnAlliedLines, 90, 15000);
    	boolean isBehindLines = behindEnemyLines.isBehindEnemyLinesForCapture(noMansLand, Side.ALLIED);
    	Assertions.assertTrue (isBehindLines == true);
    	Assertions.assertTrue (behindEnemyLines.getReasonCode().equals(""));
    }

}
