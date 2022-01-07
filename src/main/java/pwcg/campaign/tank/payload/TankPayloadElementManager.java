package pwcg.campaign.tank.payload;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TankPayloadElementManager 
{
	private Map<String, TankPayloadElement> payloadElements = new HashMap<>();

	public TankPayloadElementManager()
	{
	   List<TankPayloadElement> payloadElementList = Arrays.asList(TankPayloadElement.values());
	   for (TankPayloadElement payloadElement : payloadElementList)
	   {
		   payloadElements.put(payloadElement.getDescription(), payloadElement);
	   }
	}
	
	public TankPayloadElement getPayloadElementByDescription(String description)
	{
		return payloadElements.get(description);
	}
}
