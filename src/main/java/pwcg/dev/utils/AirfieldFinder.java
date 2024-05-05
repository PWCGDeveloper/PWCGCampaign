package pwcg.dev.utils;

import java.util.HashSet;
import java.util.List;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.io.json.SquadronIOJson;
import pwcg.campaign.squadron.Squadron;

public class AirfieldFinder
{
    public static void main(String[] args)
    {
        try
        {
            PWCGContext.setProduct(PWCGProduct.FC);
            PWCGContext.getInstance(); 

            AirfieldManager airfieldManager = new AirfieldManager();
            airfieldManager.configure(FrontMapIdentifier.WESTERN_FRONT_MAP);

            List<Squadron> squadrons = SquadronIOJson.readJson(); 
            HashSet<String> airfields = new HashSet<>();
            HashSet<String> missingAirfields = new HashSet<>();
            
            for (Squadron squadron : squadrons)
            {
                for (String airfieldName : squadron.getAirfields().values())
                {
                    Airfield airfield = airfieldManager.getAirfield(airfieldName);
                    if (airfield == null)
                    {
                        missingAirfields.add(airfieldName);
                    }
                    else 
                    {
                        airfields.add(airfieldName);
                    }
                }
            }
            
            for (String missingAirfield : missingAirfields)
            {
                System.out.println(missingAirfield);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
}
