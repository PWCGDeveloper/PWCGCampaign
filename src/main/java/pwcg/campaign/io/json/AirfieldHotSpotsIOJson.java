package pwcg.campaign.io.json;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.airfield.hotspot.AirfieldHotSpotCollection;
import pwcg.core.exception.PWCGException;

public class AirfieldHotSpotsIOJson 
{
	public static AirfieldHotSpotCollection readJson() throws PWCGException, PWCGException
	{
		JsonObjectReader<AirfieldHotSpotCollection> jsonReader = new JsonObjectReader<>(AirfieldHotSpotCollection.class);
		AirfieldHotSpotCollection airfieldHotSpotCollection = jsonReader.readJsonFile(PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir(), "AirfieldHotspots.json");
		return airfieldHotSpotCollection;
	}
}
