package pwcg.mission.ground;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.target.TargetType;

public class GroundUnitInformation
{
    private ICountry country = CountryFactory.makeCountryByCountry(Country.NEUTRAL);
    private Campaign campaign;
    private String name = "";
    private Date date;
    private Coordinate position = new Coordinate();
    private Coordinate destination = new Coordinate();
	private Orientation orientation = new Orientation();
    private TargetType targetType = TargetType.TARGET_NONE;
    private GroundUnitSize unitSize = GroundUnitSize.GROUND_UNIT_SIZE_TINY;
    private String requestedUnitType = "";
    
    public GroundUnitInformation(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public ICountry getCountry()
    {
        return country;
    }

    public void setCountry(ICountry country)
    {
        this.country = country;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Coordinate getPosition()
    {
        return position;
    }

    public void setPosition(Coordinate position)
    {
        this.position = position;
    }

    public Coordinate getDestination()
    {
        return destination;
    }

    public void setDestination(Coordinate destination)
    {
        this.destination = destination;
    }

    public Orientation getOrientation()
    {
        return orientation;
    }

    public void setOrientation(Orientation orientation)
    {
        this.orientation = orientation;
    }

    public TargetType getTargetType()
    {
        return targetType;
    }

    public void setTargetType(TargetType targetType)
    {
        this.targetType = targetType;
    }

    public GroundUnitSize getUnitSize()
    {
        return unitSize;
    }

    public void setUnitSize(GroundUnitSize unitSize)
    {
        this.unitSize = unitSize;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public String getRequestedUnitType()
    {
        return requestedUnitType;
    }

    public void setRequestedUnitType(String requestedUnitType)
    {
        this.requestedUnitType = requestedUnitType;
    }

    public Campaign getCampaign()
    {
        return campaign;
    }

    public FrontMapIdentifier getCampaignMap() throws PWCGException
    {
        return campaign.getCampaignMap();
    }
}
