package pwcg.mission.mcu;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.CountryFactory;

public class CoalitionFactoryTest
{
    ICountry britain;
    ICountry germany;
    
    @Before
    public void setup()
    {
        britain = CountryFactory.makeCountryByCountry(Country.BRITAIN);
        germany = CountryFactory.makeCountryByCountry(Country.GERMANY);
    }
    
    @Test
    public void testCoalitionBySide()
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Coalition coalition = CoalitionFactory.getCoalitionBySide(Side.ALLIED);
        assert(coalition == Coalition.COALITION_ALLIED);

        coalition = CoalitionFactory.getCoalitionBySide(Side.AXIS);
        assert(coalition == Coalition.COALITION_AXIS);

        PWCGContext.setProduct(PWCGProduct.FC);
        coalition = CoalitionFactory.getCoalitionBySide(Side.ALLIED);
        assert(coalition == Coalition.COALITION_ENTENTE);

        coalition = CoalitionFactory.getCoalitionBySide(Side.AXIS);
        assert(coalition == Coalition.COALITION_CENTRAL);
    }
    
    @Test
    public void testFriendlyCoalition()
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Coalition coalition = CoalitionFactory.getFriendlyCoalition(britain);
        assert(coalition == Coalition.COALITION_ALLIED);

        coalition = CoalitionFactory.getFriendlyCoalition(germany);
        assert(coalition == Coalition.COALITION_AXIS);

        PWCGContext.setProduct(PWCGProduct.FC);
        coalition = CoalitionFactory.getFriendlyCoalition(britain);
        assert(coalition == Coalition.COALITION_ENTENTE);

        coalition = CoalitionFactory.getFriendlyCoalition(germany);
        assert(coalition == Coalition.COALITION_CENTRAL);
    }
    
    @Test
    public void testEnemyCoalition()
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Coalition coalition = CoalitionFactory.getEnemyCoalition(britain);
        assert(coalition == Coalition.COALITION_AXIS);

        coalition = CoalitionFactory.getEnemyCoalition(germany);
        assert(coalition == Coalition.COALITION_ALLIED);

        PWCGContext.setProduct(PWCGProduct.FC);
        coalition = CoalitionFactory.getEnemyCoalition(britain);
        assert(coalition == Coalition.COALITION_CENTRAL);

        coalition = CoalitionFactory.getEnemyCoalition(germany);
        assert(coalition == Coalition.COALITION_ENTENTE);
    }
    
    @Test
    public void testAllCoalitions()
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        List<Coalition> wwiiCoalitions = CoalitionFactory.getAllCoalitions();
        assert(wwiiCoalitions.contains(Coalition.COALITION_AXIS));
        assert(wwiiCoalitions.contains(Coalition.COALITION_ALLIED));
        assert(!wwiiCoalitions.contains(Coalition.COALITION_CENTRAL));
        assert(!wwiiCoalitions.contains(Coalition.COALITION_ENTENTE));

        PWCGContext.setProduct(PWCGProduct.FC);
        List<Coalition> wwiCoalitions = CoalitionFactory.getAllCoalitions();
        assert(wwiCoalitions.contains(Coalition.COALITION_CENTRAL));
        assert(wwiCoalitions.contains(Coalition.COALITION_ENTENTE));
        assert(!wwiCoalitions.contains(Coalition.COALITION_AXIS));
        assert(!wwiCoalitions.contains(Coalition.COALITION_ALLIED));
    }
}
