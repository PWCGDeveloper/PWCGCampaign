package pwcg.dev.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class SquadronAifieldFirstOfMonth
{
    static public void main(String[] args)
    {
        UserDir.setUserDir();

        try
        {
            SquadronAifieldFirstOfMonth finder = new SquadronAifieldFirstOfMonth(PWCGProduct.FC);
            finder.findDuplicateAirfieldUse();
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
    }

    public SquadronAifieldFirstOfMonth(PWCGProduct product) throws PWCGException
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
                
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(airfieldDate.getTime());
                if (calendar.get(Calendar.DAY_OF_MONTH) == 1)
                {
                    airfields.put(airfieldDate, airfieldName);
                }
                else
                {
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                    Date newDate = new Date(calendar.getTimeInMillis());
                    airfields.put(newDate, airfieldName);
                }
            }
            
            squadToCheck.setAirfields(airfields);
            PWCGLogger.log(LogLevel.DEBUG, squadToCheck.getFileName());
            for (Date airfieldDate : squadToCheck.getAirfields().keySet())
            {
                String airfieldName = squadToCheck.getAirfields().get(airfieldDate);
                PWCGLogger.log(LogLevel.DEBUG, "    " + airfieldDate + "   " + airfieldName);
            }
         }        
    }
}



