package pwcg.gui.rofmap.brief.model;

import pwcg.core.location.Coordinate;

public class BriefingMapPoint
{
    private static long masterNewWaypointId = 1000000;
    
    private long waypointID = 0;

    private Coordinate position;
    private int altitude;
    private int distanceToNextPoint;
    private boolean editable = true;
    private boolean isTarget = false;
    private boolean isWaypoint = false;
    private String desc;
    
    public BriefingMapPoint(long waypointID)
    {
        this.waypointID = waypointID;
        ++masterNewWaypointId;
    }
    
    public BriefingMapPoint copy()
    {
        BriefingMapPoint copy = new BriefingMapPoint(masterNewWaypointId);
        copy.position = this.position.copy();
        copy.altitude = this.altitude;
        copy.distanceToNextPoint = 0;
        copy.editable = this.editable;
        copy.isTarget = this.isTarget;
        copy.isWaypoint = this.isWaypoint;
        copy.desc = this.desc;
        return copy;
    }

    public Coordinate getPosition()
    {
        return position;
    }

    public void setPosition(Coordinate position)
    {
        this.position = position;
    }

    public int getAltitude()
    {
        return altitude;
    }

    public void setAltitude(int altitude)
    {
        this.altitude = altitude;
    }

    public int getDistanceToNextPoint()
    {
        return distanceToNextPoint;
    }

    public void setDistanceToNextPoint(int distanceToNextPoint)
    {
        this.distanceToNextPoint = distanceToNextPoint;
    }

    public boolean isEditable()
    {
        return editable;
    }

    public void setIsEditable(boolean editable)
    {
        this.editable = editable;
    }

    public boolean isTarget()
    {
        return isTarget;
    }

    public void setIsTarget(boolean isTarget)
    {
        this.isTarget = isTarget;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public long getWaypointID()
    {
        return waypointID;
    }

    public void setWaypointID(long waypointID)
    {
        this.waypointID = waypointID;
    }

    public boolean isWaypoint()
    {
        return isWaypoint;
    }

    public void setIsWaypoint(boolean isWaypoint)
    {
        this.isWaypoint = isWaypoint;
    }
}
