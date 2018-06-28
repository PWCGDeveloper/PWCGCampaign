package pwcg.campaign.plane.payload;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayloadElementManager 
{
	private Map<String, PayloadElement> payloadElements = new HashMap<>();

	public PayloadElementManager()
	{
	   List<PayloadElement> payloadElementList = Arrays.asList(PayloadElement.values());
	   for (PayloadElement payloadElement : payloadElementList)
	   {
		   payloadElements.put(payloadElement.getDescription(), payloadElement);
	   }
	}
	
	public PayloadElement getPayloadElementByDescription(String description)
	{
		return payloadElements.get(description);
	}
}
