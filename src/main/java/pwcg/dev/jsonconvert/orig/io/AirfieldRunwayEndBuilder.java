package pwcg.dev.jsonconvert.orig.io;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.airfield.AirfieldDescriptor;
import pwcg.campaign.group.airfield.AirfieldDescriptorSet;
import pwcg.campaign.group.airfield.Runway;
import pwcg.campaign.io.json.AirfieldDescriptorIOJson;

public class AirfieldRunwayEndBuilder
{
    public static void main(String[] args)
    {
        try 
        {
            String mapName = "Normandy";
            AirfieldDescriptorSet airfieldDescriptorSet = AirfieldDescriptorIOJson.readJson(PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\", "AirfieldLocations.json");
            for (AirfieldDescriptor airfieldDescriptor : airfieldDescriptorSet.getLocations())
            {
                for (Runway runway : airfieldDescriptor.getRunways())
                {
                    
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
