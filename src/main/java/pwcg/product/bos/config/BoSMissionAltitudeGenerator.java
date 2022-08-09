package pwcg.product.bos.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IMissionAltitudeGenerator;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.Balloon;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.options.MissionWeather;

public class BoSMissionAltitudeGenerator implements IMissionAltitudeGenerator
{
    static public int DIVE_BOMB_ALT = 4100;

    public int determineFlightAltitude(Campaign campaign, FlightTypes flightType, MissionWeather missionWeather) throws PWCGException 
    {
        int altitude = determineDefaultFlightAltitude(campaign);
        altitude = missionWeather.recalculateAltitudeForCloudBase(altitude);
        
        if (flightType == FlightTypes.LOW_ALT_CAP  || 
            flightType == FlightTypes.LOW_ALT_PATROL)
        {
            altitude = determineLowAltitudePatrolAltitude();
            altitude = missionWeather.recalculateAltitudeForCloudBase(altitude);
        }
        else if (flightType == FlightTypes.LOW_ALT_BOMB)
        {
            altitude = determineLowAltitudeBombingAltitude();
            altitude = missionWeather.recalculateAltitudeForCloudBase(altitude);
        }
        else if (flightType == FlightTypes.GROUND_ATTACK || flightType == FlightTypes.GROUND_HUNT)
        {
            altitude = determineGroundAttackAltitude();
            altitude = missionWeather.recalculateAltitudeForCloudBase(altitude);
        }
        else if (flightType == FlightTypes.RAID)
        {
            altitude = determineRaidAltitude();
        }
        else if (flightType == FlightTypes.BOMB)
        {
            altitude = determineHighAltitudeBombingAltitude();
            altitude = missionWeather.recalculateAltitudeForCloudBase(altitude);
        }
        else if (flightType == FlightTypes.PARATROOP_DROP || flightType == FlightTypes.CARGO_DROP)
        {
            altitude = determineParaDropFlightAltitude();
            altitude = missionWeather.recalculateAltitudeForCloudBase(altitude);
        }
        else if (flightType == FlightTypes.SCRAMBLE)
        {
            altitude = determineScrambleFlightAltitude();
            altitude = missionWeather.recalculateAltitudeForCloudBase(altitude);
        }
        else if (flightType == FlightTypes.TRANSPORT)
        {
            altitude = determineTransportFlightAltitude();
            altitude = missionWeather.recalculateAltitudeForCloudBase(altitude);
        }
        else if (flightType == FlightTypes.SPY_EXTRACT)
        {
            altitude = determineSpyExtractFlightAltitude();
            altitude = missionWeather.recalculateAltitudeForCloudBase(altitude);
        }
        else if (flightType == FlightTypes.SPY_EXTRACT)
        {
            altitude = determineSpyExtractFlightAltitude();
            altitude = missionWeather.recalculateAltitudeForCloudBase(altitude);
        }
        else if (flightType == FlightTypes.CONTACT_PATROL)
        {
            altitude = determineContactPatrolFlightAltitude();
            altitude = missionWeather.recalculateAltitudeForCloudBase(altitude);
        }
        else if (flightType == FlightTypes.ARTILLERY_SPOT)
        {
            altitude = determineArtillerySpotFlightAltitude();
            altitude = missionWeather.recalculateAltitudeForCloudBase(altitude);
        }
        else if (flightType == FlightTypes.ESCORT)
        {
            altitude = determineHighAltitudeBombingAltitude() + 500;
            altitude = missionWeather.recalculateAltitudeForCloudBase(altitude);
        }
        else if (flightType == FlightTypes.DIVE_BOMB)
        {
            altitude = determineDiveBombFlightAltitude();
        }
        else if (flightType == FlightTypes.STRATEGIC_BOMB)
        {
            altitude = determineStrategicAltitudeBombingAltitude();
        }
        else if (flightType == FlightTypes.BALLOON_BUST)
        {
            altitude = determineBalloonBustAltitude();
        }
        else if (flightType == FlightTypes.BALLOON_DEFENSE)
        {
            altitude = determineBalloonDefenseAltitude();
        }
        
        return altitude;
    }

    private int determineDefaultFlightAltitude(Campaign campaign) throws PWCGException 
    {
        int altitude = 4000;

        try
        {
            int RandomAdditionalAltitude = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.RandomAdditionalAltitudeKey);
            
            List<Integer> baseAltitudes = new ArrayList<Integer>();        
            baseAltitudes.add(campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.BaseAltPeriod1Key));
            baseAltitudes.add(campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.BaseAltPeriod2Key));
            baseAltitudes.add(campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.BaseAltPeriod3Key));
            baseAltitudes.add(campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.BaseAltPeriod4Key));
            baseAltitudes.add(campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.BaseAltPeriod5Key));
            baseAltitudes.add(campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.BaseAltPeriod6Key));

            List<Date> baseAltitudePeriods = new ArrayList<Date>();        
            baseAltitudePeriods.add(DateUtils.getDateWithValidityCheck("01/09/1941"));
            baseAltitudePeriods.add(DateUtils.getDateWithValidityCheck("01/01/1942"));
            baseAltitudePeriods.add(DateUtils.getDateWithValidityCheck("01/06/1942"));
            baseAltitudePeriods.add(DateUtils.getDateWithValidityCheck("01/01/1943"));
            baseAltitudePeriods.add(DateUtils.getDateWithValidityCheck("01/06/1943"));
            baseAltitudePeriods.add(DateUtils.getDateWithValidityCheck("01/01/1944"));

            // Default to late war
            int baseAlt = baseAltitudes.get(baseAltitudes.size()-1);
            
            // Look for other periods before the last
            for (int i = 1; i < baseAltitudes.size(); ++i)
            {
                if (campaign.getDate().before(baseAltitudePeriods.get(i)))
                {
                    baseAlt = baseAltitudes.get(i-1);
                    break;
                }
            }
            
            int randomAlt = RandomNumberGenerator.getRandom(RandomAdditionalAltitude);
            altitude = baseAlt + randomAlt;
        }
        catch (Exception e)
        {
            // Just use the default if something bad happens
        }
        
        return altitude;
    }

    private int determineHighAltitudeBombingAltitude()
    {
        int altitude = 3000;
        int randomAlt = RandomNumberGenerator.getRandom(2000);
        altitude = altitude + randomAlt;            
        return altitude;
    }
        
    private int determineStrategicAltitudeBombingAltitude()
    {
        int altitude = 3000;
        int randomAlt = RandomNumberGenerator.getRandom(4000);
        altitude = altitude + randomAlt;            
        return altitude;
    }

    private int determineLowAltitudePatrolAltitude()
    {
        int altitude = 1500;
        int randomAlt = RandomNumberGenerator.getRandom(1000);
        altitude = altitude + randomAlt;            
        return altitude;
    }

    private int determineRaidAltitude()
    {
        int altitude = 300;
        int randomAlt = RandomNumberGenerator.getRandom(300);
        altitude = altitude + randomAlt;            
        return altitude;
    }

    private int determineLowAltitudeBombingAltitude()
    {
        int altitude = 1000;
        int randomAlt = RandomNumberGenerator.getRandom(600);
        altitude = altitude + randomAlt;            
        return altitude;
    }

    private int determineGroundAttackAltitude() 
    {
        int altitude = 800;
        int randomAlt = RandomNumberGenerator.getRandom(400);
        altitude = altitude + randomAlt;            

        return altitude;
    }
    
    private int determineDiveBombFlightAltitude() 
    {
        int altitude = DIVE_BOMB_ALT;
        return altitude;
    }
    
    private int determineParaDropFlightAltitude() throws PWCGException 
    {
        int altitude = 800;
        int randomAltitude = RandomNumberGenerator.getRandom(800);
        int additionalAltitudeForMountains = 0;

        FrontMapIdentifier map = PWCGContext.getInstance().getCurrentMap().getMapIdentifier();
        if (map == FrontMapIdentifier.KUBAN_MAP)
        {
            additionalAltitudeForMountains = 1000;
        }
        
        return altitude + randomAltitude + additionalAltitudeForMountains;
    }
    
    private int determineScrambleFlightAltitude() 
    {
        int altitude = 2000;
        int randomAlt = RandomNumberGenerator.getRandom(1000);
        
        altitude = altitude + randomAlt;
        
        return altitude;
    }
    
    private int determineTransportFlightAltitude() throws PWCGException 
    {
        int altitude = 2000;
        int randomAltitude = RandomNumberGenerator.getRandom(2000);
        int additionalAltitudeForMountains = 0;

        FrontMapIdentifier map = PWCGContext.getInstance().getCurrentMap().getMapIdentifier();
        if (map == FrontMapIdentifier.KUBAN_MAP)
        {
            additionalAltitudeForMountains = 1000;
        }
        return altitude + randomAltitude + additionalAltitudeForMountains;
    }

    private int determineArtillerySpotFlightAltitude() 
    {
        int altitude = 200 + RandomNumberGenerator.getRandom(300);
        return altitude;
    }

    private int determineContactPatrolFlightAltitude() 
    {
        int altitude = 500;
        int randomAlt = RandomNumberGenerator.getRandom(150);
        altitude = altitude + randomAlt;            
        return altitude;
    }

    private int determineSpyExtractFlightAltitude() 
    {
        int altitude = 500 + RandomNumberGenerator.getRandom(100);        
        return altitude;
    }

    private int determineBalloonBustAltitude()
    {
        return Balloon.BALLOON_ALTITUDE + 200;
    }

    private int determineBalloonDefenseAltitude()
    {
        return Balloon.BALLOON_ALTITUDE + 500;
    }
}
