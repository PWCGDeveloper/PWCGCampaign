package pwcg.mission.flight.plane;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.plane.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FlightSizeCalculatorTest 
{
    @Mock private FlightInformation flightInformation;
    @Mock private Company squadron;
    private Campaign campaign;
    private List<Country> countries = Arrays.asList(Country.GERMANY, Country.RUSSIA);

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.KG53_PROFILE);
    }

	@Test
	public void calcPlanesInFlightTest() throws PWCGException
	{
	    Mockito.when(squadron.determineSquadronPrimaryRoleCategory(Mockito.any())).thenReturn(PwcgRoleCategory.ATTACK);

	    for (Country country : countries)
	    {

	        int planesInFlight = calcPlanesInFlight(FlightTypes.GROUND_ATTACK, country);
	        assert(planesInFlight >= 2 && planesInFlight <= 4);
	    }
	}

    @Test
    public void calcPlanesInFlightBombTest() throws PWCGException
    {
        Mockito.when(squadron.determineSquadronPrimaryRoleCategory(Mockito.any())).thenReturn(PwcgRoleCategory.BOMBER);

        for (Country country : countries)
        {
            int planesInFlight = calcPlanesInFlight(FlightTypes.BOMB, country);
            assert(planesInFlight >= 2 && planesInFlight <= 4);
    
            planesInFlight = calcPlanesInFlight(FlightTypes.DIVE_BOMB, country);
            assert(planesInFlight >= 2 && planesInFlight <= 4);
    
            planesInFlight = calcPlanesInFlight(FlightTypes.LOW_ALT_BOMB, country);
            assert(planesInFlight >= 2 && planesInFlight <= 4);
    
            planesInFlight = calcPlanesInFlight(FlightTypes.STRATEGIC_BOMB, country);
            assert(planesInFlight >= 4 && planesInFlight <= 8);
        }
    }

    @Test
    public void calcPlanesInFlightFighterTest() throws PWCGException
    {
        Mockito.when(squadron.determineSquadronPrimaryRoleCategory(Mockito.any())).thenReturn(PwcgRoleCategory.FIGHTER);

        for (Country country : countries)
        {
            int maxPlanes = 4;
            if (country == Country.RUSSIA)
            {
                maxPlanes = 3;
            }
            
            int planesInFlight = calcPlanesInFlight(FlightTypes.BALLOON_BUST, country);
            assert(planesInFlight >= 2 && planesInFlight <= maxPlanes);
    
            planesInFlight = calcPlanesInFlight(FlightTypes.BALLOON_DEFENSE, country);
            assert(planesInFlight >= 2 && planesInFlight <= maxPlanes);
    
            planesInFlight = calcPlanesInFlight(FlightTypes.ESCORT, country);
            assert(planesInFlight >= 2 && planesInFlight <= maxPlanes);
    
            planesInFlight = calcPlanesInFlight(FlightTypes.INTERCEPT, country);
            assert(planesInFlight >= 2 && planesInFlight <= maxPlanes);
    
            planesInFlight = calcPlanesInFlight(FlightTypes.LOW_ALT_CAP, country);
            assert(planesInFlight >= 2 && planesInFlight <= maxPlanes);
    
            planesInFlight = calcPlanesInFlight(FlightTypes.LOW_ALT_PATROL, country);
            assert(planesInFlight >= 2 && planesInFlight <= maxPlanes);
    
            planesInFlight = calcPlanesInFlight(FlightTypes.SCRAMBLE, country);
            assert(planesInFlight >= 2 && planesInFlight <= maxPlanes);
    
            planesInFlight = calcPlanesInFlight(FlightTypes.SCRAMBLE_OPPOSE, country);
            assert(planesInFlight >= 2 && planesInFlight <= maxPlanes);
    
            planesInFlight = calcPlanesInFlight(FlightTypes.PATROL, country);
            assert(planesInFlight >= 2 && planesInFlight <= maxPlanes);
    
            planesInFlight = calcPlanesInFlight(FlightTypes.OFFENSIVE, country);
            assert(planesInFlight >= 2 && planesInFlight <= maxPlanes);
        }
    }

    @Test
    public void calcPlanesInTransportTest() throws PWCGException
    {
        for (Country country : countries)
        {
            int planesInFlight = calcPlanesInFlight(FlightTypes.CARGO_DROP, country);
            assert(planesInFlight >= 2 && planesInFlight <= 4);
    
            planesInFlight = calcPlanesInFlight(FlightTypes.PARATROOP_DROP, country);
            assert(planesInFlight >= 2 && planesInFlight <= 4);
        }
    }

	private int calcPlanesInFlight(FlightTypes flightType, Country flightCountry) throws PWCGException
	{
        ICountry country = CountryFactory.makeCountryByCountry(flightCountry);
        Mockito.when(squadron.getCountry()).thenReturn(country);

        Mockito.when(flightInformation.getCampaign()).thenReturn(campaign);
        Mockito.when(flightInformation.getFlightType()).thenReturn(flightType);
        Mockito.when(flightInformation.getCompany()).thenReturn(squadron);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        
		FlightSizeCalculator flightSizeCalculator = new FlightSizeCalculator(flightInformation);
		int planesInFlight = flightSizeCalculator.calcPlanesInFlight();
        return planesInFlight;
	}
}

