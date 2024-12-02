package pwcg.dev.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.io.json.SquadronIOJson;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class SquadronWipeAirfieldsAfterDate
{
    static public void main(String[] args)
    {
        UserDir.setUserDir();

        try
        {
            SquadronWipeAirfieldsAfterDate finder = new SquadronWipeAirfieldsAfterDate(PWCGProduct.FC);
            finder.findDuplicateAirfieldUse();
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
    }

    public SquadronWipeAirfieldsAfterDate(PWCGProduct product) throws PWCGException
    {
        PWCGContext.setProduct(product);
    }

    private void findDuplicateAirfieldUse() throws PWCGException
    {
        List<Squadron> allSq = PWCGContext.getInstance().getSquadronManager().getAllSquadrons();
        for (Squadron squadToCheck : allSq)
        {
            Map<Date, String> airfields = new TreeMap<>();
            for (Date airfieldDate : squadToCheck.getAirfields().keySet())
            {
                String airfieldName = squadToCheck.getAirfields().get(airfieldDate);
                Date wipeDate = DateUtils.getDateDashDelimitedYYYYMMDD("1918-04-01");
                if (airfieldDate.before(wipeDate))
                {
                    airfields.put(airfieldDate, airfieldName);
                }
            }
            
            squadToCheck.setAirfields(airfields);
            PWCGLogger.log(LogLevel.DEBUG, squadToCheck.getFileName());
            for (Date airfieldDate : squadToCheck.getAirfields().keySet())
            {
                String airfieldName = squadToCheck.getAirfields().get(airfieldDate);
                PWCGLogger.log(LogLevel.DEBUG, "    " + airfieldDate + "   " + airfieldName);
            }
            
            SquadronIOJson.writeJson(squadToCheck);
         }        
    }
}



