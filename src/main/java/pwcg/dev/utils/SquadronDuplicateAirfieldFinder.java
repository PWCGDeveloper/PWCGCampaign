package pwcg.dev.utils;

import java.util.ArrayList;
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

public class SquadronDuplicateAirfieldFinder
{
    static public void main(String[] args)
    {
        UserDir.setUserDir();

        try
        {
            SquadronDuplicateAirfieldFinder finder = new SquadronDuplicateAirfieldFinder(PWCGProduct.FC);
            finder.findDuplicateAirfieldUse();
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
    }

    public SquadronDuplicateAirfieldFinder(PWCGProduct product) throws PWCGException
    {
        PWCGContext.setProduct(product);
    }

    private void findDuplicateAirfieldUse() throws PWCGException
    {
        List<Squadron> allSq = PWCGContext.getInstance().getSquadronManager().getAllSquadrons();
        Map<Date, List<String>> duplicateAirfieldUsage = new TreeMap<>();
        for (Squadron squadToCheck : allSq)
        {
            for (Squadron otherSquad : allSq)
            {
                if (squadToCheck.getSquadronId() == otherSquad.getSquadronId())
                {
                    continue;
                }

                Map<Date, String> squadToCheckAirfields = squadToCheck.getAirfields();
                Map<Date, String> otherSquadquadAirfields = otherSquad.getAirfields();
                for (Date squadToCheckAirfieldDate : squadToCheckAirfields.keySet())
                {
                    for (Date otherSquaadAirfieldDate : otherSquadquadAirfields.keySet())
                    {
                        if (squadToCheckAirfieldDate.equals(otherSquaadAirfieldDate)) 
                        {
                            String squadToCheckAirfield = squadToCheckAirfields.get(squadToCheckAirfieldDate);
                            String otherSquaadAirfield = otherSquadquadAirfields.get(otherSquaadAirfieldDate);
                            if(squadToCheckAirfield.equals(otherSquaadAirfield))
                            {
                                if (!duplicateAirfieldUsage.containsKey(squadToCheckAirfieldDate))
                                {
                                    List<String> duplicates = new ArrayList<>();
                                    duplicateAirfieldUsage.put(squadToCheckAirfieldDate, duplicates);
                                }
                                List<String> duplicates = duplicateAirfieldUsage.get(squadToCheckAirfieldDate);
                                        
                                String message =  "    Duplicate airfield use found " + squadToCheck.getFileName() + " and " + otherSquad.getFileName()
                                + " share airfield " + squadToCheckAirfield + " on " + squadToCheckAirfieldDate;
                                
                                duplicates.add(message);
                            }
                        }
                    }                    
                }
             }
        }
        
        for (Date date : duplicateAirfieldUsage.keySet())
        {
            PWCGLogger.log(LogLevel.DEBUG, date.toString());
            List<String> duplicates = duplicateAirfieldUsage.get(date);
            for (String message : duplicates)
            {
                PWCGLogger.log(LogLevel.DEBUG, message);
            }
        }
    }
}
