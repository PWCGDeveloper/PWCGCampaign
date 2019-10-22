package pwcg.mission.flight.plane;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class FlightSizeCalculatorTest 
{
    Campaign campaign;

    @Before
    public void fighterFlightTests() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.KG53_PROFILE);
    }

	@Test
	public void calcPlanesInFlightGroundAttackTest() throws PWCGException
	{
		calcPlanesInFlightGroundAttack(FlightTypes.GROUND_ATTACK);
		calcPlanesInFlightGroundAttack(FlightTypes.ANTI_SHIPPING_ATTACK);

	}

	public void calcPlanesInFlightGroundAttack(FlightTypes flightType) throws PWCGException
	{
		FlightSizeCalculator flightSizeCalculator = new FlightSizeCalculator(campaign, flightType);
		int planesInFlight = flightSizeCalculator.calcPlanesInFlight();
		assert(planesInFlight >= 2 && planesInFlight <= 4);
	}

	@Test
	public void calcPlanesInFlightBombTest() throws PWCGException
	{
		calcPlanesInFlightGroundAttack(FlightTypes.BOMB);
		calcPlanesInFlightGroundAttack(FlightTypes.DIVE_BOMB);
		calcPlanesInFlightGroundAttack(FlightTypes.LOW_ALT_BOMB);
		calcPlanesInFlightGroundAttack(FlightTypes.STRATEGIC_BOMB);
	}

	public void calcPlanesInFlightBombAttack(FlightTypes flightType) throws PWCGException
	{
		FlightSizeCalculator flightSizeCalculator = new FlightSizeCalculator(campaign, flightType);
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

	public void calcPlanesInFlightFighterAttack(FlightTypes flightType) throws PWCGException
	{
		FlightSizeCalculator flightSizeCalculator = new FlightSizeCalculator(campaign, flightType);
		int planesInFlight = flightSizeCalculator.calcPlanesInFlight();
		assert(planesInFlight >= 2 && planesInFlight <= 4);
	}

	@Test
	public void calcPlanesInTransportFighterTest() throws PWCGException
	{
		calcPlanesInFlightGroundAttack(FlightTypes.CARGO_DROP);
		calcPlanesInFlightGroundAttack(FlightTypes.PARATROOP_DROP);

	}

	public void calcPlanesInFlightTransportAttack(FlightTypes flightType) throws PWCGException
	{
		FlightSizeCalculator flightSizeCalculator = new FlightSizeCalculator(campaign, flightType);
		int planesInFlight = flightSizeCalculator.calcPlanesInFlight();
		assert(planesInFlight >= 1 && planesInFlight <= 3);
	}
}

