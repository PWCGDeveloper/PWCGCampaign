package pwcg.mission;

import pwcg.campaign.context.MapArea;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;

public class MissionCenterAdjuster
{

    public static Coordinate keepWithinMap(Coordinate missionCenterCoordinate, int missionBoxRadius, MapArea usableMapArea) throws PWCGException
    {
        missionCenterCoordinate = adjustForSouthEdge(missionCenterCoordinate, missionBoxRadius, usableMapArea);
        missionCenterCoordinate = adjustForNorthEdge(missionCenterCoordinate, missionBoxRadius, usableMapArea);
        missionCenterCoordinate = adjustForWestEdge(missionCenterCoordinate, missionBoxRadius, usableMapArea);
        missionCenterCoordinate = adjustForEastEdge(missionCenterCoordinate, missionBoxRadius, usableMapArea);
        return missionCenterCoordinate;
    }

    private static Coordinate adjustForSouthEdge(Coordinate missionCenterCoordinate, int missionBoxRadius, MapArea usableMapArea) throws PWCGException
    {
        Coordinate missionSouthCoordinate = MathUtils.calcNextCoord(missionCenterCoordinate, 180, missionBoxRadius);
        if (missionSouthCoordinate.getXPos()  < usableMapArea.getxMin())
        {
            double distanceToMove = usableMapArea.getxMin() - missionSouthCoordinate.getXPos();
            missionCenterCoordinate = MathUtils.calcNextCoordWithMapAdjustments(missionCenterCoordinate, 0, distanceToMove);
        }
        return missionCenterCoordinate;
    }

    private static Coordinate adjustForNorthEdge(Coordinate missionCenterCoordinate, int missionBoxRadius, MapArea usableMapArea) throws PWCGException
    {
        Coordinate missionNorthCoordinate = MathUtils.calcNextCoord(missionCenterCoordinate, 0, missionBoxRadius);
        if (missionNorthCoordinate.getXPos()  > usableMapArea.getxMax())
        {
            double distanceToMove = missionNorthCoordinate.getXPos() - usableMapArea.getxMax();
            missionCenterCoordinate = MathUtils.calcNextCoordWithMapAdjustments(missionCenterCoordinate, 180, distanceToMove);
        }
        return missionCenterCoordinate;
    }

    private static Coordinate adjustForWestEdge(Coordinate missionCenterCoordinate, int missionBoxRadius, MapArea usableMapArea) throws PWCGException
    {
        Coordinate missionWestCoordinate = MathUtils.calcNextCoord(missionCenterCoordinate, 270, missionBoxRadius);
        if (missionWestCoordinate.getZPos()  < usableMapArea.getzMin())
        {
            double distanceToMove = usableMapArea.getzMin() - missionWestCoordinate.getZPos();
            missionCenterCoordinate = MathUtils.calcNextCoordWithMapAdjustments(missionCenterCoordinate, 90, distanceToMove);
        }
        return missionCenterCoordinate;
    }

    private static Coordinate adjustForEastEdge(Coordinate missionCenterCoordinate, int missionBoxRadius, MapArea usableMapArea) throws PWCGException
    {
        Coordinate missionEastCoordinate = MathUtils.calcNextCoord(missionCenterCoordinate, 90, missionBoxRadius);
        if (missionEastCoordinate.getZPos()  > usableMapArea.getzMax())
        {
            double distanceToMove = missionEastCoordinate.getZPos() - usableMapArea.getzMax();
            missionCenterCoordinate = MathUtils.calcNextCoordWithMapAdjustments(missionCenterCoordinate, 270, distanceToMove);
        }
        return missionCenterCoordinate;
    }
}
