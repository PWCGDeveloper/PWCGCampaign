package pwcg.campaign.group.airfield;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.location.PWCGLocation;

public class AirfieldDescriptor extends PWCGLocation
{
    private List<Runway> runways = new ArrayList<>();

    public List<Runway> getRunways()
    {
        return runways;
    }

    public void setRunways(List<Runway> runways)
    {
        this.runways = runways;
    }
    
}
