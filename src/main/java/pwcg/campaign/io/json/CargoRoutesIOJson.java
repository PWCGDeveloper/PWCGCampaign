package pwcg.campaign.io.json;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.shipping.CargoShipRoutes;
import pwcg.core.exception.PWCGException;

public class CargoRoutesIOJson 
{
	public static CargoShipRoutes readJson(String mapName) throws PWCGException, PWCGException
	{
		JsonObjectReader<CargoShipRoutes> jsonReader = new JsonObjectReader<>(CargoShipRoutes.class);
		String filePath = PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\";
		CargoShipRoutes cargoRoutes = jsonReader.readJsonFile(filePath, "CargoRoutes.json");
		return cargoRoutes;
	}

    public static void writeJson(String directory, String filename, CargoShipRoutes cargoShipRoutes) throws PWCGException
    {
        PwcgJsonWriter<CargoShipRoutes> jsonWriter = new PwcgJsonWriter<>();
        jsonWriter.writeAsJson(cargoShipRoutes, directory, filename);
    }
}
