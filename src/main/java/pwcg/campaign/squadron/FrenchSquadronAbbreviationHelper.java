package pwcg.campaign.squadron;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.plane.PlaneArchType;
import pwcg.core.exception.PWCGException;

public class FrenchSquadronAbbreviationHelper
{
    private Map<String, String> frenchAbbreviations = new HashMap<String, String>();
    
    public FrenchSquadronAbbreviationHelper ()
    {
        initializeFrenchAbbreviations();
    }
    
    public String determineFrenchDisplayName (Squadron squadron, Date date, String displayName) throws PWCGException 
    {        
        ICountry squadronCountry = squadron.determineServiceForSquadron(date).getCountry();
        if (squadronCountry.isCountry(Country.FRANCE))
        {
            for (String planeManufacturer : frenchAbbreviations.keySet())
            {
                List<PlaneArchType> planeArchType = squadron.determineCurrentAircraftArchTypes(date);
                if (planeArchType != null)
                {
                    if (planeArchType.contains(planeManufacturer))
                    {
                        int index = displayName.indexOf(" ");
                        String newDisplayName = displayName.substring(0, index+1);
                        newDisplayName += frenchAbbreviations.get(planeManufacturer);
                        newDisplayName += displayName.substring(index+1);
                        displayName = newDisplayName;
                    }
                }
            }
        }
        
        return displayName;
    }

    private void initializeFrenchAbbreviations()
    {
        frenchAbbreviations.put("nieuport", "N.");
        frenchAbbreviations.put("spad", "SPA.");
        frenchAbbreviations.put("sop", "SOP.");
        frenchAbbreviations.put("breguet", "BR.");
    }

}
