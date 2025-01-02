package pwcg.product.fc.config;

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

public class FCMissionAltitudeGenerator implements IMissionAltitudeGenerator
{	
    public int determineFlightAltitude(Campaign campaign, FlightTypes flightType, MissionWeather missionWeather) throws PWCGException 
    {
		int altitude = determineDefaultFlightAltitude(campaign);
        altitude = missionWeather.recalculateAltitudeForCloudBase(altitude);

        try {
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
	        else if (flightType == FlightTypes.ARTILLERY_SPOT)
	        {
	            altitude = determineArtillerySpotAltitude(campaign);
	        }
	        else if (flightType == FlightTypes.RECON)
	        {
	            altitude = determineReconAltitude(campaign);
	        }
	        else if (flightType == FlightTypes.BOMB)
	        {
	            altitude = determineHighAltitudeBombingAltitude(campaign);
	            altitude = missionWeather.recalculateAltitudeForCloudBase(altitude);
	        }
	        else if (flightType == FlightTypes.PARATROOP_DROP || flightType == FlightTypes.CARGO_DROP)
	        {
	            altitude = determineParaDropFlightAltitude(campaign);
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
	            altitude = determineHighAltitudeBombingAltitude(campaign) + 200;
	            altitude = missionWeather.recalculateAltitudeForCloudBase(altitude);
	        }
	        else if (flightType == FlightTypes.STRATEGIC_BOMB)
	        {
	            altitude = determineStrategicAltitudeBombingAltitude();
	        }
	        else if (flightType == FlightTypes.DIVE_BOMB)
	        {
	            altitude = determineDiveBombFlightAltitude();
	        }
	        else if (flightType == FlightTypes.BALLOON_BUST)
	        {
	            altitude = determineBalloonBustAltitude();
	        }
	        else if (flightType == FlightTypes.BALLOON_DEFENSE)
	        {
	            altitude = determineBalloonDefenseAltitude();
	        }
        }
        catch (Exception e)
        {
        	altitude = determineDefaultFlightAltitude(campaign);
        }

        return altitude;
    }

	private int determineDefaultFlightAltitude(Campaign campaign) throws PWCGException 
    {
        int altitude = 2500;

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
            baseAltitudePeriods.add(DateUtils.getDateWithValidityCheck("01/08/1914"));
            baseAltitudePeriods.add(DateUtils.getDateWithValidityCheck("01/05/1916"));
            baseAltitudePeriods.add(DateUtils.getDateWithValidityCheck("01/01/1917"));
            baseAltitudePeriods.add(DateUtils.getDateWithValidityCheck("01/06/1917"));
            baseAltitudePeriods.add(DateUtils.getDateWithValidityCheck("01/01/1918"));
            baseAltitudePeriods.add(DateUtils.getDateWithValidityCheck("01/06/1918"));

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

    private int determineArtillerySpotAltitude(Campaign campaign) throws PWCGException {
        int altitude = 1000;
        if (campaign.getDate().before(DateUtils.getDateYYYYMMDD("19170101")))
        {
        	altitude = 500;
        }
        int randomAlt = RandomNumberGenerator.getRandom(500);
        altitude += randomAlt;
        return altitude;
	}
    
    
	private int determineReconAltitude(Campaign campaign) throws PWCGException {
        int altitude = 3000;
        int randomAlt = RandomNumberGenerator.getRandom(1000);
        if (campaign.getDate().before(DateUtils.getDateYYYYMMDD("19170101")))
        {
            altitude = 2000;
            randomAlt = RandomNumberGenerator.getRandom(1000);
        }
        else if (campaign.getDate().before(DateUtils.getDateYYYYMMDD("19180101")))
        {
            altitude = 1200;
            randomAlt = RandomNumberGenerator.getRandom(500);
        }
        altitude += randomAlt;
        return altitude;
	}


    private int determineHighAltitudeBombingAltitude(Campaign campaign) throws PWCGException
    {
        int altitude = 2000;
        int randomAlt = RandomNumberGenerator.getRandom(2000);
        if (campaign.getDate().before(DateUtils.getDateYYYYMMDD("19170101")))
        {
        	altitude = 1000;
            randomAlt = RandomNumberGenerator.getRandom(500);
        }
        altitude = altitude + randomAlt;            
        return altitude;
    }
    
    
    private int determineStrategicAltitudeBombingAltitude()
    {
        int altitude = 2500;
        int randomAlt = RandomNumberGenerator.getRandom(1000);
        altitude = altitude + randomAlt;            
        return altitude;
    }


    private int determineLowAltitudePatrolAltitude()
    {
        int altitude = 1000;
        int randomAlt = RandomNumberGenerator.getRandom(500);
        altitude = altitude + randomAlt;            
        return altitude;
    }

    private int determineRaidAltitude()
    {
        int altitude = 200;
        int randomAlt = RandomNumberGenerator.getRandom(100);
        altitude = altitude + randomAlt;            
        return altitude;
    }

    private int determineLowAltitudeBombingAltitude()
    {
        int altitude = 700;
        int randomAlt = RandomNumberGenerator.getRandom(400);
        altitude = altitude + randomAlt;            
        return altitude;
    }

    private int determineGroundAttackAltitude() 
    {
        int altitude = 600;
        int randomAlt = RandomNumberGenerator.getRandom(200);
        altitude = altitude + randomAlt;            

        return altitude;
    }
    
    private int determineDiveBombFlightAltitude() 
    {
        int altitude = 4100;
        return altitude;
    }
    
    private int determineParaDropFlightAltitude(Campaign campaign) throws PWCGException 
    {
        int altitude = 800;
        int randomAltitude = RandomNumberGenerator.getRandom(800);
        int additionalAltitudeForMountains = 0;

        FrontMapIdentifier map = PWCGContext.getInstance().getMap(campaign.getCampaignMap()).getMapIdentifier();
        if (map == FrontMapIdentifier.KUBAN_MAP)
        {
            additionalAltitudeForMountains = 1000;
        }
        
        return altitude + randomAltitude + additionalAltitudeForMountains;
    }
    
    private int determineScrambleFlightAltitude() 
    {
        int altitude = 1200;
        int randomAlt = RandomNumberGenerator.getRandom(500);
        
        altitude = altitude + randomAlt;
        
        return altitude;
    }
    
    private int determineTransportFlightAltitude() throws PWCGException 
    {
        int altitude = 1000;
        int randomAltitude = RandomNumberGenerator.getRandom(2000);
        return altitude + randomAltitude;
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
