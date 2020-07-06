package pwcg.gui.rofmap.brief.model;

import pwcg.core.location.Coordinate;

public class BriefingMapPoint
{
    private Coordinate position;
    private int altitude;
    private int distanceToNextPoint;
    private boolean editable = true;
    private boolean isTarget = false;
    private String desc;

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

    public void setEditable(boolean editable)
    {
        this.editable = editable;
    }

    public boolean isTarget()
    {
        return isTarget;
    }

    public void setTarget(boolean isTarget)
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

}
