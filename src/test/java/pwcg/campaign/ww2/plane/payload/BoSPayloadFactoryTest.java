package pwcg.campaign.ww2.plane.payload;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.PlaneTypeFactory;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class BoSPayloadFactoryTest
{
	
	@Before 
	public void setup() throws PWCGException
	{
    	PWCGContextManager.setRoF(false);      
	}

	@Test
	public void validatePayloadsForAllPlanes() throws PWCGException 
	{
		BoSPayloadFactory bosPayloadFactory = new BoSPayloadFactory();
		PlaneTypeFactory planeTypeFactory = PWCGContextManager.getInstance().getPlaneTypeFactory();

		for (PlaneType bosPlaneType : planeTypeFactory.getAllPlanes())
		{
			IPlanePayload payload = bosPayloadFactory.createPlanePayload(bosPlaneType.getType());
			assert(payload != null);
		}
	}
}
