package pwcg.mission.target;

import pwcg.campaign.api.ICountry;
import pwcg.core.location.Coordinate;

public class TargetDefinition
{
    private TargetType targetType;
    private Coordinate position;
    private ICountry country;
    
    public TargetDefinition (TargetType targetType, Coordinate position, ICountry country)
    {
        this.targetType = targetType;
        this.position = position;
        this.country = country;
    }

    public TargetType getTargetType()
    {
        return targetType;
    }

    public Coordinate getPosition()
    {
        return position;
    }

    public ICountry getCountry()
    {
        return country;
    }

    public TargetCategory getTargetCategory()
    {
        return targetType.getTargetCategory();
    }
}
