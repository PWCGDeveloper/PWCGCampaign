package pwcg.product.bos.plane;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.TankType;
import pwcg.campaign.plane.TankTypeFactory;
import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
public class BoSPlaneFactoryTest
{	
	public BoSPlaneFactoryTest() throws PWCGException
	{
    	PWCGContext.setProduct(PWCGProduct.BOS);      
	}

	@Test
	public void testTankTypeCreation() throws PWCGException
	{
		TankTypeFactory planeTypeFactory = PWCGContext.getInstance().getTankTypeFactory();
		
		for (TankType planeType : planeTypeFactory.getAllPlanes())
		{
			TankType plane = planeTypeFactory.getPlaneById(planeType.getType());
			Assertions.assertTrue (plane.getType().equals(planeType.getType()));
			Assertions.assertTrue (plane.getArchType() != null);
		}
	}
}
