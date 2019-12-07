package pwcg.campaign.plane.payload;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.target.TargetCategory;

@RunWith(MockitoJUnitRunner.class)
public class WW2PayloadTest 
{
	@Mock
	Flight flight;
	
	@Mock
	Campaign campaign;
	
	@Mock
	Squadron squadron;
	
	@Mock
	ConfigManagerCampaign configManagerCampaign;
	
	@Before
	public void setup() throws PWCGException
	{
		PWCGContext.setProduct(PWCGProduct.BOS);
        
        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManagerCampaign);
		Mockito.when(flight.getSquadron()).thenReturn(squadron);
	}

	@Test
	public void payloadTest() throws PWCGException
	{
		IPayloadFactory payloadFactory = PWCGContext.getInstance().getPayloadFactory();
		
        for (PlaneType planeType : PWCGContext.getInstance().getPlaneTypeFactory().getAllPlanes())
        {
        	IPlanePayload payloadGenerator = payloadFactory.createPlanePayload(planeType.getType());
        	testPatrolPayload(payloadGenerator);
        	testInterceptPayload(payloadGenerator);
        	testBombPayloads(payloadGenerator);
        }
	}
	
	private void testPatrolPayload(IPlanePayload payloadGenerator) throws PWCGException
	{
		Mockito.when(flight.getFlightType()).thenReturn(FlightTypes.PATROL);
		Mockito.when(flight.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_NONE);
		runPayload(payloadGenerator);
	}
	
	private void testInterceptPayload(IPlanePayload payloadGenerator) throws PWCGException
	{
		Mockito.when(flight.getFlightType()).thenReturn(FlightTypes.INTERCEPT);
		Mockito.when(flight.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_NONE);
		runPayload(payloadGenerator);
	}

	private void testBombPayloads(IPlanePayload payloadGenerator) throws PWCGException
	{
		testBombPayload(payloadGenerator, FlightTypes.GROUND_ATTACK);
		testBombPayload(payloadGenerator, FlightTypes.DIVE_BOMB);
        testBombPayload(payloadGenerator, FlightTypes.BOMB);
        testBombPayload(payloadGenerator, FlightTypes.LOW_ALT_BOMB);
	}
	
	private void testBombPayload(IPlanePayload payloadGenerator, FlightTypes flightType) throws PWCGException
	{
		Mockito.when(flight.getFlightType()).thenReturn(flightType);
		Mockito.when(flight.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_SOFT);
		runPayload(payloadGenerator);
		Mockito.when(flight.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_ARMORED);
		runPayload(payloadGenerator);
		Mockito.when(flight.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_MEDIUM);
		runPayload(payloadGenerator);
		Mockito.when(flight.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_HEAVY);
		runPayload(payloadGenerator);
	}

	private void runPayload(IPlanePayload payloadGenerator) throws PWCGException {
		for (int i = 0; i < 100; ++i)
		{
			int payloadId = payloadGenerator.createWeaponsPayload(flight);
			PayloadDesignation payloadDesignation = payloadGenerator.getSelectedPayloadDesignation();
			assert(payloadDesignation.getPayloadId() == payloadId);
		}
	}
}
