package pwcg.mission.flight.plane;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class FlightSizeCalculatorTest 
{
    @Mock private FlightInformation flightInformation;
    @Mock private Squadron squadron;
    @Mock private Campaign campaign;
    private List<Country> countries = Arrays.asList(Country.GERMANY, Country.RUSSIA);

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.KG53_PROFILE);
    }

	@Test
	public void calcPlanesInFlightTest() throws PWCGException
	{
	    Mockito.when(squadron.determineSquadronPrimaryRole(Mockito.any())).thenReturn(Role.ROLE_ATTACK);

	    for (Country country : countries)
	    {

	        int planesInFlight = calcPlanesInFlight(FlightTypes.GROUND_ATTACK, country);
	        assert(planesInFlight >= 2 && planesInFlight <= 4);
	    }
	}

    @Test
    public void calcPlanesInFlightBombTest() throws PWCGException
    {
        Mockito.when(squadron.determineSquadronPrimaryRole(Mockito.any())).thenReturn(Role.ROLE_BOMB);

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
        Mockito.when(squadron.determineSquadronPrimaryRole(Mockito.any())).thenReturn(Role.ROLE_FIGHTER);

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
        Mockito.when(flightInformation.getSquadron()).thenReturn(squadron);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        
		FlightSizeCalculator flightSizeCalculator = new FlightSizeCalculator(flightInformation);
		int planesInFlight = flightSizeCalculator.calcPlanesInFlight();
        return planesInFlight;
	}
}

