package pwcg.campaign.battle;

import java.util.Date;

import pwcg.campaign.context.Country;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class Battle
{
    private String name;
	private Coordinate neCorner;
	private Coordinate swCorner;
    private Country aggressorcountry;
    private Country defendercountry;
    private Date startDate;
    private Date stopDate;
    private FrontMapIdentifier map;

	public Battle()
	{
	}

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Coordinate getSWCorner()
    {
        return swCorner;
    }

    public void setSWCorner(Coordinate swCorner)
    {
        this.swCorner = swCorner;
    }
    
    public Coordinate getNECorner()
    {
        return neCorner;
    }
    
    public void setNECorner(Coordinate neCorner)
    {
        this.neCorner = neCorner;
    }

    public Country getAggressorcountry()
    {
        return aggressorcountry;
    }

    public void setAggressorcountry(Country aggressorcountry)
    {
        this.aggressorcountry = aggressorcountry;
    }

    public Country getDefendercountry()
    {
        return defendercountry;
    }

    public void setDefendercountry(Country defendercountry)
    {
        this.defendercountry = defendercountry;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Date getStopDate()
    {
        return stopDate;
    }

    public void setStopDate(Date stopDate)
    {
        this.stopDate = stopDate;
    }

    public FrontMapIdentifier getMap()
    {
        return map;
    }

    public void setMap(FrontMapIdentifier map)
    {
        this.map = map;
    }
    
    public void dump()
    {
        PWCGLogger.log(LogLevel.DEBUG, name);
        PWCGLogger.log(LogLevel.DEBUG, startDate.toString());
        PWCGLogger.log(LogLevel.DEBUG, stopDate.toString());
        PWCGLogger.log(LogLevel.DEBUG, swCorner.toString());
        PWCGLogger.log(LogLevel.DEBUG, neCorner.toString());
        PWCGLogger.log(LogLevel.DEBUG, "" + map);
    }
}
