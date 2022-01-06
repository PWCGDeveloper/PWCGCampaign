package pwcg.campaign.plane.payload;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanePayloadElementManager 
{
	private Map<String, PlanePayloadElement> payloadElements = new HashMap<>();

	public PlanePayloadElementManager()
	{
	   List<PlanePayloadElement> payloadElementList = Arrays.asList(PlanePayloadElement.values());
	   for (PlanePayloadElement payloadElement : payloadElementList)
	   {
		   payloadElements.put(payloadElement.getDescription(), payloadElement);
	   }
	}
	
	public PlanePayloadElement getPayloadElementByDescription(String description)
	{
		return payloadElements.get(description);
	}
}
