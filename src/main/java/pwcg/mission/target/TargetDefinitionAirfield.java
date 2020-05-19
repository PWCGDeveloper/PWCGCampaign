package pwcg.mission.target;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.group.Block;

public class TargetDefinitionAirfield
{
    private Block departureStation;
    private ICountry country;

    public Block getDepartureStation()
    {
        return departureStation;
    }

    public void setDepartureStation(Block departureStation)
    {
        this.departureStation = departureStation;
    }

    public ICountry getCountry()
    {
        return country;
    }

    public void setCountry(ICountry country)
    {
        this.country = country;
    }

}
