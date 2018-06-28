package pwcg.campaign.ww2.plane;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.PlaneTypeFactory;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class BoSPlaneFactoryTest
{
	
	@Before 
	public void setup() throws PWCGException
	{
    	PWCGContextManager.setRoF(false);      
	}

	@Test
	public void testPlaneTypeCreation() throws PWCGException
	{
		PlaneTypeFactory planeTypeFactory = PWCGContextManager.getInstance().getPlaneTypeFactory();
		
		for (PlaneType planeType : planeTypeFactory.getAllPlanes())
		{
			PlaneType plane = planeTypeFactory.getPlaneById(planeType.getType());
			assert plane.getType().equals(planeType.getType());
		}
	}
}
