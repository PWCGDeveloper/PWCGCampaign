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
 * Create a creeping search pattern. 
 * A creeping search pattern looks like a sideways bar graph
 * 
 * @author U071098
 *
 */
public class CreepingLinePattern 
{       
    private static final int MAX_CREEP_SEGMENTS = 3;
    
    private Campaign campaign;
    List<McuWaypoint> creepingWPs = new ArrayList<McuWaypoint>();
    private IFlight flight;
    private int wpTriggerArea = 1000;
    private WaypointType waypointType;
    private WaypointAction waypointAction;
    private int legsInCreeping = MAX_CREEP_SEGMENTS;
    private double initialOrientation;

    public CreepingLinePattern(
            Campaign campaign, 
            IFlight flight, 
            WaypointType waypointType, 
            WaypointAction waypointAction, 
            int wpTriggerArea, 
            int legsInCreeping,
            double initialOrientation) throws PWCGException 
    {
        this.campaign = campaign;
        this.flight = flight;
        this.wpTriggerArea = wpTriggerArea;
        this.waypointType = waypointType;
        this.waypointAction = waypointAction;
        this.legsInCreeping = legsInCreeping;
        this.initialOrientation = initialOrientation;
    }

    public List<McuWaypoint> generateCreepingWPSegments(McuWaypoint lastWP, double legDistance, double connectSegmentDistance) throws PWCGException
    {
        int legCount = 0;
                        
        generateCreepingWPSegment(lastWP, legDistance, connectSegmentDistance, legCount);
        
        return creepingWPs;
    }

    private void generateCreepingWPSegment(McuWaypoint lastWP, double legDistance, double connectSegmentDistance, int legCount) throws PWCGException
    {
        lastWP = generateCreepingWP(lastWP, connectSegmentDistance, initialOrientation);
        lastWP = generateCreepingWP(lastWP, legDistance, MathUtils.adjustAngle(initialOrientation, 90));
        lastWP = generateCreepingWP(lastWP, connectSegmentDistance, initialOrientation);
        lastWP = generateCreepingWP(lastWP, legDistance, MathUtils.adjustAngle(initialOrientation, -90));
        
        ++legCount;
        if (legCount < legsInCreeping)
        {
            generateCreepingWPSegment(lastWP, legDistance, connectSegmentDistance, legCount);
        }
    }

    private McuWaypoint generateCreepingWP(McuWaypoint lastWP, double legDistance, double orientation) throws PWCGException
    {
        McuWaypoint nextCreepingWP = WaypointFactory.createDefinedWaypointType(waypointType, waypointAction);
        
        nextCreepingWP.setTriggerArea(wpTriggerArea);
        nextCreepingWP.setDesc(flight.getFlightInformation().getFlightName(), waypointType.getName());

        nextCreepingWP.setSpeed(lastWP.getSpeed());
        nextCreepingWP.setPriority(WaypointPriority.PRIORITY_LOW);          
        
        Orientation creepingWPOrientation = new Orientation();
        creepingWPOrientation.setyOri(orientation);
        nextCreepingWP.setOrientation(creepingWPOrientation);

        Coordinate creepingCoords = MathUtils.calcNextCoord(lastWP.getPosition().copy(), orientation, legDistance);
        
        creepingCoords.setYPos(lastWP.getPosition().getYPos());
        nextCreepingWP.setPosition(creepingCoords);
        
        creepingWPs.add(nextCreepingWP);
        
        return nextCreepingWP;
    }
}
