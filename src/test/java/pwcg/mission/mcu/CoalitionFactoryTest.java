package pwcg.mission.mcu;

import java.util.List;

import org.junit.jupiter.api.Test;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;

public class CoalitionFactoryTest
{
    private static ICountry britain;
    private static ICountry germany;
    
    public CoalitionFactoryTest() throws PWCGException
    {
        britain = CountryFactory.makeCountryByCountry(Country.BRITAIN);
        germany = CountryFactory.makeCountryByCountry(Country.GERMANY);
    }
    
    @Test
    public void testCoalitionBySide() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Coalition coalition = CoalitionFactory.getCoalitionBySide(Side.ALLIED);
        assert(coalition == Coalition.COALITION_ALLIED);

        coalition = CoalitionFactory.getCoalitionBySide(Side.AXIS);
        assert(coalition == Coalition.COALITION_AXIS);

        PWCGContext.setProduct(PWCGProduct.BOS);
        coalition = CoalitionFactory.getCoalitionBySide(Side.ALLIED);
        assert(coalition == Coalition.COALITION_ENTENTE);

        coalition = CoalitionFactory.getCoalitionBySide(Side.AXIS);
        assert(coalition == Coalition.COALITION_CENTRAL);
    }
    
    @Test
    public void testFriendlyCoalition() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Coalition coalition = CoalitionFactory.getFriendlyCoalition(britain);
        assert(coalition == Coalition.COALITION_ALLIED);

        coalition = CoalitionFactory.getFriendlyCoalition(germany);
        assert(coalition == Coalition.COALITION_AXIS);

        PWCGContext.setProduct(PWCGProduct.BOS);
        coalition = CoalitionFactory.getFriendlyCoalition(britain);
        assert(coalition == Coalition.COALITION_ENTENTE);

        coalition = CoalitionFactory.getFriendlyCoalition(germany);
        assert(coalition == Coalition.COALITION_CENTRAL);
    }
    
    @Test
    public void testEnemyCoalition() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Coalition coalition = CoalitionFactory.getEnemyCoalition(britain);
        assert(coalition == Coalition.COALITION_AXIS);

        coalition = CoalitionFactory.getEnemyCoalition(germany);
        assert(coalition == Coalition.COALITION_ALLIED);

        PWCGContext.setProduct(PWCGProduct.BOS);
        coalition = CoalitionFactory.getEnemyCoalition(britain);
        assert(coalition == Coalition.COALITION_CENTRAL);

        coalition = CoalitionFactory.getEnemyCoalition(germany);
        assert(coalition == Coalition.COALITION_ENTENTE);
    }
    
    @Test
    public void testAllCoalitions() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        List<Coalition> wwiiCoalitions = CoalitionFactory.getAllCoalitions();
        assert(wwiiCoalitions.contains(Coalition.COALITION_AXIS));
        assert(wwiiCoalitions.contains(Coalition.COALITION_ALLIED));
        assert(!wwiiCoalitions.contains(Coalition.COALITION_CENTRAL));
        assert(!wwiiCoalitions.contains(Coalition.COALITION_ENTENTE));

        PWCGContext.setProduct(PWCGProduct.BOS);
        List<Coalition> wwiCoalitions = CoalitionFactory.getAllCoalitions();
        assert(wwiCoalitions.contains(Coalition.COALITION_CENTRAL));
        assert(wwiCoalitions.contains(Coalition.COALITION_ENTENTE));
        assert(!wwiCoalitions.contains(Coalition.COALITION_AXIS));
        assert(!wwiCoalitions.contains(Coalition.COALITION_ALLIED));
    }
}
