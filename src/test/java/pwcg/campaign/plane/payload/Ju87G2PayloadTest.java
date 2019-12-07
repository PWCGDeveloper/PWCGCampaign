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
import pwcg.product.bos.plane.BosPlaneAttributeMapping;

@RunWith(MockitoJUnitRunner.class)
public class Ju87G2PayloadTest 
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
	public void payloadNormalTest() throws PWCGException
	{
		PlaneType ju87D3 = PWCGContext.getInstance().getPlaneTypeFactory().createPlaneTypeByType(BosPlaneAttributeMapping.JU87_D3.getPlaneType());
		IPayloadFactory payloadFactory = PWCGContext.getInstance().getPayloadFactory();
    	IPlanePayload payloadGenerator = payloadFactory.createPlanePayload(ju87D3.getType());
    	testPatrolPayload(payloadGenerator);
    	testInterceptPayload(payloadGenerator);
    	testGroundAttackPayload(payloadGenerator);
	}

	@Test
	public void payloadJu87G2F2Test() throws PWCGException
	{
		PlaneType ju87G2 = PWCGContext.getInstance().getPlaneTypeFactory().createPlaneTypeByType(BosPlaneAttributeMapping.JU87_D3.getPlaneType());
		IPayloadFactory payloadFactory = PWCGContext.getInstance().getPayloadFactory();
    	IPlanePayload payloadGenerator = payloadFactory.createPlanePayload(ju87G2.getType());
    	testGroundAttackPayload(payloadGenerator);
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
	
	private void testGroundAttackPayload(IPlanePayload payloadGenerator) throws PWCGException
	{
		Mockito.when(flight.getFlightType()).thenReturn(FlightTypes.GROUND_ATTACK);
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
