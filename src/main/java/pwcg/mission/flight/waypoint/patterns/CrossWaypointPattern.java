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

/**
 * Create a cross search pattern. 
 * A cross search pattern looks like a sideways bar graph
 * 
 * @author U071098
 *
 */
public class CrossWaypointPattern 
{       
    private static final int MAX_CROSS_SEGMENTS = 3;
    
    private Campaign campaign;
    private IFlight flight;
    private List<McuWaypoint> crossWPs = new ArrayList<McuWaypoint>();
    private int wpTriggerArea = 1000;
    private WaypointType waypointType;
    private WaypointAction waypointAction;
    
    public CrossWaypointPattern(Campaign campaign, IFlight flight, WaypointType waypointType, WaypointAction waypointAction, int wpTriggerArea) throws PWCGException 
    {
        this.campaign = campaign;
        this.flight = flight;
        this.wpTriggerArea = wpTriggerArea;
        this.waypointType = waypointType;
        this.waypointAction = waypointAction;
    }

    /**
     * Recursively generate cross, altering altitude per the request
     * 
     * @param missionWPs
     * @throws PWCGException 
     */
    public List<McuWaypoint> generateCrossWPSegments(McuWaypoint lastWP, double legDistance) throws PWCGException
    {
        int legCount = 0;
             
        generateCrossWPSegment(lastWP, legDistance, lastWP.getOrientation().getyOri(), legCount);
        
        return crossWPs;
    }

    /**
     * Recursively generate cross segments.  
     * A cross pattern looks like a  bar graph
     * 
     * @param missionWPs
     * @throws PWCGException 
     */
    private void generateCrossWPSegment(McuWaypoint lastWP, double legDistance, double orientation, int legCount) throws PWCGException
    {
        // A leg is two waypoints, a long leg (diameter) and then a shorter one 
        // (chord the size of radius)
        lastWP = generateCrossWP(lastWP, legDistance, orientation);

        double nextOrientation = MathUtils.adjustAngle(orientation, 120);
        lastWP = generateCrossWP(lastWP, legDistance/2, nextOrientation);
        
        ++legCount;
        if (legCount < MAX_CROSS_SEGMENTS)
        {
            nextOrientation = MathUtils.adjustAngle(nextOrientation, 120);
            generateCrossWPSegment(lastWP, legDistance, nextOrientation, legCount);
        }
    }
    
    

    /**
     * Generate a single waypoint in the cross segment
     * 
     * @param missionWPs
     * @throws PWCGException 
     */
    private McuWaypoint generateCrossWP(McuWaypoint lastWP, double legDistance, double orientation) throws PWCGException
    {
        McuWaypoint nextCrossWP = WaypointFactory.createDefinedWaypointType(waypointType, waypointAction);
        
        nextCrossWP.setTriggerArea(wpTriggerArea);
        nextCrossWP.setDesc(flight.getFlightInformation().getFlightName(), waypointType.getName());

        nextCrossWP.setSpeed(lastWP.getSpeed());
        nextCrossWP.setPriority(WaypointPriority.PRIORITY_LOW);          
        
        Orientation crossWPOrientation = new Orientation();
        crossWPOrientation.setyOri(orientation);
        nextCrossWP.setOrientation(crossWPOrientation);

        Coordinate crossCoords = MathUtils.calcNextCoord(lastWP.getPosition().copy(), orientation, legDistance);
        
        crossCoords.setYPos(lastWP.getPosition().getYPos());
        nextCrossWP.setPosition(crossCoords);
        
        crossWPs.add(nextCrossWP);
        
        return nextCrossWP;
    }
}
