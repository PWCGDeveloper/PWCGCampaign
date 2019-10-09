package pwcg.product.rof.airfield;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IAirfieldConfiguration;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.FixedPosition;
import pwcg.campaign.group.FixedPositions;
import pwcg.campaign.io.json.FixedPositionIOJson;
import pwcg.campaign.io.json.LocationIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.LocationSet;
import pwcg.core.location.PWCGLocation;

public class RoFAirfieldConfiguration implements IAirfieldConfiguration
{
	
    @Override
    public  Map<String, IAirfield> configure (String mapName) throws PWCGException 
    {        
	    String pwcgInputDir = PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\";
        LocationSet airfieldLocations = LocationIOJson.readJson(pwcgInputDir, AIRFIELD_LOCATION_FILE_NAME);
        
		Map<String, IAirfield> airfieldMap = extractAirfieldsFromRoFAirfields(mapName, airfieldLocations);
		mapAirfieldsWIthLocations(airfieldMap, airfieldLocations);
		
        validateAllLocationsHaveAirfield(airfieldMap, airfieldLocations);
        validateAllAirfieldsHaveLocation(airfieldMap, airfieldLocations);

        return airfieldMap;
    }

	private Map<String, IAirfield> extractAirfieldsFromRoFAirfields(String mapName, LocationSet airfieldLocations)
	        throws PWCGException, PWCGIOException
	{
		String directory = PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\";
        FixedPositions rofAirfieldFixedPositions = FixedPositionIOJson.readJson(directory, "RoFAirfields");
        Map<String, IAirfield> airfieldMap = buildRofAirfieldMap(rofAirfieldFixedPositions, airfieldLocations);
		return airfieldMap;
	}

	private Map<String, IAirfield> buildRofAirfieldMap(FixedPositions rofAirfieldFixedPositions, LocationSet airfieldLocations) throws PWCGException
	{
		Map<String, IAirfield> airfieldMap = new HashMap<>();
        for (PWCGLocation location : airfieldLocations.getLocations())
        {
        	FixedPosition rofAirfieldFixedPosition = rofAirfieldFixedPositions.getFixedPosition(location.getName());
            if (rofAirfieldFixedPosition != null)
            {
            	RoFAirfield rofAirfield = new RoFAirfield(rofAirfieldFixedPosition);
                rofAirfield.initializeAirfieldFromLocation(location);            
            }
            else
            {
                throw new PWCGException("No icon for airfield " + location.getName());
            }
            
            RoFAirfield rofAirfield = new RoFAirfield(rofAirfieldFixedPosition);
            airfieldMap.put(location.getName(), rofAirfield);
        }
		return airfieldMap;
	}

	private void mapAirfieldsWIthLocations(Map<String, IAirfield> airfieldMap, LocationSet airfieldLocations) throws PWCGException
	{
		for (PWCGLocation location : airfieldLocations.getLocations())
        {
            if (airfieldMap.containsKey(location.getName()))
            {
            	RoFAirfield airfield = (RoFAirfield)airfieldMap.get(location.getName());
            	airfield.initializeAirfieldFromLocation(location);
            }
            else
            {
                throw new PWCGException("Unable to map location to airfield " + location.getName());
            }
        }		
	}

	private void validateAllLocationsHaveAirfield(Map<String, IAirfield> airfieldMap, LocationSet airfieldLocations) throws PWCGException
	{
		for (PWCGLocation location : airfieldLocations.getLocations())
        {
            if (!airfieldMap.containsKey(location.getName()))
            {
                throw new PWCGException("No RoF airfield for " + location.getName());
            }
        }
	}

	private void validateAllAirfieldsHaveLocation(Map<String, IAirfield> airfieldMap, LocationSet airfieldLocations) throws PWCGException
	{
        Map<String, PWCGLocation> airfieldIcons = loadAirfieldLocations(airfieldLocations);
		for (IAirfield airfield : airfieldMap.values())
        {
            if (!airfieldIcons.containsKey(airfield.getName()))
            {
                throw new PWCGException("No location for RoF airfield " + airfield.getName());
            }
        }
	}

	private Map<String, PWCGLocation> loadAirfieldLocations(LocationSet airfieldLocations)
	{
		Map<String, PWCGLocation> airfieldIcons = new TreeMap<>();
        for (PWCGLocation location : airfieldLocations.getLocations())
        {
            airfieldIcons.put(location.getName(), location);
        }
		return airfieldIcons;
	}
}
