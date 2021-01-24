package pwcg.campaign.group.airfield.hotspot;

import java.util.Date;
import java.util.List;

import pwcg.campaign.api.IHotSpotTranslator;
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

public class AirfieldHotSpotTranslator implements IHotSpotTranslator
{
    private static double SPOT_DENSITY = 20;

    private Mission mission;
    private Airfield airfield;

    public AirfieldHotSpotTranslator(Mission mission, Airfield airfield)
    {
        this.mission = mission;
        this.airfield = airfield;
    }

    public List<HotSpot> getHotSpots(Airfield airfield, Date date) throws PWCGException
    {
        List<HotSpot> hotSpots = classifyAirfieldHotspots(airfield, date);
        hotSpots.addAll(classifyRandomHotspots(airfield));
        return hotSpots;
    }

    private List<HotSpot> classifyAirfieldHotspots(Airfield airfield, Date date) throws PWCGException
    {
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

    private List<HotSpot> classifyRandomHotspots(Airfield airfield) throws PWCGException
    {
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
            int roll = RandomNumberGenerator.getRandom(100);
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
        int numLights = 0;
        if (mission.isNightMission())
        {
            numLights = Math.max(numLights, 2);
        }
        return numLights;
    }

    private int determineNumAAAHotSpots(int numHotSpots) throws PWCGException
    {
        int numAAHotSpots = 4;

        IFlight flight = mission.getMissionFlightBuilder().getFlightForAirfield(airfield);
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

    private List<HotSpot> selectHotSpotsFromEmptySpace(Airfield airfield) throws PWCGException
    {
        EmptySpaceFinder emptySpaceFinder = new EmptySpaceFinder(mission);
        List<Coordinate> boundary = airfield.getBoundary();
        int targetNumber = (int) (Math.sqrt(MathUtils.polygonArea(boundary)) / SPOT_DENSITY);
        return emptySpaceFinder.findEmptySpaces(boundary, targetNumber);

    }
}
