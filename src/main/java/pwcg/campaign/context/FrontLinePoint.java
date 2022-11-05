
package pwcg.campaign.context;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class FrontLinePoint extends PWCGLocation
{
    public static String ALLIED_FRONT_LINE = "AlliedFrontLine";
    public static String AXIS_FRONT_LINE = "AxisFrontLine";
    
    protected ICountry country = CountryFactory.makeNeutralCountry();

    public FrontLinePoint ()
    {
    }

    public ICountry getCountry()
    {
        return country;
    }

    public Side getSide()
    {
        return country.getSide();
    }


    public void setCountry(ICountry country)
    {
        this.country = country;
        
        if (country.getSide() == Side.ALLIED)
        {
            setName(ALLIED_FRONT_LINE);
        }
        else
        {
            setName(AXIS_FRONT_LINE);
        }
    }
    

    private void setCountryFromName(FrontMapIdentifier mapIdentifier, String name)
    {
        if (name.equals(ALLIED_FRONT_LINE))
        {
            country = CountryFactory.makeMapReferenceCountry(mapIdentifier, Side.ALLIED);
        }
        else if (name.equals(AXIS_FRONT_LINE))
        {
            country = CountryFactory.makeMapReferenceCountry(mapIdentifier, Side.AXIS);
        }
        else
        {
            PWCGLogger.log(LogLevel.ERROR, "Unidentifiable name for front line location " + name);
        }
    }
    
    public void setOrientation(double angle) throws PWCGException 
    {
        getOrientation().setyOri(angle);
    }

    public void setName(FrontMapIdentifier mapIdentifier, String name)
    {
        this.name = name;
        setCountryFromName(mapIdentifier, name);
    }

    public PWCGLocation getLocation()
    {
        return this;
    }

    public void setLocation(FrontMapIdentifier mapIdentifier,PWCGLocation location)
    {
        setFromLocation(location);
        setCountryFromName(mapIdentifier, getName());
    }
}
