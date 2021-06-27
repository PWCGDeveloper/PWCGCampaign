package pwcg.campaign.io.json;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.shipping.ShipEncounterZones;
import pwcg.core.exception.PWCGException;

public class ShipEncounterZonesIOJson 
{
	public static ShipEncounterZones readJson(String mapName) throws PWCGException, PWCGException
	{
		JsonObjectReader<ShipEncounterZones> jsonReader = new JsonObjectReader<>(ShipEncounterZones.class);
		String filePath = PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\";
		ShipEncounterZones shipEncounterZones = jsonReader.readJsonFile(filePath, "ShipEncounter.json");
		return shipEncounterZones;
	}
}
