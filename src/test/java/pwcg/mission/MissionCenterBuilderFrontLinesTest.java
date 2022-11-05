package pwcg.mission;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.MapArea;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.product.bos.map.moscow.MoscowMapArea;
import pwcg.product.bos.map.moscow.MoscowMapUsableArea;

public class MissionCenterBuilderFrontLinesTest
{
    public MissionCenterBuilderFrontLinesTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }
    
    @Test
    public void testNoAdjustmment() throws PWCGException
    {
        MapArea usableMapArea = new MoscowMapUsableArea();

        Coordinate missionCenterCoordinate = new Coordinate(usableMapArea.getxMin(), 0.0, usableMapArea.getzMin());
        missionCenterCoordinate = MathUtils.calcNextCoord(FrontMapIdentifier.MOSCOW_MAP, missionCenterCoordinate, 45, 50000);

        Coordinate adjustedCoordinate = MissionCenterAdjuster.keepWithinMap(FrontMapIdentifier.MOSCOW_MAP, missionCenterCoordinate.copy(), 30000, usableMapArea);
        Assertions.assertTrue (missionCenterCoordinate.equals(adjustedCoordinate));
    }

    @Test
    public void testAdjustWest() throws PWCGException
    {
        MapArea mapArea = new MoscowMapArea();
        MapArea usableMapArea = new MoscowMapUsableArea();

        Coordinate missionCenterCoordinate = new Coordinate(mapArea.getxMin() + 50000, 0.0, mapArea.getzMax());

        Coordinate adjustedCoordinate = MissionCenterAdjuster.keepWithinMap(FrontMapIdentifier.MOSCOW_MAP, missionCenterCoordinate.copy(), 30000, usableMapArea);
        Assertions.assertTrue (!missionCenterCoordinate.equals(adjustedCoordinate));
        Assertions.assertTrue (adjustedCoordinate.getXPos() - missionCenterCoordinate.getXPos() < 0.1);
        verifyInUsableMapArea(usableMapArea, adjustedCoordinate);
    }

    @Test
    public void testAdjustEast() throws PWCGException
    {
        MapArea mapArea = new MoscowMapArea();
        MapArea usableMapArea = new MoscowMapUsableArea();

        Coordinate missionCenterCoordinate = new Coordinate(mapArea.getxMin() + 50000, 0.0, usableMapArea.getzMin() - 5000);

        Coordinate adjustedCoordinate = MissionCenterAdjuster.keepWithinMap(FrontMapIdentifier.MOSCOW_MAP, missionCenterCoordinate.copy(), 30000, usableMapArea);
        Assertions.assertTrue (!missionCenterCoordinate.equals(adjustedCoordinate));
        Assertions.assertTrue (Math.abs(adjustedCoordinate.getXPos() - missionCenterCoordinate.getXPos()) < 0.1);
        verifyInUsableMapArea(usableMapArea, adjustedCoordinate);
    }

    @Test
    public void testAdjustSouth() throws PWCGException
    {
        MapArea mapArea = new MoscowMapArea();
        MapArea usableMapArea = new MoscowMapUsableArea();

        Coordinate missionCenterCoordinate = new Coordinate(mapArea.getxMin(), 0.0, usableMapArea.getzMin() + 50000);

        Coordinate adjustedCoordinate = MissionCenterAdjuster.keepWithinMap(FrontMapIdentifier.MOSCOW_MAP, missionCenterCoordinate.copy(), 30000, usableMapArea);
        Assertions.assertTrue (!missionCenterCoordinate.equals(adjustedCoordinate));
        Assertions.assertTrue (adjustedCoordinate.getZPos() - missionCenterCoordinate.getZPos() < 0.1);
        verifyInUsableMapArea(usableMapArea, adjustedCoordinate);
    }

    @Test
    public void testAdjustNorth() throws PWCGException
    {
        MapArea mapArea = new MoscowMapArea();
        MapArea usableMapArea = new MoscowMapUsableArea();

        Coordinate missionCenterCoordinate = new Coordinate(mapArea.getxMax(), 0.0, usableMapArea.getzMin() + 50000);

        Coordinate adjustedCoordinate = MissionCenterAdjuster.keepWithinMap(FrontMapIdentifier.MOSCOW_MAP, missionCenterCoordinate.copy(), 30000, usableMapArea);
        Assertions.assertTrue (!missionCenterCoordinate.equals(adjustedCoordinate));
        Assertions.assertTrue (adjustedCoordinate.getZPos() - missionCenterCoordinate.getZPos() < 0.1);
        verifyInUsableMapArea(usableMapArea, adjustedCoordinate);
    }

    @Test
    public void testAdjustSouthEast() throws PWCGException
    {
        MapArea mapArea = new MoscowMapArea();
        MapArea usableMapArea = new MoscowMapUsableArea();

        Coordinate missionCenterCoordinate = new Coordinate(mapArea.getxMin(), 0.0, usableMapArea.getzMin());

        Coordinate adjustedCoordinate = MissionCenterAdjuster.keepWithinMap(FrontMapIdentifier.MOSCOW_MAP, missionCenterCoordinate.copy(), 30000, usableMapArea);
        Assertions.assertTrue (!missionCenterCoordinate.equals(adjustedCoordinate));
        verifyInUsableMapArea(usableMapArea, adjustedCoordinate);
    }

    private void verifyInUsableMapArea(MapArea usableMapArea, Coordinate adjustedCoordinate) throws PWCGException
    {
        Coordinate missionWestCoordinate = MathUtils.calcNextCoord(FrontMapIdentifier.MOSCOW_MAP, adjustedCoordinate, 270, 30000);
        Assertions.assertTrue (missionWestCoordinate.getXPos() >= usableMapArea.getxMin());

        Coordinate missionEastCoordinate = MathUtils.calcNextCoord(FrontMapIdentifier.MOSCOW_MAP, adjustedCoordinate, 90, 30000);
        Assertions.assertTrue (missionEastCoordinate.getXPos() <= usableMapArea.getxMax());

        Coordinate missionSouthCoordinate = MathUtils.calcNextCoord(FrontMapIdentifier.MOSCOW_MAP, adjustedCoordinate, 180, 30000);
        Assertions.assertTrue (missionSouthCoordinate.getZPos() >= usableMapArea.getzMin());

        Coordinate missionNorthCoordinate = MathUtils.calcNextCoord(FrontMapIdentifier.MOSCOW_MAP, adjustedCoordinate, 0, 30000);
        Assertions.assertTrue (missionNorthCoordinate.getZPos() <= usableMapArea.getzMax());
    }
}
