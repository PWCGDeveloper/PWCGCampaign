package pwcg.mission.flight.waypoint.patterns;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuWaypoint;

public class WaypointPatternFactory
{

    public static List<McuWaypoint> generateCirclePattern(
    				Campaign campaign,
                    IFlight flight, 
                    WaypointType wpType, 
                    WaypointAction wpAction, 
                    int wpTriggerArea, 
                    int legsInCircle,
                    McuWaypoint lastWP, 
                    double endAlt, 
                    double legDistance) throws PWCGException
    {
        CircleWaypointPattern circleWaypointGenerator = new CircleWaypointPattern(campaign, flight, wpType, wpAction, wpTriggerArea, legsInCircle);

        List<McuWaypoint> waypoints = circleWaypointGenerator.generateCircleWPs(
                lastWP.getPosition(), 
                lastWP.getOrientation().getyOri(), 
                lastWP.getPosition().getYPos(),
                endAlt, 
                legDistance);


        return waypoints;
    }

    public static List<McuWaypoint> generateCreepingPattern(
    		Campaign campaign, 
            IFlight flight, 
            WaypointType wpType, 
            WaypointAction wpAction, 
            int wpTriggerArea, 
            int legsInCreeping,
            McuWaypoint lastWP, 
            double legDistance, 
            double connectSegmentDistance,
            double initialOrientation) throws PWCGException
    {
        CreepingLinePattern pattern = new CreepingLinePattern(campaign, flight, wpType, wpAction, wpTriggerArea, legsInCreeping, initialOrientation);

        List<McuWaypoint> waypoints = pattern.generateCreepingWPSegments(lastWP, legDistance, connectSegmentDistance);

        return waypoints;
    }


    public static List<McuWaypoint> generateCrossPattern(
    		Campaign campaign, 
    		IFlight flight, 
    		WaypointType wpType, 
    		WaypointAction wpAction, 
    		int wpTriggerArea, 
    		McuWaypoint lastWP, 
    		double legDistance) throws PWCGException
    {
        CrossWaypointPattern pattern = new CrossWaypointPattern(campaign, flight, wpType, wpAction, wpTriggerArea);

        List<McuWaypoint> waypoints = pattern.generateCrossWPSegments(lastWP, legDistance);

        return waypoints;
    }
}
