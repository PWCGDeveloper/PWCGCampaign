package pwcg.dev.utils;

import pwcg.campaign.api.IAirfieldConfiguration;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.group.GroupManager;
import pwcg.campaign.io.json.AirfieldDescriptorIOJson;
import pwcg.campaign.io.json.LocationIOJson;
import pwcg.campaign.ww2.airfield.BoSAirfield;
import pwcg.campaign.ww2.airfield.BoSAirfieldConfiguration.AirfieldDescriptorSet;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.LocationSet;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.Logger;

public class TownNamer
{
    static public void main (String[] args)
    {
        UserDir.setUserDir();

        try
        {
            TownNamer townNamer = new TownNamer();
            townNamer.nameTowns();
        }
        catch (Exception e)
        {
             Logger.logException(e);;
        }
    }

   
    public void nameTowns() throws PWCGException 
    {
        PWCGContextManager.setRoF(false);
        
        String pwcgInputDir = PWCGContextManager.getInstance().getDirectoryManager().getPwcgInputDir() + "Bodenplatte" + "\\";
        AirfieldDescriptorSet airfieldDescriptors = AirfieldDescriptorIOJson.readJson(pwcgInputDir, IAirfieldConfiguration.AIRFIELD_LOCATION_FILE_NAME);
        for (BoSAirfield.AirfieldDescriptor desc : airfieldDescriptors.locations)
        {
            System.out.println(desc.getName());
        }
        
        LocationSet townLocations = LocationIOJson.readJson(pwcgInputDir, GroupManager.TOWN_LOCATION_FILE_NAME);
        for (PWCGLocation location : townLocations.getLocations())
        {
            //System.out.println(location.getName());
        }
    }
}
