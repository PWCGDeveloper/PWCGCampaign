package pwcg.dev.utils;

import java.util.List;
import java.util.TreeMap;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class SquadronMapFinder 
{
	static public void main (String[] args)
	{
        UserDir.setUserDir();

        try
		{
			SquadronMapFinder finder = new SquadronMapFinder();
			finder.squadronIsOnMap();
		}
		catch (Exception e)
		{
			 PWCGLogger.logException(e);
		}
	}

    
    private void squadronIsOnMap() throws PWCGException  
    {     
        PWCGContext.setProduct(PWCGProduct.BOS);
        
        TreeMap<Integer, String> airfieldsOnMapSorted = new TreeMap<>();
        
        List<Squadron> allSq =  PWCGContext.getInstance().getSquadronManager().getAllSquadrons();
        for (Squadron squadron : allSq)
        {
            Airfield airfield = squadron.determineCurrentAirfieldCurrentMap(FrontMapIdentifier.NORMANDY_MAP, DateUtils.getDateYYYYMMDD("19430801"));
            if (airfield != null)
            {
                airfieldsOnMapSorted.put(squadron.getSquadronId(), airfield.getName());
            }
        }

        for (int squadronId : airfieldsOnMapSorted.keySet())
        {
            PWCGLogger.log(LogLevel.DEBUG, "" + squadronId);
        }
    }
}
