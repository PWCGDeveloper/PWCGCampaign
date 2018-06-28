package pwcg.core.location;

import java.util.Date;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.CountryDesignator;
import pwcg.core.exception.PWCGException;

public class PWCGLocation
{
	protected String name = "";
	protected Coordinate position = new Coordinate();
	protected Orientation orientation = new Orientation();
	
	public void setFromLocation(PWCGLocation referenceLocation)
	{
        this.name = referenceLocation.name;
        this.position = referenceLocation.position;
        this.orientation = referenceLocation.orientation;
	}
	
    public void initialize(Coordinate referenceLocation, String name)
    {
        this.name = name;
        this.position = referenceLocation;
        this.orientation = new Orientation();
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

	public Orientation getOrientation()
	{
		return orientation;
	}

	public void setOrientation(Orientation orientation)
	{
		this.orientation = orientation;
	}

	public ICountry getCountry(Date date) throws PWCGException
	{
		CountryDesignator countryDesignator = new CountryDesignator();
		return countryDesignator.determineCountry(position, date);
	}

}
