package pwcg.product.fc.plane.payload;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.TankType;
import pwcg.campaign.plane.TankTypeFactory;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PlanePayloadElement;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.product.fc.plane.FCPlaneAttributeMapping;

@ExtendWith(MockitoExtension.class)
public class FCPayloadFactoryTest
{	
	public FCPayloadFactoryTest() throws PWCGException
	{
    	PWCGContext.setProduct(PWCGProduct.BOS);      
	}

	@Test
	public void validatePayloadsForAllPlanes() throws PWCGException 
	{
		FCPayloadFactory bosPayloadFactory = new FCPayloadFactory();
		TankTypeFactory planeTypeFactory = PWCGContext.getInstance().getTankTypeFactory();

		for (TankType bosTankType : planeTypeFactory.getAllPlanes())
		{
		    System.out.println(bosTankType.getType());
		    
			IPlanePayload payload = bosPayloadFactory.createPayload(bosTankType.getType(), DateUtils.getDateYYYYMMDD("19180501"));
			assert(payload != null);
			
			if (bosTankType.getType().equals(FCPlaneAttributeMapping.PFALZD3A.getTankType()))
			{
			    assert(bosTankType.getStockModifications().get(0) == PlanePayloadElement.COCKPIT_LIGHT);
			}
		}
	}
}
