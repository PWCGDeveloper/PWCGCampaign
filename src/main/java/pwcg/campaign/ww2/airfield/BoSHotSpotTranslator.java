package pwcg.campaign.ww2.airfield;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IHotSpotTranslator;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.GroupManager;
import pwcg.campaign.group.airfield.AirfieldHotSpotDefinition;
import pwcg.campaign.group.airfield.HotSpot;
import pwcg.campaign.group.airfield.HotSpotManager;
import pwcg.campaign.group.airfield.HotSpotType;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.RandomNumberGenerator;

public class BoSHotSpotTranslator implements IHotSpotTranslator
{

    public List<HotSpot> getHotSpots(IAirfield airfield, Date date) throws PWCGException
    {
        List<HotSpot> hotSpots = getNearbyAirfieldHotSpots(airfield, date);
        
        int numAAAHotSPots = determineNumAAAHotSpots(hotSpots.size());
        for (int i = 0; i < numAAAHotSPots; ++i)
        {
            hotSpots.get(i).setHotSpotType(HotSpotType.HOTSPOT_AAA);
        }
                
        for (int i = numAAAHotSPots; i < hotSpots.size(); ++i)
        {
            int roll =  RandomNumberGenerator.getRandom(100);
            if (roll < 90)
            {
                hotSpots.get(i).setHotSpotType(HotSpotType.HOTSPOT_PLANE);
            }
            else
            {
                hotSpots.get(i).setHotSpotType(HotSpotType.HOTSPOT_ITEM);
            }
        }
        
        return hotSpots;
    }
    
    private int determineNumAAAHotSpots(int numHotSPots)
    {
    	if (numHotSPots > 6)
    	{
    		return 5;
    	}
    	else if (numHotSPots > 4)
    	{
    		return 4;
    	}
    	else if (numHotSPots > 2)
    	{
    		return 2;
    	}
        else if (numHotSPots > 1)
        {
            return 1;
        }

    	return 0;
    }


    private List<HotSpot> getNearbyAirfieldHotSpots(IAirfield airfield, Date date) throws PWCGException
    {
        List<HotSpot> nearbyAirfieldHotSpots = new ArrayList<HotSpot>();
        
        GroupManager groupManager = PWCGContextManager.getInstance().getCurrentMap().getGroupManager();
        
        List<Block>nearbyBlocks = groupManager.getBlockFinder().getBlocksBySideWithinRadius(airfield.createCountry(date).getSide(), date, airfield.getPosition().copy(), 3000.0);
        for (Block block : nearbyBlocks)
        {
            List<AirfieldHotSpotDefinition> boSHotSpotDefinitions =  HotSpotManager.getInstance().getAirfieldHotSpots(block.getModel());
            if (!boSHotSpotDefinitions.isEmpty())
            {
                List<HotSpot> hotSpots = convertToHotSpot(block.getPosition(), block.getOrientation(), boSHotSpotDefinitions);
                
                nearbyAirfieldHotSpots.addAll(hotSpots);
            }
        }
        
            return nearbyAirfieldHotSpots;
    }


    private List<HotSpot> convertToHotSpot(Coordinate position, Orientation orientation, List<AirfieldHotSpotDefinition> boSHotSpotDefinitions) throws PWCGException
    {
        List<HotSpot> hotSpotsForBlock = new ArrayList<HotSpot>();

        for (AirfieldHotSpotDefinition boSHotSpotDefinition : boSHotSpotDefinitions)
        {
            HotSpot hotSpot = boSHotSpotDefinition.convert(position, orientation);
            hotSpotsForBlock.add(hotSpot);
        }

        return hotSpotsForBlock;
    }
}
