package pwcg.dev.utils;

import java.util.List;
import java.util.TreeMap;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;

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
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.KUBAN_MAP);
        
        TreeMap<Integer, String> airfieldsOnMapSorted = new TreeMap<>();
        
        List<Squadron> allSq =  PWCGContext.getInstance().getSquadronManager().getAllSquadrons();
        for (Squadron squadron : allSq)
        {
            IAirfield airfield = squadron.determineCurrentAirfieldCurrentMap(DateUtils.getDateYYYYMMDD("19430801"));
            if (airfield != null)
            {
                airfieldsOnMapSorted.put(squadron.getSquadronId(), airfield.getName());
            }
        }

        for (int squadronId : airfieldsOnMapSorted.keySet())
        {
            System.out.println(squadronId);
        }
    }
}
