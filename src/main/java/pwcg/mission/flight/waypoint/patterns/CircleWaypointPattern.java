package pwcg.mission.flight.waypoint.patterns;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointPriority;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuWaypoint;

public class CircleWaypointPattern 
{	 	
    private Campaign campaign;
    private List<McuWaypoint> circleWPs = new ArrayList<McuWaypoint>();
    private IFlight flight;
    private int waypointSpeed = 250;
    private int wpTriggerArea = 1000;
    private WaypointType wpType;
    private WaypointAction wpAction;
    private int legsInCircle;
    
	public CircleWaypointPattern(Campaign campaign, IFlight flight, WaypointType wpType, WaypointAction wpAction, int wpTriggerArea, int legsInCircle) throws PWCGException 
	{
        this.campaign = campaign;
        this.flight = flight;
        this.wpTriggerArea = wpTriggerArea;
        this.wpType = wpType;
        this.wpAction = wpAction;
        this.legsInCircle = legsInCircle;
	}

    public List<McuWaypoint> generateCircleWPs(McuWaypoint lastWP, double endAlt, double legDistance) throws PWCGException
    {
        int legCount = 0;
        
        double deltaAlt = (endAlt - lastWP.getPosition().getYPos()) / legsInCircle;
                        
        generateCircleWP(lastWP, endAlt, deltaAlt, legDistance, legCount);
        
        return circleWPs;
    }

    private void generateCircleWP(McuWaypoint lastWP, double endAlt, double deltaAlt, double legDistance, int legCount) throws PWCGException
    {
        McuWaypoint nextCircleWP = WaypointFactory.createDefinedWaypointType(wpType, wpAction);
        
        nextCircleWP.setTriggerArea(wpTriggerArea);
        nextCircleWP.setDesc(flight.getSquadron().determineDisplayName(campaign.getDate()), wpType.getName());
        nextCircleWP.setWpAction(wpAction);

        nextCircleWP.setSpeed(waypointSpeed - 20);
        nextCircleWP.setPriority(WaypointPriority.PRIORITY_LOW);          

        double circleWPOrientationAngle = getNextCircleWPAngle(lastWP);
        
        Orientation circleWPOrientation = new Orientation();
        circleWPOrientation.setyOri(circleWPOrientationAngle);
        nextCircleWP.setOrientation(circleWPOrientation);

        Coordinate circleCoords = MathUtils.calcNextCoord(lastWP.getPosition().copy(), circleWPOrientationAngle, legDistance);
        
        circleCoords.setYPos(lastWP.getPosition().getYPos() + deltaAlt);
        nextCircleWP.setPosition(circleCoords);
                
        circleWPs.add(nextCircleWP);
        
        if (++legCount < legsInCircle)
        {
            generateCircleWP(nextCircleWP, endAlt, deltaAlt, legDistance, legCount);
        }
    }

    private double getNextCircleWPAngle(McuWaypoint lastCircleWP) throws PWCGException
    {
        double turnAngle = 60;
        
        double circleWPOrientationAngle = MathUtils.adjustAngle(lastCircleWP.getOrientation().getyOri(), turnAngle);
                
        return circleWPOrientationAngle;
    }

}
