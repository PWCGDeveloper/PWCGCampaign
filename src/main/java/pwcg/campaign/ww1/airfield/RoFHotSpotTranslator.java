package pwcg.campaign.ww1.airfield;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IHotSpotTranslator;
import pwcg.campaign.group.airfield.AirfieldHotSpotDefinition;
import pwcg.campaign.group.airfield.HotSpot;
import pwcg.campaign.group.airfield.HotSpotManager;
import pwcg.campaign.group.airfield.HotSpotType;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;
import pwcg.core.utils.RandomNumberGenerator;

public class RoFHotSpotTranslator implements IHotSpotTranslator
{

    public List<HotSpot> getHotSpots(IAirfield airfield, Date date) throws PWCGException
    {
        List<HotSpot> hotSpots = new ArrayList<HotSpot>();
        
        List<AirfieldHotSpotDefinition> rofHotSpots =  HotSpotManager.getInstance().getAirfieldHotSpots(airfield.getModel());
        for (AirfieldHotSpotDefinition rofHotSpot : rofHotSpots)
        {
            HotSpot hotSpot = rofHotSpot.convert(airfield.getPosition().copy(), airfield.getOrientation().copy());
            hotSpots.add(hotSpot);
        }
        
        if (!hotSpots.isEmpty())
        {
            hotSpots.get(0).setHotSpotType(HotSpotType.HOTSPOT_TOWER);
            
            int numAAAHotSPots = determineNumAAAHotSpots(hotSpots.size());
            for (int i = 1; i < (numAAAHotSPots+1); ++i)
            {
                hotSpots.get(i).setHotSpotType(HotSpotType.HOTSPOT_AAA);
            }
    
            for (int i = (numAAAHotSPots+1); i < hotSpots.size(); ++i)
            {
                int roll =RandomNumberGenerator.getRandom(100);
                if (roll < 50)
                {
                    hotSpots.get(i).setHotSpotType(HotSpotType.HOTSPOT_PLANE);
                }
                else
                {
                    hotSpots.get(i).setHotSpotType(HotSpotType.HOTSPOT_ITEM);
                }
            }
        }
        else
        {
            Logger.log(LogLevel.ERROR, "Non critical issue: no hot spots for airfield " + airfield.getName());
        }

        return hotSpots;
    }

    private int determineNumAAAHotSpots(int numHotSPots)
    {
    	if (numHotSPots > 5)
    	{
    		return 2;
    	}
    	else if (numHotSPots > 3)
    	{
    		return 1;
    	}
    	
    	return 0;
    }


}
