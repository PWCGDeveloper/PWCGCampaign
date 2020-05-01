package pwcg.campaign.group.airfield.hotspot;

import java.util.Date;
import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IHotSpotTranslator;
import pwcg.campaign.group.EmptySpaceFinder;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;

public class AirfieldHotSpotTranslator implements IHotSpotTranslator
{
    private static double SPOT_DENSITY = 20;

    private Mission mission;
    
    public AirfieldHotSpotTranslator(Mission mission)
    {
        this.mission = mission;
    }
    
    public List<HotSpot> getHotSpots(IAirfield airfield, Date date) throws PWCGException
    {
        List<HotSpot> hotSpots = classifyAirfieldHotspots(airfield, date);
        hotSpots.addAll(classifyRandomHotspots(airfield));

        return hotSpots;
    }

    private List<HotSpot> classifyAirfieldHotspots(IAirfield airfield, Date date) throws PWCGException {
        List<HotSpot> hotSpots = airfield.getNearbyHotSpots();
        
        for (HotSpot hotSpot : hotSpots)
        {
            int roll = RandomNumberGenerator.getRandom(100);
            if (roll < 90)
            {
                hotSpot.setHotSpotType(HotSpotType.HOTSPOT_PLANE);
            }
            else
            {
                hotSpot.setHotSpotType(HotSpotType.HOTSPOT_ITEM);
            }
        }
        return hotSpots;
    }

    private List<HotSpot> classifyRandomHotspots(IAirfield airfield) throws PWCGException {
        List<HotSpot> hotSpots = selectHotSpotsFromEmptySpace(airfield);

        
        int numAAAHotSpots = determineNumAAAHotSpots(hotSpots.size());
        for (int i = 0; i < numAAAHotSpots && i < hotSpots.size(); ++i)
        {
            hotSpots.get(i).setHotSpotType(HotSpotType.HOTSPOT_AAA);
        }
   
        int numSearchLightHotSpots = determineNumSearchLightHotSpots(hotSpots.size(), numAAAHotSpots);
        for (int i = numAAAHotSpots; i < numSearchLightHotSpots + numAAAHotSpots && i < hotSpots.size(); ++i)
        {
            hotSpots.get(i).setHotSpotType(HotSpotType.HOTSPOT_SEARCHLIGHT);
        }

        for (int i = numAAAHotSpots + numSearchLightHotSpots; i < hotSpots.size(); ++i)
        {
            int roll =  RandomNumberGenerator.getRandom(100);
            if (roll < 50)
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

    private int determineNumSearchLightHotSpots(int numHotSpots, int numAAAHotSpots)
    {
        int numLights = numAAAHotSpots / 4;
        if (mission.isNightMission())
            numLights = Math.max(numLights, 2);
        return numLights;
    }

    private int determineNumAAAHotSpots(int numHotSpots)
    {
        int numAAA = numHotSpots / 3;
        numAAA = Math.max(numAAA, 6);
        numAAA = Math.min(numAAA, 24);
        return numAAA;
    }

    private List<HotSpot> selectHotSpotsFromEmptySpace(IAirfield airfield) throws PWCGException
    {
        EmptySpaceFinder emptySpaceFinder = new EmptySpaceFinder();
        List<Coordinate> boundary = airfield.getBoundary();
        int targetNumber = (int) (Math.sqrt(MathUtils.polygonArea(boundary)) / SPOT_DENSITY);
        return emptySpaceFinder.findEmptySpaces(boundary, targetNumber);

    }
}
