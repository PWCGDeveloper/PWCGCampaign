package pwcg.campaign.target;

import java.util.Date;

import pwcg.campaign.api.Side;
import pwcg.core.location.Coordinate;

public class TargetTypeAvailabilityInputs
{
    private Coordinate targetGeneralLocation;
    private double preferredDistance;
    private double maxDistance;
    private Side side;
    private Date date;

    public Coordinate getTargetGeneralLocation()
    {
        return targetGeneralLocation;
    }

    public void setTargetGeneralLocation(Coordinate targetGeneralLocation)
    {
        this.targetGeneralLocation = targetGeneralLocation;
    }

    public double getPreferredDistance()
    {
        return preferredDistance;
    }

    public void setPreferredDistance(double preferredDistance)
    {
        this.preferredDistance = preferredDistance;
    }

    public double getMaxDistance()
    {
        return maxDistance;
    }

    public void setMaxDistance(double maxDistance)
    {
        this.maxDistance = maxDistance;
    }

    public Side getSide()
    {
        return side;
    }

    public void setSide(Side side)
    {
        this.side = side;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

}
