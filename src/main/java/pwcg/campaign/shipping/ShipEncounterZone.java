package pwcg.campaign.shipping;

import java.util.Date;

import pwcg.core.location.Coordinate;

public class ShipEncounterZone
{
    private String name;
    private Date startDate;
    private Date endDate;
    private Coordinate swCorner;
    private Coordinate neCorner;
    private Coordinate encounterPoint;

    public ShipEncounterZone copy()
    {
        ShipEncounterZone copy = new ShipEncounterZone();
        copy.name = this.name;
        copy.startDate = this.startDate;
        copy.endDate = this.endDate;
        copy.swCorner = this.swCorner.copy();
        copy.neCorner = this.neCorner.copy();
        copy.encounterPoint = this.encounterPoint.copy();
        return copy;
    }

    public String getName()
    {
        return name;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public Coordinate getSwCorner()
    {
        return swCorner;
    }

    public Coordinate getNeCorner()
    {
        return neCorner;
    }

    public Coordinate getEncounterPoint()
    {
        return encounterPoint;
    }

    public void setEncounterPoint(Coordinate encounterPoint)
    {
        this.encounterPoint = encounterPoint;
    }
}
