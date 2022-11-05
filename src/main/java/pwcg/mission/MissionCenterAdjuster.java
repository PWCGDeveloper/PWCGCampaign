package pwcg.mission;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.MapArea;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;

public class MissionCenterAdjuster
{

    public static Coordinate keepWithinMap(FrontMapIdentifier mapIdentifier, Coordinate missionCenterCoordinate, int missionBoxRadius, MapArea usableMapArea) throws PWCGException
    {
        missionCenterCoordinate = adjustForSouthEdge(mapIdentifier, missionCenterCoordinate, missionBoxRadius, usableMapArea);
        missionCenterCoordinate = adjustForNorthEdge(mapIdentifier, missionCenterCoordinate, missionBoxRadius, usableMapArea);
        missionCenterCoordinate = adjustForWestEdge(mapIdentifier, missionCenterCoordinate, missionBoxRadius, usableMapArea);
        missionCenterCoordinate = adjustForEastEdge(mapIdentifier, missionCenterCoordinate, missionBoxRadius, usableMapArea);
        return missionCenterCoordinate;
    }

    private static Coordinate adjustForSouthEdge(FrontMapIdentifier mapIdentifier, Coordinate missionCenterCoordinate, int missionBoxRadius, MapArea usableMapArea) throws PWCGException
    {
        Coordinate missionSouthCoordinate = MathUtils.calcNextCoord(mapIdentifier, missionCenterCoordinate, 180, missionBoxRadius);
        if (missionSouthCoordinate.getXPos()  < usableMapArea.getxMin())
        {
            double distanceToMove = usableMapArea.getxMin() - missionSouthCoordinate.getXPos();
            missionCenterCoordinate = MathUtils.calcNextCoordWithMapAdjustments(mapIdentifier, missionCenterCoordinate, 0, distanceToMove);
        }
        return missionCenterCoordinate;
    }

    private static Coordinate adjustForNorthEdge(FrontMapIdentifier mapIdentifier, Coordinate missionCenterCoordinate, int missionBoxRadius, MapArea usableMapArea) throws PWCGException
    {
        Coordinate missionNorthCoordinate = MathUtils.calcNextCoord(mapIdentifier, missionCenterCoordinate, 0, missionBoxRadius);
        if (missionNorthCoordinate.getXPos()  > usableMapArea.getxMax())
        {
            double distanceToMove = missionNorthCoordinate.getXPos() - usableMapArea.getxMax();
            missionCenterCoordinate = MathUtils.calcNextCoordWithMapAdjustments(mapIdentifier, missionCenterCoordinate, 180, distanceToMove);
        }
        return missionCenterCoordinate;
    }

    private static Coordinate adjustForWestEdge(FrontMapIdentifier mapIdentifier, Coordinate missionCenterCoordinate, int missionBoxRadius, MapArea usableMapArea) throws PWCGException
    {
        Coordinate missionWestCoordinate = MathUtils.calcNextCoord(mapIdentifier, missionCenterCoordinate, 270, missionBoxRadius);
        if (missionWestCoordinate.getZPos()  < usableMapArea.getzMin())
        {
            double distanceToMove = usableMapArea.getzMin() - missionWestCoordinate.getZPos();
            missionCenterCoordinate = MathUtils.calcNextCoordWithMapAdjustments(mapIdentifier, missionCenterCoordinate, 90, distanceToMove);
        }
        return missionCenterCoordinate;
    }

    private static Coordinate adjustForEastEdge(FrontMapIdentifier mapIdentifier, Coordinate missionCenterCoordinate, int missionBoxRadius, MapArea usableMapArea) throws PWCGException
    {
        Coordinate missionEastCoordinate = MathUtils.calcNextCoord(mapIdentifier, missionCenterCoordinate, 90, missionBoxRadius);
        if (missionEastCoordinate.getZPos()  > usableMapArea.getzMax())
        {
            double distanceToMove = missionEastCoordinate.getZPos() - usableMapArea.getzMax();
            missionCenterCoordinate = MathUtils.calcNextCoordWithMapAdjustments(mapIdentifier, missionCenterCoordinate, 270, distanceToMove);
        }
        return missionCenterCoordinate;
    }
}
