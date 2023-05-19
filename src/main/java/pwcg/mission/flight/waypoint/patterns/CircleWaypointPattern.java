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

    public List<McuWaypoint> generateCircleWPs(Coordinate circleCenter, double entryAngle, double entryAlt, double endAlt, double legDistance) throws PWCGException
    {
        double deltaAlt = (endAlt - entryAlt) / legsInCircle;                        
        startGenerateCircleWP(circleCenter, entryAngle, entryAlt, endAlt, deltaAlt, legDistance);
        return circleWPs;
    }

    private void startGenerateCircleWP(Coordinate circleCenter, double entryAngle, double entryAlt, double endAlt, double deltaAlt, double legDistance) throws PWCGException
    {
        McuWaypoint firstCircleWP = createCircleWaypoint();
        
        Coordinate circleCoords = MathUtils.calcNextCoord(flight.getCampaignMap(), circleCenter, entryAngle, legDistance);
        circleCoords.setYPos(entryAlt);
        firstCircleWP.setPosition(circleCoords);

        double circleLegAngle = (360.0 / legsInCircle);
        double circleWPOrientationAngle = MathUtils.adjustAngle(entryAngle, circleLegAngle);
        Orientation circleWPOrientation = new Orientation(circleWPOrientationAngle);
        firstCircleWP.setOrientation(circleWPOrientation);

        circleWPs.add(firstCircleWP);
        
        generateCircleWP(firstCircleWP, endAlt, deltaAlt, legDistance, 1);

    }
    
    private void generateCircleWP(McuWaypoint lastWP, double endAlt, double deltaAlt, double legDistance, int legCount) throws PWCGException
    {
        McuWaypoint nextCircleWP = createCircleWaypoint();

        double circleWPOrientationAngle = getNextCircleWPAngle(lastWP);
        
        Orientation circleWPOrientation = new Orientation(circleWPOrientationAngle);
        nextCircleWP.setOrientation(circleWPOrientation);

        Coordinate circleCoords = MathUtils.calcNextCoord(flight.getCampaignMap(), lastWP.getPosition().copy(), circleWPOrientationAngle, legDistance);
        
        circleCoords.setYPos(lastWP.getPosition().getYPos() + deltaAlt);

        nextCircleWP.setPosition(circleCoords);

        circleWPs.add(nextCircleWP);
        
        if (++legCount < legsInCircle)
        {
            generateCircleWP(nextCircleWP, endAlt, deltaAlt, legDistance, legCount);
        }
    }

    private McuWaypoint createCircleWaypoint() throws PWCGException
    {
        McuWaypoint nextCircleWP = WaypointFactory.createDefinedWaypointType(wpType, wpAction);
        
        nextCircleWP.setTriggerArea(wpTriggerArea);
        nextCircleWP.setDesc(flight.getSquadron().determineDisplayName(campaign.getDate()), wpType.getName());
        nextCircleWP.setWpAction(wpAction);

        int climbSpeed = calculateClimbSpeed(flight.getFlightCruisingSpeed());
        nextCircleWP.setSpeed(climbSpeed);
        nextCircleWP.setPriority(WaypointPriority.PRIORITY_LOW);
        return nextCircleWP;
    }
    
    
    private int calculateClimbSpeed(int cruisingSpeed)
    {
        if (cruisingSpeed < 100)
        {
            return cruisingSpeed - 10;
        }
        else if (cruisingSpeed < 150)
        {
            return cruisingSpeed - 20;
        }
        else if (cruisingSpeed < 250)
        {
            return cruisingSpeed - 30;
        }
        else
        {
            return cruisingSpeed - 40;
        }
    }

    private double getNextCircleWPAngle(McuWaypoint lastCircleWP) throws PWCGException
    {
        double turnAngle = 60;
        
        double circleWPOrientationAngle = MathUtils.adjustAngle(lastCircleWP.getOrientation().getyOri(), turnAngle);
                
        return circleWPOrientationAngle;
    }

}
