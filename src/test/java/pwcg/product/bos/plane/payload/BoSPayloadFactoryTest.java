package pwcg.product.bos.plane.payload;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.PlaneTypeFactory;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.core.exception.PWCGException;
import pwcg.product.bos.plane.payload.BoSPayloadFactory;

@RunWith(MockitoJUnitRunner.class)
public class BoSPayloadFactoryTest
{
	
	@Before 
	public void setup() throws PWCGException
	{
    	PWCGContext.setProduct(PWCGProduct.BOS);      
	}

	@Test
	public void validatePayloadsForAllPlanes() throws PWCGException 
	{
		BoSPayloadFactory bosPayloadFactory = new BoSPayloadFactory();
		PlaneTypeFactory planeTypeFactory = PWCGContext.getInstance().getPlaneTypeFactory();

		for (PlaneType bosPlaneType : planeTypeFactory.getAllPlanes())
		{
			IPlanePayload payload = bosPayloadFactory.createPlanePayload(bosPlaneType.getType());
			assert(payload != null);
		}
	}
}
