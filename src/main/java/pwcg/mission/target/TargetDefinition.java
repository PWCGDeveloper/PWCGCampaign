package pwcg.mission.target;

import pwcg.campaign.api.ICountry;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.IFlight;

public class TargetDefinition
{
    private TargetType targetType;
    private Coordinate position;
    private ICountry country;
    private String targetName;
    private IFlight opposingFlight;

    public TargetDefinition (TargetType targetType, Coordinate position, ICountry country, String targetName)
    {
        this.targetType = targetType;
        this.position = position;
        this.country = country;
        this.targetName = targetName;
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

    public String getTargetName()
    {
        return targetName;
    }

    public IFlight getOpposingFlight()
    {
        return opposingFlight;
    }

    public void setOpposingFlight(IFlight opposingFlight)
    {
        this.opposingFlight = opposingFlight;
    }
}
