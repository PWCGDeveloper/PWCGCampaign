package pwcg.campaign.battle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import pwcg.campaign.context.Country;

public class AmphibiousAssault
{
    private String skirmishName;
    private Date landingStartDate;
    private Date landingStopDate;
    private Country aggressorCountry;
    private Country defendingCountry;
    private List<AmphibiousAssaultShipDefinition> shipDefinitions = new ArrayList<>();

    public String getSkirmishName()
    {
        return skirmishName;
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

    public List<AmphibiousAssaultShipDefinition> getShipDefinitions()
    {
        return shipDefinitions;
    }

    public void shuffleLandingCraft()
    {
        Collections.shuffle(shipDefinitions);
    }
}
