package pwcg.campaign.plane.payload;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetDefinition;
import pwcg.product.bos.plane.BosPlaneAttributeMapping;

@RunWith(MockitoJUnitRunner.class)
public class Ju87G2PayloadTest 
{
	@Mock IFlight flight;
	@Mock FlightInformation flightInformation;
	@Mock TargetDefinition targetDefinition;
	@Mock Campaign campaign;
	@Mock Squadron squadron;
	@Mock ConfigManagerCampaign configManagerCampaign;
	
	@Before
	public void setup() throws PWCGException
	{
		PWCGContext.setProduct(PWCGProduct.BOS);
	}

	@Test
	public void payloadNormalTest() throws PWCGException
	{
		PlaneType ju87D3 = PWCGContext.getInstance().getPlaneTypeFactory().createPlaneTypeByType(BosPlaneAttributeMapping.JU87_D3.getPlaneType());
		IPayloadFactory payloadFactory = PWCGContext.getInstance().getPayloadFactory();
    	IPlanePayload payloadGenerator = payloadFactory.createPlanePayload(ju87D3.getType(), DateUtils.getDateYYYYMMDD("19420801"));
    	testPatrolPayload(payloadGenerator);
    	testInterceptPayload(payloadGenerator);
        int expectedPayloadId = 2;
        testGroundAttackPayload(payloadGenerator, expectedPayloadId);
	}

	@Test
	public void payloadJu87GunsTest() throws PWCGException
	{
		PlaneType ju87G2 = PWCGContext.getInstance().getPlaneTypeFactory().createPlaneTypeByType(BosPlaneAttributeMapping.JU87_D3.getPlaneType());
		IPayloadFactory payloadFactory = PWCGContext.getInstance().getPayloadFactory();
    	IPlanePayload payloadGenerator = payloadFactory.createPlanePayload(ju87G2.getType(), DateUtils.getDateYYYYMMDD("19430801"));
    	int expectedPayloadId = 9;
    	testGroundAttackPayload(payloadGenerator, expectedPayloadId);
	}

	private void testPatrolPayload(IPlanePayload payloadGenerator) throws PWCGException
	{
		Mockito.when(flight.getFlightType()).thenReturn(FlightTypes.PATROL);
		runPayload(payloadGenerator);
	}
	
	private void testInterceptPayload(IPlanePayload payloadGenerator) throws PWCGException
	{
		Mockito.when(flight.getFlightType()).thenReturn(FlightTypes.INTERCEPT);
		runPayload(payloadGenerator);
	}
	
	private void testGroundAttackPayload(IPlanePayload payloadGenerator, int expectedPayloadId) throws PWCGException
	{
		Mockito.when(flight.getFlightType()).thenReturn(FlightTypes.GROUND_ATTACK);
		int payloadId =runPayload(payloadGenerator);
		assert(expectedPayloadId == payloadId);
	}

	private int runPayload(IPlanePayload payloadGenerator) throws PWCGException {
        int payloadId = payloadGenerator.createWeaponsPayload(flight);
        PayloadDesignation payloadDesignation = payloadGenerator.getSelectedPayloadDesignation();
        assert(payloadDesignation.getPayloadId() == payloadId);
        return payloadId;
	}
}
