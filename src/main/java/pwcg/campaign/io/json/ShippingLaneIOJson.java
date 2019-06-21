package pwcg.campaign.io.json;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.target.locator.ShippingLanes;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;

public class ShippingLaneIOJson 
{
	public static ShippingLanes readJson(String mapName) throws PWCGException, PWCGIOException
	{
		JsonObjectReader<ShippingLanes> jsonReader = new JsonObjectReader<>(ShippingLanes.class);
		ShippingLanes shippingLanes = jsonReader.readJsonFile(PWCGContextManager.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\", "SeaLanes.json");
		return shippingLanes;
	}
}
