package pwcg.campaign.context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.MathUtils;

@RunWith(MockitoJUnitRunner.class)
public class BehindEnemyLinesTest
{
	private BehindEnemyLines behindEnemyLines;
	private Coordinate referenceCoordinateOnAlliedLines =  new Coordinate(276155.0, 0.0,68288.0);
	private FrontMapIdentifier mapId = FrontMapIdentifier.FRANCE_MAP;
	
    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.getInstance().changeContext(mapId);
    	behindEnemyLines = new BehindEnemyLines(DateUtils.getDateYYYYMMDD("19170101"));
    }

    @Test
    public void testInNoMansLand () throws PWCGException
    {            	
        Coordinate noMansLand = MathUtils.calcNextCoord(referenceCoordinateOnAlliedLines, 90, 3000);
    	boolean isBehindLines = behindEnemyLines.isBehindEnemyLinesForCapture(mapId, noMansLand, Side.ALLIED);
    	assert (isBehindLines == false);
    	assert (behindEnemyLines.getReasonCode().equals("NML"));
    }

    @Test
    public void testFriendlyTerritory () throws PWCGException
    {            	
        Coordinate behindFriendlyLines = MathUtils.calcNextCoord(referenceCoordinateOnAlliedLines, 270, 3000);
    	boolean isBehindLines = behindEnemyLines.isBehindEnemyLinesForCapture(mapId, behindFriendlyLines, Side.ALLIED);
    	assert (isBehindLines == false);
    	assert (behindEnemyLines.getReasonCode().equals("Friendly Territory"));
    }

    @Test
    public void testNearGroup () throws PWCGException
    {            	
        Coordinate spotNearGroup =  new Coordinate(165000.0, 0.0, 57000.0);
    	boolean isBehindLines = behindEnemyLines.isBehindEnemyLinesForCapture(mapId, spotNearGroup, Side.ALLIED);
    	assert (isBehindLines == false);
    	assert (behindEnemyLines.getReasonCode().equals("Friendly Group"));
    }

    @Test
    public void testNearAirfield () throws PWCGException
    {            	
        Coordinate spotNearAirfield =  new Coordinate(105000.0, 0.0, 160000.0);
    	boolean isBehindLines = behindEnemyLines.isBehindEnemyLinesForCapture(mapId, spotNearAirfield, Side.ALLIED);
    	assert (isBehindLines == false);
    	assert (behindEnemyLines.getReasonCode().equals("Friendly Airfield"));
    }

    @Test
    public void testBehindEnemyLines () throws PWCGException
    {            	
        Coordinate noMansLand = MathUtils.calcNextCoord(referenceCoordinateOnAlliedLines, 90, 10000);
    	boolean isBehindLines = behindEnemyLines.isBehindEnemyLinesForCapture(mapId, noMansLand, Side.ALLIED);
    	assert (isBehindLines == true);
    	assert (behindEnemyLines.getReasonCode().equals(""));
    }

}
