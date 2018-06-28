package pwcg.campaign.ww1.config;

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

public class RoFMissionAltitudeGenerator implements IMissionAltitudeGenerator
{
    
    /**
     * @return
     * @throws PWCGException 
     * @
     */
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

    public int getBombingAltitude(BombingAltitudeLevel bombingAltitudeLevel)
    {
        // Default to medium
        int altitude = 1000;
        int randomAlt = RandomNumberGenerator.getRandom(200);

        if (bombingAltitudeLevel == BombingAltitudeLevel.HIGH)
        {
            altitude = 1500;
            randomAlt = RandomNumberGenerator.getRandom(500);
        }
        else if (bombingAltitudeLevel == BombingAltitudeLevel.LOW)
        {
            altitude = 300;
            randomAlt = RandomNumberGenerator.getRandom(200);
        }

        altitude = altitude + randomAlt;            

        return altitude;
    }


    public int getLowAltitudePatrolAltitude()
    {
        int altitude = 800;
        int randomAlt = RandomNumberGenerator.getRandom(600);
        altitude = altitude + randomAlt;            
        return altitude;
    }

}
