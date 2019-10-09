package pwcg.product.rof.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IMissionAltitudeGenerator;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;

public class RoFMissionAltitudeGenerator implements IMissionAltitudeGenerator
{
    public int determineFlightAltitude(Campaign campaign, FlightTypes flightType) throws PWCGException 
    {
        if (flightType == FlightTypes.LOW_ALT_CAP  || 
            flightType == FlightTypes.LOW_ALT_PATROL)
        {
            return determineLowAltitudePatrolAltitude();
        }
        else if (flightType == FlightTypes.LOW_ALT_BOMB)
        {
            return determineLowAltitudeBombingAltitude();
        }
        else if (flightType == FlightTypes.GROUND_ATTACK)
        {
            return determineGroundAttackAltitude();
        }
        else if (flightType == FlightTypes.BOMB)
        {
            return determineHighAltitudeBombingAltitude();
        }
        else if (flightType == FlightTypes.STRATEGIC_BOMB)
        {
            return determineStrategicAltitudeBombingAltitude();
        }
        else if (flightType == FlightTypes.DIVE_BOMB)
        {
            return determineDiveBombFlightAltitude();
        }
        else if (flightType == FlightTypes.PARATROOP_DROP || flightType == FlightTypes.CARGO_DROP)
        {
            return determineParaDropFlightAltitude();
        }
        else if (flightType == FlightTypes.SCRAMBLE)
        {
            return determineScrambleFlightAltitude();
        }
        else if (flightType == FlightTypes.TRANSPORT)
        {
            return determineTransportFlightAltitude();
        }
        else if (flightType == FlightTypes.SPY_EXTRACT)
        {
            return determineSpyExtractFlightAltitude();
        }
        else if (flightType == FlightTypes.SPY_EXTRACT)
        {
            return determineSpyExtractFlightAltitude();
        }
        else if (flightType == FlightTypes.CONTACT_PATROL)
        {
            return determineContactPatrolFlightAltitude();
        }
        else if (flightType == FlightTypes.ARTILLERY_SPOT)
        {
            return determineArtillerySpotFlightAltitude();
        }
        else if (flightType == FlightTypes.ESCORT)
        {
            return determineHighAltitudeBombingAltitude() + 500;
        }
        else
        {
            return determineDefaultFlightAltitude(campaign);
        }
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

    private int determineHighAltitudeBombingAltitude()
    {
        int altitude = 2000;
        int randomAlt = RandomNumberGenerator.getRandom(2000);
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
}
