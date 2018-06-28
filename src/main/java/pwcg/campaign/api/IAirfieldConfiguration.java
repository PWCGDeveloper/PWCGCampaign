package pwcg.campaign.api;

import java.util.Map;

import pwcg.core.exception.PWCGException;

public interface IAirfieldConfiguration
{
	public static final String AIRFIELD_LOCATION_FILE_NAME = "AirfieldLocations";

	Map<String, IAirfield> configure(String mapName) throws PWCGException;

}