package pwcg.campaign.io.json;

import pwcg.campaign.context.PWCGDirectoryManager;
import pwcg.campaign.group.airfield.AirfieldHotSpotCollection;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;

public class AirfieldHotSpotsIOJson 
{
	public static AirfieldHotSpotCollection readJson() throws PWCGException, PWCGIOException
	{
		JsonObjectReader<AirfieldHotSpotCollection> jsonReader = new JsonObjectReader<>(AirfieldHotSpotCollection.class);
		AirfieldHotSpotCollection airfieldHotSpotCollection = jsonReader.readJsonFile(PWCGDirectoryManager.getInstance().getPwcgInputDir(), "AirfieldHotspots.json");
		return airfieldHotSpotCollection;
	}
}
