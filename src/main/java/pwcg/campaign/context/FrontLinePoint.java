
package pwcg.campaign.context;

import java.util.Date;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.core.utils.MathUtils;

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
    

    private void setCountryFromName(String name)
    {
        if (name.equals(ALLIED_FRONT_LINE))
        {
            country = CountryFactory.makeMapReferenceCountry(Side.ALLIED);
        }
        else if (name.equals(AXIS_FRONT_LINE))
        {
            country = CountryFactory.makeMapReferenceCountry(Side.AXIS);
        }
        else
        {
            PWCGLogger.log(LogLevel.ERROR, "Unidentifiable name for front line location " + name);
        }
    }

    public double getOrientation(Date date) throws PWCGException
    {
        if (getName().equals(FrontLinePoint.ALLIED_FRONT_LINE))
        {
            FrontLinesForMap frontLinesForMap =  PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(date);
            Coordinate closestAxis = frontLinesForMap.findClosestFrontCoordinateForSide(this.getPosition(), Side.AXIS);
            return MathUtils.calcAngle(this.getPosition(), closestAxis);
        }
        else
        {   
            FrontLinesForMap frontLinesForMap =  PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(date);
            Coordinate closestAllied = frontLinesForMap.findClosestFrontCoordinateForSide(this.getPosition(), Side.ALLIED);
            return MathUtils.calcAngle(this.getPosition(), closestAllied);
        }        
    }
    
    public void setOrientation(double angle) throws PWCGException 
    {
        getOrientation().setyOri(angle);
    }

    public void setName(String name)
    {
        this.name = name;
        setCountryFromName(name);
    }

    public PWCGLocation getLocation()
    {
        return this;
    }

    public void setLocation(PWCGLocation location)
    {
        setFromLocation(location);
        setCountryFromName(getName());
    }

    public static String getAlliedFrontLine()
    {
        return ALLIED_FRONT_LINE;
    }

    public static void setAlliedFrontLine(String alliedFrontLine)
    {
        ALLIED_FRONT_LINE = alliedFrontLine;
    }

    public static String getAxisFrontLine()
    {
        return AXIS_FRONT_LINE;
    }

    public static void setAxisFrontLine(String axisFrontLine)
    
    {
        AXIS_FRONT_LINE = axisFrontLine;
    }
 
    
}
