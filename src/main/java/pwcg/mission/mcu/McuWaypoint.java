package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.WaypointPriority;
import pwcg.mission.flight.waypoint.WaypointType;

public class McuWaypoint extends BaseFlightMcu implements Cloneable
{
    public static long NO_WAYPOINT_ID = -1;
    private static long masterWaypointId = 1;
    
    public WaypointType waypointType;
    private WaypointAction wpAction;
    private WaypointPriority priority = WaypointPriority.PRIORITY_LOW;

    public static int TARGET_AREA = 500;
    public static int INITIAL_CLIMB_AREA = 1000;
    public static int LAND_AREA = 300;
    public static int CLIMB_AREA = 500;
    public static int START_AREA = 500;
    public static int FLIGHT_AREA = 3000;
    public static int COMBAT_AREA = 1000;
    private int triggerArea = FLIGHT_AREA;
    private int speed = 130;
    private boolean isTargetWaypoint = false;
    private long waypointID = 0;

    public McuWaypoint(WaypointType waypointType)
    {
        super();

        this.name = waypointType.getName();
        this.desc = waypointType.getName();
        this.waypointType = waypointType;
        this.waypointID = masterWaypointId;
        ++masterWaypointId;
    }

    private McuWaypoint()
    {
        super();
    }

    public McuWaypoint copy()
    {
        McuWaypoint clone = new McuWaypoint();

        clone.waypointID = waypointID;
        clone.waypointType = waypointType;
        clone.wpAction = wpAction;
        clone.priority = priority;

        clone.triggerArea = triggerArea;
        clone.speed = speed;
        clone.isTargetWaypoint = isTargetWaypoint;

        clone.name = name;
        clone.desc = desc;

        clone.position = position.copy();
        clone.orientation = orientation.copy();

        return clone;
    }

    public int getTriggerArea()
    {
        return triggerArea;
    }

    public void setTriggerArea(int area)
    {
        this.triggerArea = area;
    }

    public int getSpeed()
    {
        return speed;
    }

    public void setSpeed(int speed)
    {
        this.speed = speed;
    }

    public WaypointPriority getPriority()
    {
        return priority;
    }

    public void setPriority(WaypointPriority priority)
    {
        this.priority = priority;
    }

    public void setWaypointAltitude(int altitude)
    {
        this.position.setYPos(altitude);
    }

    public WaypointAction getWpAction()
    {
        return wpAction;
    }

    public void setWpAction(WaypointAction wpAction)
    {
        this.wpAction = wpAction;
    }

    public void setTarget(int target)
    {
        super.setTarget(target);
    }

    public boolean isTargetWaypoint()
    {
        return isTargetWaypoint;
    }

    public void setTargetWaypoint(boolean isTargetWaypoint)
    {
        this.isTargetWaypoint = isTargetWaypoint;
    }

    public void setDesc(String name, String desc)
    {
        this.desc = name + ": " + desc;
    }

    public WaypointType getWaypointType()
    {
        return waypointType;
    }
    
    public long getWaypointID()
    {
        return waypointID;
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        try
        {
            writer.write("MCU_Waypoint");
            writer.newLine();
            writer.write("{");
            writer.newLine();

            super.write(writer);

            writer.write("  Area = " + triggerArea + ";");
            writer.newLine();
            writer.write("  Speed = " + speed + ";");
            writer.newLine();
            writer.write("  Priority = " + priority.getPriorityValue() + ";");
            writer.newLine();

            writer.write("}");
            writer.newLine();
            writer.newLine();
            writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
    }

    public void dump()
    {
        PWCGLogger.log(LogLevel.DEBUG, "MCU_Waypoint");
        PWCGLogger.log(LogLevel.DEBUG, "    " + desc);
        PWCGLogger.log(LogLevel.DEBUG, "    " + position.getXPos() + "   " + position.getZPos() + "   " + position.getYPos());
    }

}
