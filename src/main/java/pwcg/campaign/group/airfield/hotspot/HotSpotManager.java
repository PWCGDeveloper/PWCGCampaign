package pwcg.campaign.group.airfield.hotspot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.io.json.JsonObjectReader;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.PWCGLogger;

public class HotSpotManager
{
    private static HotSpotManager instance = null;

    private Map<String, List<AirfieldHotSpotDefinition>> airfieldHotSpots = new HashMap<String, List<AirfieldHotSpotDefinition>>();

    public static HotSpotManager getInstance()
    {
        if (instance == null)
        {
            instance = new HotSpotManager();
            instance.configureAirfieldHotSpots();
        }
        
        return instance;
    }

    public void configureAirfieldHotSpots()
    {
        try 
        {
	        readJson(); 
        } 
        catch (Exception e) 
        {
            PWCGLogger.logException(e);
        }
    }
    

	private void readJson() throws PWCGException, PWCGIOException
	{
		JsonObjectReader<AirfieldHotSpotCollection> jsonReader = new JsonObjectReader<>(AirfieldHotSpotCollection.class);
		AirfieldHotSpotCollection airfieldHotSpotCollection = jsonReader.readJsonFile(PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir(), "AirfieldHotspots.json");
		for (String hotspotCollectionKey : airfieldHotSpotCollection.getAirfieldHotSpots().keySet())
		{
			List<AirfieldHotSpotDefinition> hotSpotsForField = airfieldHotSpotCollection.getAirfieldHotSpots().get(hotspotCollectionKey);
			if (!hotSpotsForField.isEmpty())
			{
				airfieldHotSpots.put(hotSpotsForField.get(0).getModel(), hotSpotsForField);
			}
		}
	}


    public List<AirfieldHotSpotDefinition> getAirfieldHotSpots(String model) 
    {
        List<AirfieldHotSpotDefinition> hotSpot = new ArrayList<AirfieldHotSpotDefinition>();
        if (airfieldHotSpots.containsKey(model))
        {
            hotSpot = airfieldHotSpots.get(model);
        }
        
        return hotSpot;
    }


}
