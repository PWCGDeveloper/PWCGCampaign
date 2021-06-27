package pwcg.campaign.io.json;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.shipping.CargoRoutes;
import pwcg.core.exception.PWCGException;

public class CargoRoutesIOJson 
{
	public static CargoRoutes readJson(String mapName) throws PWCGException, PWCGException
	{
		JsonObjectReader<CargoRoutes> jsonReader = new JsonObjectReader<>(CargoRoutes.class);
		String filePath = PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\";
		CargoRoutes cargoRoutes = jsonReader.readJsonFile(filePath, "CargoRoutes.json");
		return cargoRoutes;
	}
}
