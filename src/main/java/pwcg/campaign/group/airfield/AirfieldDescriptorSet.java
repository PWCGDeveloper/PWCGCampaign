package pwcg.campaign.group.airfield;

import java.util.ArrayList;
import java.util.List;

public class AirfieldDescriptorSet
{
    private String locationSetName = "";
    private List<AirfieldDescriptor> locations = new ArrayList<>();

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
        return locations;
    }

    public void setLocations(List<AirfieldDescriptor> locations)
    {
        this.locations = locations;
    }

}
