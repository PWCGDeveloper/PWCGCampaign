package pwcg.campaign.ww2.airfield;

import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IAirfieldConfiguration;
import pwcg.campaign.context.PWCGDirectoryManager;
import pwcg.campaign.io.json.LocationIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.LocationSet;
import pwcg.core.location.PWCGLocation;

public class BoSAirfieldConfiguration implements IAirfieldConfiguration
{
    public  Map<String, IAirfield> configure (String mapName) throws PWCGException 
    {
        Map<String, IAirfield> airfields = new TreeMap<String, IAirfield>();
        
        airfields.clear();

	    String pwcgInputDir = PWCGDirectoryManager.getInstance().getPwcgInputDir() + mapName + "\\";
        LocationSet airfieldLocations = LocationIOJson.readJson(pwcgInputDir, AIRFIELD_LOCATION_FILE_NAME);
        for (PWCGLocation location : airfieldLocations.getLocations())
        {
            IAirfield field = new BoSAirfield();
            field.initializeAirfieldFromLocation(location);
            airfields.put(location.getName(), field);
        }

        return airfields;
    }

}
