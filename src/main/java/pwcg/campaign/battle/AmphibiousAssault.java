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
    private int landingCraftBackOff;
    private List<AmphibiousAssaultShipDefinition> shipDefinitions = new ArrayList<>();

    public AmphibiousAssault ()
    {
        
    }

    public AmphibiousAssault (String skirmishName)
    {
        this.skirmishName = skirmishName;
    }
    
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

    public int getLandingCraftBackOff()
    {
        return landingCraftBackOff;
    }

    public void shuffleLandingCraft()
    {
        Collections.shuffle(shipDefinitions);
    }

    public void setSkirmishName(String skirmishName)
    {
        this.skirmishName = skirmishName;
    }

    public void setLandingStartDate(Date landingStartDate)
    {
        this.landingStartDate = landingStartDate;
    }

    public void setLandingStopDate(Date landingStopDate)
    {
        this.landingStopDate = landingStopDate;
    }

    public void setAggressorCountry(Country aggressorCountry)
    {
        this.aggressorCountry = aggressorCountry;
    }

    public void setDefendingCountry(Country defendingCountry)
    {
        this.defendingCountry = defendingCountry;
    }

    public void setLandingCraftBackOff(int landingCraftBackOff)
    {
        this.landingCraftBackOff = landingCraftBackOff;
    }

    public void setShipDefinitions(List<AmphibiousAssaultShipDefinition> shipDefinitions)
    {
        this.shipDefinitions = shipDefinitions;
    }

    public void addAssaultShip(AmphibiousAssaultShipDefinition amphibiousAssaultShipDefinition)
    {
        shipDefinitions.add(amphibiousAssaultShipDefinition);        
    }
    
    
}
