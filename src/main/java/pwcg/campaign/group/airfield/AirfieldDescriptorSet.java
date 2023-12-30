package pwcg.campaign.group.airfield;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AirfieldDescriptorSet
{
    private String locationSetName = "";
    private Map<String, AirfieldDescriptor> locations = new TreeMap<>();

    public String getLocationSetName()
    {
        return locationSetName;
    }

    public void setLocationSetName(String locationSetName)
    {
        this.locationSetName = locationSetName;
    }

    public List<AirfieldDescriptor> getLocations()
    {
        return new ArrayList<AirfieldDescriptor>(locations.values());
    }

    public void setLocations(List<AirfieldDescriptor> locations)
    {
        for (AirfieldDescriptor airfield : locations)
        {
            this.locations.put(airfield.getName(), airfield);
        }
    }

}
