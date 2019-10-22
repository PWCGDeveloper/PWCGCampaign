package pwcg.mission.ground;

import java.util.Date;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.CountryDesignator;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.target.TacticalTarget;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.MissionBeginUnit;

public class GroundUnitInformation
{
    protected ICountry country = CountryFactory.makeCountryByCountry(Country.NEUTRAL);
    protected MissionBeginUnit missionBeginUnit = null;
    protected String name = "";
    protected Date date;
    protected Coordinate position = new Coordinate();
    protected Coordinate destination = new Coordinate();
	protected Orientation orientation = new Orientation();
    protected TacticalTarget targetType = TacticalTarget.TARGET_NONE;
    protected GroundUnitSize unitSize = GroundUnitSize.GROUND_UNIT_SIZE_TINY;

	public ICountry getCountry(Date date) throws PWCGException
	{
		CountryDesignator countryDesignator = new CountryDesignator();
		return countryDesignator.determineCountry(position, date);
	}

    public ICountry getCountry()
    {
        return country;
    }

    public void setCountry(ICountry country)
    {
        this.country = country;
    }

    public MissionBeginUnit getMissionBeginUnit()
    {
        return missionBeginUnit;
    }

    public void setMissionBeginUnit(MissionBeginUnit missionBeginUnit)
    {
        this.missionBeginUnit = missionBeginUnit;
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

    public TacticalTarget getTargetType()
    {
        return targetType;
    }

    public void setTargetType(TacticalTarget targetType)
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
}
