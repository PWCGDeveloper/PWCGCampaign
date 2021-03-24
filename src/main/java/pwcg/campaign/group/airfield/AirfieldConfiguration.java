package pwcg.campaign.group.airfield;

import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.io.json.AirfieldDescriptorIOJson;
import pwcg.core.exception.PWCGException;

public class AirfieldConfiguration
{
    public static final String AIRFIELD_LOCATION_FILE_NAME = "AirfieldLocations";
    
    public Map<String, Airfield> configure (FrontMapIdentifier mapIdentifier) throws PWCGException
    {
        Map<String, Airfield> airfields = new TreeMap<String, Airfield>();
        
        airfields.clear();

        String pwcgInputDir = PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapIdentifier.getMapName() + "\\";
        AirfieldDescriptorSet airfieldDescriptors = AirfieldDescriptorIOJson.readJson(pwcgInputDir, AIRFIELD_LOCATION_FILE_NAME);
        for (AirfieldDescriptor desc : airfieldDescriptors.getLocations())
        {
            Airfield field = new Airfield();
            field.initializeAirfieldFromDescriptor(desc);
            airfields.put(desc.getName(), field);
        }

        return airfields;
    }
}