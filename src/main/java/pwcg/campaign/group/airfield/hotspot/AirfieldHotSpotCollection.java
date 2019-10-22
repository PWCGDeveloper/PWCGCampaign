package pwcg.campaign.group.airfield.hotspot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AirfieldHotSpotCollection
{
	private Map<String, List<AirfieldHotSpotDefinition>> airfieldHotSpots = new HashMap<>();

	public Map<String, List<AirfieldHotSpotDefinition>> getAirfieldHotSpots()
	{
		return airfieldHotSpots;
	}

	public void setAirfieldHotSpots(Map<String, List<AirfieldHotSpotDefinition>> airfieldHotSpots)
	{
		this.airfieldHotSpots = airfieldHotSpots;
	}

	public void addAirfieldHotSpots(String airfieldModel, List<AirfieldHotSpotDefinition> airfieldHotSpots)
	{
		this.airfieldHotSpots.put(airfieldModel, airfieldHotSpots);
	}

	public List<AirfieldHotSpotDefinition> retrieveAirfieldHotSpots(String airfieldModel)
	{
		return this.airfieldHotSpots.get(airfieldModel);
	}
	
}
