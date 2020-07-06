package pwcg.gui.rofmap.brief.model;

import pwcg.core.location.Coordinate;

public class BriefingMapPoint
{
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


    public boolean isWaypoint()
    {
        return isWaypoint;
    }

    public void setIsWaypoint(boolean isWaypoint)
    {
        this.isWaypoint = isWaypoint;
    }
}
