package pwcg.mission;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.context.MapArea;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.product.bos.map.moscow.MoscowMapArea;
import pwcg.product.bos.map.moscow.MoscowMapUsableArea;

public class MissionCenterBuilderFrontLinesTest
{
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        PWCGContext.getInstance().setCurrentMap(FrontMapIdentifier.MOSCOW_MAP);
    }
    
    @Test
    public void testNoAdjustmment() throws PWCGException
    {
        MapArea usableMapArea = new MoscowMapUsableArea();

        Coordinate missionCenterCoordinate = new Coordinate(usableMapArea.getxMin(), 0.0, usableMapArea.getzMin());
        missionCenterCoordinate = MathUtils.calcNextCoord(missionCenterCoordinate, 45, 50000);

        Coordinate adjustedCoordinate = MissionCenterAdjuster.keepWithinMap(missionCenterCoordinate.copy(), 30000, usableMapArea);
        assert (missionCenterCoordinate.equals(adjustedCoordinate));
    }

    @Test
    public void testAdjustWest() throws PWCGException
    {
        MapArea mapArea = new MoscowMapArea();
        MapArea usableMapArea = new MoscowMapUsableArea();

        Coordinate missionCenterCoordinate = new Coordinate(mapArea.getxMin() + 50000, 0.0, mapArea.getzMax());

        Coordinate adjustedCoordinate = MissionCenterAdjuster.keepWithinMap(missionCenterCoordinate.copy(), 30000, usableMapArea);
        assert (!missionCenterCoordinate.equals(adjustedCoordinate));
        assert (adjustedCoordinate.getXPos() - missionCenterCoordinate.getXPos() < 0.1);
        verifyInUsableMapArea(usableMapArea, adjustedCoordinate);
    }

    @Test
    public void testAdjustEast() throws PWCGException
    {
        MapArea mapArea = new MoscowMapArea();
        MapArea usableMapArea = new MoscowMapUsableArea();

        Coordinate missionCenterCoordinate = new Coordinate(mapArea.getxMin() + 50000, 0.0, usableMapArea.getzMin() - 5000);

        Coordinate adjustedCoordinate = MissionCenterAdjuster.keepWithinMap(missionCenterCoordinate.copy(), 30000, usableMapArea);
        assert (!missionCenterCoordinate.equals(adjustedCoordinate));
        assert (Math.abs(adjustedCoordinate.getXPos() - missionCenterCoordinate.getXPos()) < 0.1);
        verifyInUsableMapArea(usableMapArea, adjustedCoordinate);
    }

    @Test
    public void testAdjustSouth() throws PWCGException
    {
        MapArea mapArea = new MoscowMapArea();
        MapArea usableMapArea = new MoscowMapUsableArea();

        Coordinate missionCenterCoordinate = new Coordinate(mapArea.getxMin(), 0.0, usableMapArea.getzMin() + 50000);

        Coordinate adjustedCoordinate = MissionCenterAdjuster.keepWithinMap(missionCenterCoordinate.copy(), 30000, usableMapArea);
        assert (!missionCenterCoordinate.equals(adjustedCoordinate));
        assert (adjustedCoordinate.getZPos() - missionCenterCoordinate.getZPos() < 0.1);
        verifyInUsableMapArea(usableMapArea, adjustedCoordinate);
    }

    @Test
    public void testAdjustNorth() throws PWCGException
    {
        MapArea mapArea = new MoscowMapArea();
        MapArea usableMapArea = new MoscowMapUsableArea();

        Coordinate missionCenterCoordinate = new Coordinate(mapArea.getxMax(), 0.0, usableMapArea.getzMin() + 50000);

        Coordinate adjustedCoordinate = MissionCenterAdjuster.keepWithinMap(missionCenterCoordinate.copy(), 30000, usableMapArea);
        assert (!missionCenterCoordinate.equals(adjustedCoordinate));
        assert (adjustedCoordinate.getZPos() - missionCenterCoordinate.getZPos() < 0.1);
        verifyInUsableMapArea(usableMapArea, adjustedCoordinate);
    }

    @Test
    public void testAdjustSouthEast() throws PWCGException
    {
        MapArea mapArea = new MoscowMapArea();
        MapArea usableMapArea = new MoscowMapUsableArea();

        Coordinate missionCenterCoordinate = new Coordinate(mapArea.getxMin(), 0.0, usableMapArea.getzMin());

        Coordinate adjustedCoordinate = MissionCenterAdjuster.keepWithinMap(missionCenterCoordinate.copy(), 30000, usableMapArea);
        assert (!missionCenterCoordinate.equals(adjustedCoordinate));
        verifyInUsableMapArea(usableMapArea, adjustedCoordinate);
    }

    private void verifyInUsableMapArea(MapArea usableMapArea, Coordinate adjustedCoordinate) throws PWCGException
    {
        Coordinate missionWestCoordinate = MathUtils.calcNextCoord(adjustedCoordinate, 270, 30000);
        assert (missionWestCoordinate.getXPos() >= usableMapArea.getxMin());

        Coordinate missionEastCoordinate = MathUtils.calcNextCoord(adjustedCoordinate, 90, 30000);
        assert (missionEastCoordinate.getXPos() <= usableMapArea.getxMax());

        Coordinate missionSouthCoordinate = MathUtils.calcNextCoord(adjustedCoordinate, 180, 30000);
        assert (missionSouthCoordinate.getZPos() >= usableMapArea.getzMin());

        Coordinate missionNorthCoordinate = MathUtils.calcNextCoord(adjustedCoordinate, 0, 30000);
        assert (missionNorthCoordinate.getZPos() <= usableMapArea.getzMax());
    }
}
