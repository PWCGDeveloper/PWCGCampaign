package pwcg.campaign.squadron;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.plane.PlaneType;
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
                PlaneType plane = squadron.determineCurrentAircraftNoCheck(date);
                if (plane != null)
                {
                    String planeName = plane.getDisplayName();
                    if (planeName.contains(planeManufacturer))
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
        frenchAbbreviations.put("Nieuport", "N.");
        frenchAbbreviations.put("SPAD", "SPA.");
        frenchAbbreviations.put("Sopwith", "SOP.");
        frenchAbbreviations.put("Salmson", "SAL.");
        frenchAbbreviations.put("Breguet", "BR.");
        frenchAbbreviations.put("Voisin", "VB.");
        frenchAbbreviations.put("Farman", "MF.");
        frenchAbbreviations.put("Dorand", "AR.");
        frenchAbbreviations.put("Caudron", "C.");
        frenchAbbreviations.put("Morane Saulnier", "MS.");
    }

}
