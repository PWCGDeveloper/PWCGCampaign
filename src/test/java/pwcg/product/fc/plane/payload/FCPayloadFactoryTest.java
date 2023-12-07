package pwcg.product.fc.plane.payload;

import java.util.Arrays;
import java.util.List;

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
import pwcg.product.fc.plane.FCPlaneAttributeMapping;

@ExtendWith(MockitoExtension.class)
public class FCPayloadFactoryTest
{	
    private static final List<String> substitutedTypes = Arrays.asList(
            "fokkere3",
            "albatrosd3",
            "albatrosc2",
            "rolc2a",
            "farmanf40",
            "dorandar",
            "aircodh2",
            "soppup",
            "sopstrutter",
            "re8");
    
	public FCPayloadFactoryTest() throws PWCGException
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

	        if (!bosPlaneType.getSubstituteType().isEmpty()) 
		    {
		        Assertions.assertTrue(substitutedTypes.contains(bosPlaneType.getType()));
		        continue;
		    }
		        
            Assertions.assertFalse(substitutedTypes.contains(bosPlaneType.getType()));
		    
			IPlanePayload payload = bosPayloadFactory.createPlanePayload(bosPlaneType.getType(), DateUtils.getDateYYYYMMDD("19180501"));
			assert(payload != null);
			
			if (bosPlaneType.getType().equals(FCPlaneAttributeMapping.PFALZD3A.getPlaneType()))
			{
			    assert(bosPlaneType.getStockModifications().get(0) == PayloadElement.COCKPIT_LIGHT);
			}
            
            payload.selectNoOrdnancePayload();
            PayloadDesignation payloadDesignation = payload.getSelectedPayloadDesignation();
            Assertions.assertNotNull(payloadDesignation); 
		}
	}
}
