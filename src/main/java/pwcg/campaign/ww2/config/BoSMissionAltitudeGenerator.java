package pwcg.campaign.ww2.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IMissionAltitudeGenerator;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.bomb.BombingWaypoints.BombingAltitudeLevel;

public class BoSMissionAltitudeGenerator implements IMissionAltitudeGenerator
{

    public int flightAltitude(Campaign campaign) throws PWCGException 
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

    public int getBombingAltitude(BombingAltitudeLevel bombingAltitudeLevel)
    {
        // Default to medium
        int altitude = 3000;
        int randomAlt = RandomNumberGenerator.getRandom(800);

        if (bombingAltitudeLevel == BombingAltitudeLevel.HIGH)
        {
            altitude = 3000;
            randomAlt = RandomNumberGenerator.getRandom(2000);
        }
        else if (bombingAltitudeLevel == BombingAltitudeLevel.LOW)
        {
            altitude = 1000;
            randomAlt = RandomNumberGenerator.getRandom(600);
        }
        else
        {
            altitude = 3000;
            randomAlt = RandomNumberGenerator.getRandom(1000);
        }

        altitude = altitude + randomAlt;            

        return altitude;
    }

    public int getLowAltitudePatrolAltitude()
    {
        int altitude = 1500;
        int randomAlt = RandomNumberGenerator.getRandom(1000);
        altitude = altitude + randomAlt;            
        return altitude;
    }

}
