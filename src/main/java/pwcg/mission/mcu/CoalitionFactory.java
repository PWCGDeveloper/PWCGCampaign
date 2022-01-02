package pwcg.mission.mcu;

import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.product.bos.config.BoSCoalitionFactory;

public class CoalitionFactory
{
    public static Coalition getCoalitionBySide(Side side)
    {
        ICoalitionFactory coalitionFactory = buildCoalitionFactory();
        return coalitionFactory.getCoalitionBySide(side);
    }    

    public static Coalition getFriendlyCoalition(ICountry country)
    {
        ICoalitionFactory coalitionFactory = buildCoalitionFactory();
        return coalitionFactory.getFriendlyCoalition(country);
    }    

    public static List<Coalition> getAllCoalitions()
    {
        ICoalitionFactory coalitionFactory = buildCoalitionFactory();
        return coalitionFactory.getAllCoalitions();
    }    

    public static Coalition getEnemyCoalition(ICountry country)
    {
        ICoalitionFactory coalitionFactory = buildCoalitionFactory();
        return coalitionFactory.getEnemyCoalition(country);
    }
    
    private static ICoalitionFactory buildCoalitionFactory()
    {
        return new BoSCoalitionFactory();
    }
}
