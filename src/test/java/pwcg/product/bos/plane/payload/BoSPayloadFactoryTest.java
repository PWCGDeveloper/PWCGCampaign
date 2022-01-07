package pwcg.product.bos.plane.payload;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.PlaneAttributeMapping;
import pwcg.campaign.plane.TankType;
import pwcg.campaign.plane.TankTypeFactory;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PlanePayloadElement;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

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
        PlanePayloadFactory bosPayloadFactory = new PlanePayloadFactory();
        TankTypeFactory planeTypeFactory = PWCGContext.getInstance().getTankTypeFactory();

        for (TankType bosTankType : planeTypeFactory.getAllPlanes())
        {
            System.out.println(bosTankType.getType());
            
            IPlanePayload payload = bosPayloadFactory.createPayload(bosTankType.getType(), DateUtils.getDateYYYYMMDD("19420801"));
            assert(payload != null);
            
            if (bosTankType.getType().equals(PlaneAttributeMapping.HURRICANE_MKII.getTankType()))
            {
                assert(payload.getSelectedModifications().get(0) == PlanePayloadElement.MIRROR);
            }
        }
    }
}
