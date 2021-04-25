package pwcg.campaign.context;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.group.airfield.Airfield;

public class MapForAirfieldFinder
{

    public static List<FrontMapIdentifier> getMapForAirfield(String airfieldName)
    {
        List<FrontMapIdentifier> mapsForAirfield = new ArrayList<>();
        for (PWCGMap map : PWCGContext.getInstance().getMaps())
        {
            AirfieldManager airfieldManager = map.getAirfieldManager();
            if (airfieldManager != null)
            {
                Airfield airfield = airfieldManager.getAirfield(airfieldName);
                
                if (airfield != null)
                {
                    mapsForAirfield.add(map.getMapIdentifier());
                }
            }
        }

        return mapsForAirfield;
    }

}
