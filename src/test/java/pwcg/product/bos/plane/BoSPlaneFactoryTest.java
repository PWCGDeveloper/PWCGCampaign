package pwcg.product.bos.plane;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.PlaneTypeFactory;
import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
public class BoSPlaneFactoryTest
{	
	public BoSPlaneFactoryTest() throws PWCGException
	{
    	PWCGContext.setProduct(PWCGProduct.BOS);      
	}

	@Test
	public void testPlaneTypeCreation() throws PWCGException
	{
		PlaneTypeFactory planeTypeFactory = PWCGContext.getInstance().getPlaneTypeFactory();
		
		for (PlaneType planeType : planeTypeFactory.getAllPlanes())
		{
			PlaneType plane = planeTypeFactory.getPlaneById(planeType.getType());
			Assertions.assertTrue (plane.getType().equals(planeType.getType()));
			Assertions.assertTrue (plane.getArchType() != null);
		}
	}
}
