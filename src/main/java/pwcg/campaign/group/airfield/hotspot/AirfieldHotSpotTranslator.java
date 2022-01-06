package pwcg.campaign.group.airfield.hotspot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pwcg.campaign.group.EmptySpaceFinder;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;

public class AirfieldHotSpotTranslator
{
    private static final int MAX_HOT_SPOTS = 30;
    private static double SPOT_DENSITY = 20;

    private Mission mission;
    private Airfield airfield;
    private List<HotSpot> hotSpots = new ArrayList<>();
    int nextHotSpot = 0;

    public AirfieldHotSpotTranslator(Mission mission, Airfield airfield)
    {
        this.mission = mission;
        this.airfield = airfield;
    }

    public List<HotSpot> getHotSpots() throws PWCGException
    {
       hotSpots = selectHotSpotsFromEmptySpace();
       Collections.shuffle(hotSpots);
       
       classifyAirfieldHotspots();
       if (hotSpots.size() > MAX_HOT_SPOTS)
       {
           hotSpots = hotSpots.subList(0, MAX_HOT_SPOTS);
       }
       return hotSpots;
    }

    private List<HotSpot> selectHotSpotsFromEmptySpace() throws PWCGException
    {
        EmptySpaceFinder emptySpaceFinder = new EmptySpaceFinder(mission);
        List<Coordinate> boundary = airfield.getBoundary();
        int targetNumber = (int) (Math.sqrt(MathUtils.polygonArea(boundary)) / SPOT_DENSITY);
        return emptySpaceFinder.findEmptySpaces(boundary, targetNumber);
    }

    private void classifyAirfieldHotspots() throws PWCGException
    {
        int numAAAHotSpots = determineNumAAAHotSpots();
        for (int i = 0; i < numAAAHotSpots && i < hotSpots.size(); ++i)
        {
            classifyHotSpot(HotSpotType.HOTSPOT_AAA);
        }
    }
    
    private void classifyHotSpot(HotSpotType hotSpotType)
    {
        if (nextHotSpot < hotSpots.size())
        {
            hotSpots.get(nextHotSpot).setHotSpotType(hotSpotType);
            ++nextHotSpot;
        }
    }

    private int determineNumAAAHotSpots() throws PWCGException
    {
        int numAAHotSpots = 4;
        return numAAHotSpots;
    }
}
