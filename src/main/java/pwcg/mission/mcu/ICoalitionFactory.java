package pwcg.mission.mcu;

import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;

public interface ICoalitionFactory
{
    public Coalition getCoalitionBySide(Side side);
    public Coalition getFriendlyCoalition(ICountry country);
    public List<Coalition> getAllCoalitions();
    public Coalition getEnemyCoalition(ICountry country);
}
