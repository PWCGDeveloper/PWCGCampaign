package pwcg.product.fc.plane.payload;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.PlaneTypeFactory;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.product.fc.plane.FCPlaneAttributeMapping;

@RunWith(MockitoJUnitRunner.class)
public class FCPayloadFactoryTest
{
	
	@Before 
	public void setup() throws PWCGException
	{
    	PWCGContext.setProduct(PWCGProduct.FC);      
	}

	@Test
	public void validatePayloadsForAllPlanes() throws PWCGException 
	{
		FCPayloadFactory bosPayloadFactory = new FCPayloadFactory();
		PlaneTypeFactory planeTypeFactory = PWCGContext.getInstance().getPlaneTypeFactory();

		for (PlaneType bosPlaneType : planeTypeFactory.getAllPlanes())
		{
		    System.out.println(bosPlaneType.getType());
		    
			IPlanePayload payload = bosPayloadFactory.createPlanePayload(bosPlaneType.getType(), DateUtils.getDateYYYYMMDD("19180501"));
			assert(payload != null);
			
			if (bosPlaneType.getType().equals(FCPlaneAttributeMapping.PFALZD3A.getPlaneType()))
			{
			    assert(bosPlaneType.getStockModifications().get(0) == PayloadElement.COCKPIT_LIGHT);
			}
		}
	}
}
