package pwcg.mission.flight.waypoint;

import java.util.Date;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;

public class BehindFriendlyLinesPositionCalculator
{

    public static Coordinate getPointBehindFriendlyLines(Coordinate nearFrontPosition, Coordinate homePosition, int distance, Date date, Side side) throws PWCGException
    {
        FrontLinesForMap frontLines = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(date);
        Coordinate nearbyFriendlyFrontPosition = frontLines.findCloseFrontPositionForSide(nearFrontPosition, 15000, side).getPosition();
        double angleFrontToHome = MathUtils.calcAngle(nearFrontPosition, homePosition);
        Coordinate rendezvousLocation = MathUtils.calcNextCoord(nearbyFriendlyFrontPosition, angleFrontToHome, distance);
        return rendezvousLocation;
    }
}
