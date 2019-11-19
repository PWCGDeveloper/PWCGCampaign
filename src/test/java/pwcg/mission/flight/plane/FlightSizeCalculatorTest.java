package pwcg.mission.flight.plane;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.CountryFactory;
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
    private Campaign campaign;
    private ICountry country;

    @Before
    public void fighterFlightTests() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.KG53_PROFILE);
        country = CountryFactory.makeCountryByCountry(Country.GERMANY);
    }

	@Test
	public void calcPlanesInFlightGroundAttackTest() throws PWCGException
	{
		calcPlanesInFlightGroundAttack(FlightTypes.GROUND_ATTACK);
		calcPlanesInFlightGroundAttack(FlightTypes.ANTI_SHIPPING_ATTACK);
	}

    @Test
    public void calcPlanesInFlightBombTest() throws PWCGException
    {
        calcPlanesInFlightGroundAttack(FlightTypes.BOMB);
        calcPlanesInFlightGroundAttack(FlightTypes.DIVE_BOMB);
        calcPlanesInFlightGroundAttack(FlightTypes.LOW_ALT_BOMB);
        calcPlanesInFlightGroundAttack(FlightTypes.STRATEGIC_BOMB);
    }

	public void calcPlanesInFlightGroundAttack(FlightTypes flightType) throws PWCGException
	{
        country = CountryFactory.makeCountryByCountry(Country.RUSSIA);
        Mockito.when(squadron.getCountry()).thenReturn(country);

        Mockito.when(flightInformation.getCampaign()).thenReturn(campaign);
        Mockito.when(flightInformation.getFlightType()).thenReturn(flightType);
        Mockito.when(flightInformation.getSquadron()).thenReturn(squadron);
        
		FlightSizeCalculator flightSizeCalculator = new FlightSizeCalculator(flightInformation);
		int planesInFlight = flightSizeCalculator.calcPlanesInFlight();
		assert(planesInFlight >= 2 && planesInFlight <= 4);
	}

	@Test
	public void calcPlanesInFlightFighterTest() throws PWCGException
	{
		calcPlanesInFlightGroundAttack(FlightTypes.BALLOON_BUST);
		calcPlanesInFlightGroundAttack(FlightTypes.BALLOON_DEFENSE);
		calcPlanesInFlightGroundAttack(FlightTypes.ESCORT);
		calcPlanesInFlightGroundAttack(FlightTypes.INTERCEPT);
		calcPlanesInFlightGroundAttack(FlightTypes.LOW_ALT_CAP);
		calcPlanesInFlightGroundAttack(FlightTypes.LOW_ALT_PATROL);
		calcPlanesInFlightGroundAttack(FlightTypes.PATROL);
		calcPlanesInFlightGroundAttack(FlightTypes.SCRAMBLE);
		calcPlanesInFlightGroundAttack(FlightTypes.SCRAMBLE_OPPOSE);
	}

    public void calcPlanesInFlightBombAttack(FlightTypes flightType) throws PWCGException
    {
        country = CountryFactory.makeCountryByCountry(Country.GERMANY);
        Mockito.when(squadron.getCountry()).thenReturn(country);

        Mockito.when(flightInformation.getCampaign()).thenReturn(campaign);
        Mockito.when(flightInformation.getFlightType()).thenReturn(flightType);
        Mockito.when(flightInformation.getSquadron()).thenReturn(squadron);
        
        FlightSizeCalculator flightSizeCalculator = new FlightSizeCalculator(flightInformation);
        int planesInFlight = flightSizeCalculator.calcPlanesInFlight();
        assert(planesInFlight >= 2 && planesInFlight <= 4);
    }

	@Test
	public void calcPlanesInTransportTest() throws PWCGException
	{
	    calcPlanesInFlightTransport(FlightTypes.CARGO_DROP);
	    calcPlanesInFlightTransport(FlightTypes.PARATROOP_DROP);

	}

	public void calcPlanesInFlightTransport(FlightTypes flightType) throws PWCGException
	{
        country = CountryFactory.makeCountryByCountry(Country.GERMANY);
        Mockito.when(squadron.getCountry()).thenReturn(country);

        Mockito.when(flightInformation.getCampaign()).thenReturn(campaign);
        Mockito.when(flightInformation.getFlightType()).thenReturn(flightType);
        Mockito.when(flightInformation.getSquadron()).thenReturn(squadron);
        
		FlightSizeCalculator flightSizeCalculator = new FlightSizeCalculator(flightInformation);
		int planesInFlight = flightSizeCalculator.calcPlanesInFlight();
		assert(planesInFlight >= 1 && planesInFlight <= 3);
	}

    @Test
    public void calcPlanesInFighterTest() throws PWCGException
    {
        calcPlanesInFlightFighter(FlightTypes.PATROL);
        calcPlanesInFlightFighter(FlightTypes.OFFENSIVE);

    }

    public void calcPlanesInFlightFighter(FlightTypes flightType) throws PWCGException
    {
        country = CountryFactory.makeCountryByCountry(Country.USA);
        Mockito.when(squadron.getCountry()).thenReturn(country);

        Mockito.when(flightInformation.getCampaign()).thenReturn(campaign);
        Mockito.when(flightInformation.getFlightType()).thenReturn(flightType);
        Mockito.when(flightInformation.getSquadron()).thenReturn(squadron);
        
        FlightSizeCalculator flightSizeCalculator = new FlightSizeCalculator(flightInformation);
        int planesInFlight = flightSizeCalculator.calcPlanesInFlight();
        assert(planesInFlight >= 2 && planesInFlight <= 4);
    }

}

