package pwcg.mission.target;

import pwcg.campaign.api.ICountry;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;

public class TargetDefinition
{
    private Coordinate position;
    private Orientation orientation;
    private ICountry country;
    
    public Coordinate getPosition()
    {
        return position;
    }

    public void setPosition(Coordinate position)
    {
        this.position = position;
    }

    public Orientation getOrientation()
    {
        return orientation;
    }

    public void setOrientation(Orientation orientation)
    {
        this.orientation = orientation;
    }

    public ICountry getCountry()
    {
        return country;
    }

    public void setCountry(ICountry country)
    {
        this.country = country;
    }

}
