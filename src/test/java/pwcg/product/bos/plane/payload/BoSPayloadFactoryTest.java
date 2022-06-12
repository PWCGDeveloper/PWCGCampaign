package pwcg.product.bos.plane.payload;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.PlaneTypeFactory;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadDesignation;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.product.bos.plane.BosPlaneAttributeMapping;

@ExtendWith(MockitoExtension.class)
public class BoSPayloadFactoryTest
{	
	public BoSPayloadFactoryTest() throws PWCGException
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
            System.out.println(bosPlaneType.getType());
            
            IPlanePayload payload = bosPayloadFactory.createPlanePayload(bosPlaneType.getType(), DateUtils.getDateYYYYMMDD("19420801"));
            assert(payload != null);
            
            if (bosPlaneType.getType().equals(BosPlaneAttributeMapping.HURRICANE_MKII.getPlaneType()))
            {
                Assertions.assertEquals(PayloadElement.MIRROR, payload.getSelectedModifications().get(0));
            }
            
            payload.selectNoOrdnancePayload();
            PayloadDesignation payloadDesignation = payload.getSelectedPayloadDesignation();
            Assertions.assertNotNull(payloadDesignation); 
        }
    }
}
