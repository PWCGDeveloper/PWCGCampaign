package pwcg.campaign.group.airfield.hotspot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pwcg.campaign.group.EmptySpaceFinder;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;

public class AirfieldHotSpotTranslator
{
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
       if (hotSpots.size() > 30)
       {
           hotSpots = hotSpots.subList(0, 30);
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
        int numSearchLightHotSpots = determineNumSearchLightHotSpots();
        for (int i = 0; i < numSearchLightHotSpots; ++i)
        {
            classifyHotSpot(HotSpotType.HOTSPOT_SEARCHLIGHT);
        }

        int numAAAHotSpots = determineNumAAAHotSpots();
        for (int i = 0; i < numAAAHotSpots && i < hotSpots.size(); ++i)
        {
            classifyHotSpot(HotSpotType.HOTSPOT_AAA);
        }

        int maxStaticPlanes = determineNumStaticAirplanes();
        int numStaticPlanesAdded = 0;
        for (int i = 0; i < hotSpots.size(); ++i)
        {
            int roll = RandomNumberGenerator.getRandom(100);
            if (roll < 40 && numStaticPlanesAdded < maxStaticPlanes)
            {
                classifyHotSpot(HotSpotType.HOTSPOT_PLANE);
                ++numStaticPlanesAdded;
            }
            else
            {
                classifyHotSpot(HotSpotType.HOTSPOT_ITEM);
            }
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

    private int determineNumSearchLightHotSpots()
    {
        if (mission.isNightMission())
        {
            return 2;
        }
        return 0;
    }

    private int determineNumAAAHotSpots() throws PWCGException
    {
        int numAAHotSpots = 4;

        IFlight flight = mission.getMissionFlights().getFlightForAirfield(airfield);
        if (flight != null)
        {
            ConfigManagerCampaign configManager = flight.getCampaign().getCampaignConfigManager();
            String currentGroundSetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey);
    
            if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_HIGH))
            {
                numAAHotSpots = 8;
            }
            
            if (flight != null)
            {
                if (flight.isPlayerFlight())
                {
                     if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_LOW))
                    {
                        numAAHotSpots = 4;
                    }
                    else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
                    {
                        numAAHotSpots = 6;
                    }
                    else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_HIGH))
                    {
                        numAAHotSpots = 10;
                    }
                }
            }
        }
        return numAAHotSpots;
    }

    private int determineNumStaticAirplanes()
    {
        return 9;
    }
}
