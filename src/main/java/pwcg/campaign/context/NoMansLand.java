package pwcg.campaign.context;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;

public class NoMansLand
{
    public static boolean inNoMansLand(Campaign campaign, Coordinate sampleCoordinates) throws PWCGException
    {
        FrontLinesForMap frontLinesForMap = PWCGContext.getInstance().getMapByMapId(campaign.getCampaignMap()).getFrontLinesForMap(campaign.getDate());
        Coordinate alliedClosestFrontCoordinates = frontLinesForMap.findClosestFrontCoordinateForSide(sampleCoordinates, Side.ALLIED);
        Coordinate axisClosestFrontCoordinates = frontLinesForMap.findClosestFrontCoordinateForSide(alliedClosestFrontCoordinates, Side.AXIS);

        double angleToAlliedLines = MathUtils.calcAngle(sampleCoordinates, alliedClosestFrontCoordinates);
        double angleToAxisLines = MathUtils.calcAngle(sampleCoordinates, axisClosestFrontCoordinates);

        double differenceInAngle = angleToAxisLines - angleToAlliedLines;
        if (Math.abs(differenceInAngle) > 100)
        {
            return true;
        }

        return false;
    }
}
