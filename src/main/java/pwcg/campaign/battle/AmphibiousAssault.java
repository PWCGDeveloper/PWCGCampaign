package pwcg.campaign.battle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.context.Country;

public class AmphibiousAssault
{
    private String name;
    private Date landingStartDate;
    private Date landingStopDate;
    private Country aggressorCountry;
    private Country defendingCountry;
    private List<AmphibiousAssaultShip> ships = new ArrayList<>();

    public String getName()
    {
        return name;
    }

    public Date getLandingStartDate()
    {
        return landingStartDate;
    }

    public Date getLandingStopDate()
    {
        return landingStopDate;
    }

    public Country getAggressorCountry()
    {
        return aggressorCountry;
    }
    
    public Country getDefendingCountry()
    {
        return defendingCountry;
    }

    public List<AmphibiousAssaultShip> getShips()
    {
        return ships;
    }

}
