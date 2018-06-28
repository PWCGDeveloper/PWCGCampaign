package pwcg.campaign.personnel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class EnemySquadronFinderTest
{
    @Mock
    private Squadron squadron;
    
    @Mock
    private Campaign campaign;
    
    @Mock
    private CampaignPersonnelManager personnelManager;

    @Mock
    private SquadronPersonnel squadronPersonnel;

    @Mock
    IAirfield squadronAirfield;
    
    @Before
    public void setupForTestEnvironment() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        
        Coordinate squadronAirfieldPosition = new Coordinate(100000.0, 0.0, 100000.0);
        ICountry squadronCountry = CountryFactory.makeCountryByCountry(Country.GERMANY);

        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
        Mockito.when(personnelManager.getSquadronPersonnel(Mockito.any())).thenReturn(squadronPersonnel);
        Mockito.when(squadronPersonnel.isSquadronViable()).thenReturn(true);

        Mockito.when(squadron.determineCurrentAirfieldAnyMap(Mockito.any())).thenReturn(squadronAirfield);
        Mockito.when(squadron.getCountry()).thenReturn(squadronCountry);
        Mockito.when(squadronAirfield.getPosition()).thenReturn(squadronAirfieldPosition);
    }
    

    
    @Test
    public void findEnemySquadronFromCorner () throws PWCGException
    {     
        Coordinate squadronAirfieldPosition = new Coordinate(0.0, 0.0, 0.0);
        Mockito.when(squadronAirfield.getPosition()).thenReturn(squadronAirfieldPosition);

        EnemySquadronFinder enemySquadronFinder = new EnemySquadronFinder(campaign);
        Squadron enemySquadron = enemySquadronFinder.getRandomEnemySquadron(squadron, DateUtils.getDateYYYYMMDD("19421001"));
        assert(enemySquadron != null);
    }

    @Test
    public void findEnemySquadronNearby () throws PWCGException
    {     
        EnemySquadronFinder enemySquadronFinder = new EnemySquadronFinder(campaign);
        Squadron enemySquadron = enemySquadronFinder.getRandomEnemySquadron(squadron, DateUtils.getDateYYYYMMDD("19421001"));
        assert(enemySquadron != null);
    }

    @Test
    public void findEnemySquadronAny () throws PWCGException
    {     
        EnemySquadronFinder enemySquadronFinder = new EnemySquadronFinder(campaign);
        Squadron enemySquadron = enemySquadronFinder.getRandomEnemySquadron(squadron, DateUtils.getDateYYYYMMDD("19421001"));
        assert(enemySquadron != null);
    }
}
