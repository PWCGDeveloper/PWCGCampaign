package pwcg.mission.options;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class MissionWeatherTest
{
    @Mock Campaign campaign;
    @Mock ConfigManagerCampaign configManager;
    
    @Before 
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        PWCGContext.getInstance().setCurrentMap(FrontMapIdentifier.BODENPLATTE_MAP);

        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManager);
        
        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManager);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19441001"));

        Mockito.when(configManager.getIntConfigParam(ConfigItemKeys.MaxWindKey)).thenReturn(20);
        Mockito.when(configManager.getIntConfigParam(ConfigItemKeys.MinHazeKey)).thenReturn(2);
        Mockito.when(configManager.getIntConfigParam(ConfigItemKeys.MaxHazeKey)).thenReturn(8);
        Mockito.when(configManager.getIntConfigParam(ConfigItemKeys.MaxTurbulenceKey)).thenReturn(8);
    }

    @Test
    public void testRealWeather() throws PWCGException
    {
        Mockito.when(configManager.getIntConfigParam(ConfigItemKeys.UseRealisticWeatherKey)).thenReturn(1);

        boolean clearWeather = false;
        boolean lightWeather = false;
        boolean averageWeather = false;
        boolean heavyWeather = false;
        boolean overcastWeather = false;

        for (int i = 0; i < 50; ++i)
        {
            int missionTimeHours = 10;
            MissionWeather weather = new MissionWeather(campaign, missionTimeHours);
            weather.createMissionWeather();
            
            if (weather.getCloudConfig().toLowerCase().contains("clear"))
            {
                MissionWeatherTest.verifyClearWeather(weather);
                clearWeather = true;
            }
            else if (weather.getCloudConfig().toLowerCase().contains("light"))
            {
                MissionWeatherTest.verifyLightWeather(weather);
                lightWeather = true;
            }
            else if (weather.getCloudConfig().toLowerCase().contains("medium"))
            {
                MissionWeatherTest.verifyMediumWeather(weather);
                averageWeather = true;
            }
            else if (weather.getCloudConfig().toLowerCase().contains("heavy"))
            {
                MissionWeatherTest.verifyHeavyWeather(weather);
                heavyWeather = true;
            }
            else if (weather.getCloudConfig().toLowerCase().contains("overcast"))
            {
                MissionWeatherTest.verifyOvercastWeather(weather);
                overcastWeather = true;
            }
        }

        assert(clearWeather);
        assert(lightWeather);
        assert(averageWeather);
        assert(heavyWeather);
        assert(overcastWeather);
    }

    @Test
    public void testClearWeather() throws PWCGException
    {
        Mockito.when(configManager.getIntConfigParam(ConfigItemKeys.UseRealisticWeatherKey)).thenReturn(0);

        boolean clearWeather = false;
        boolean lightWeather = false;
        boolean averageWeather = false;
        boolean heavyWeather = false;
        boolean overcastWeather = false;

        for (int i = 0; i < 50; ++i)
        {
            int missionTimeHours = 10;
            MissionWeather weather = new MissionWeather(campaign, missionTimeHours);
            weather.createMissionWeather();
            
            if (weather.getCloudConfig().toLowerCase().contains("clear"))
            {
                MissionWeatherTest.verifyClearWeather(weather);
                clearWeather = true;
            }
            else if (weather.getCloudConfig().toLowerCase().contains("light"))
            {
                MissionWeatherTest.verifyLightWeather(weather);
                lightWeather = true;
            }
            else if (weather.getCloudConfig().toLowerCase().contains("medium"))
            {
                MissionWeatherTest.verifyMediumWeather(weather);
                averageWeather = true;
            }
            else if (weather.getCloudConfig().toLowerCase().contains("heavy"))
            {
                heavyWeather = true;
            }
            else if (weather.getCloudConfig().toLowerCase().contains("overcast"))
            {
                overcastWeather = true;
            }
        }

        assert(clearWeather);
        assert(lightWeather);
        assert(averageWeather);
        assert(!heavyWeather);
        assert(!overcastWeather);
    }

    private static void verifyClearWeather(MissionWeather weather)
    {
        assert(weather.getCloudLevel() >= 2500);
        assert(weather.getCloudLevel() <= 7500);
        assert(weather.getCloudHeight() >= 400);
        assert(weather.getCloudHeight() <= 700);
        assert(weather.getTurbulence() == 1);
        assert(weather.getPrecLevel() == 0);
        assert(weather.getPrecType() == 0);
        assert(weather.getWeatherDescription().toLowerCase().contains("clear"));
    }
    
    private static void verifyLightWeather(MissionWeather weather)
    {
        assert(weather.getCloudLevel() >= 2000);
        assert(weather.getCloudLevel() <= 6000);
        assert(weather.getCloudHeight() >= 300);
        assert(weather.getCloudHeight() <= 600);
        assert(weather.getTurbulence() >= 1);
        assert(weather.getTurbulence() <= 2);
        assert(weather.getPrecLevel() == 0);
        assert(weather.getPrecType() == 0);
        assert(weather.getWeatherDescription().toLowerCase().contains("light"));
    }
    
    private static void verifyMediumWeather(MissionWeather weather)
    {
        assert(weather.getCloudLevel() >= 1500);
        assert(weather.getCloudLevel() <= 5500);
        assert(weather.getCloudHeight() >= 500);
        assert(weather.getCloudHeight() <= 900);
        assert(weather.getTurbulence() >= 1);
        assert(weather.getTurbulence() <= 2);
        assert(weather.getPrecLevel() == 0);
        assert(weather.getPrecType() == 0);
        assert(weather.getWeatherDescription().toLowerCase().contains("medium"));
    }
    
    private static void verifyHeavyWeather(MissionWeather weather)
    {
        assert(weather.getCloudLevel() >= 1000);
        assert(weather.getCloudLevel() <= 4000);
        assert(weather.getCloudHeight() >= 600);
        assert(weather.getCloudHeight() <= 1200);
        assert(weather.getTurbulence() >= 1);
        assert(weather.getTurbulence() <= 3);
        assert(weather.getPrecLevel() == 0);
        assert(weather.getPrecType() == 0);
        assert(weather.getWeatherDescription().toLowerCase().contains("heavy"));
    }
    
    private static void verifyOvercastWeather(MissionWeather weather)
    {
        assert(weather.getCloudLevel() >= 1000);
        assert(weather.getCloudLevel() <= 2000);
        assert(weather.getCloudHeight() >= 600);
        assert(weather.getCloudHeight() <= 1600);
        assert(weather.getTurbulence() >= 1);
        assert(weather.getTurbulence() <= 3);
        assert(weather.getPrecLevel() >= 1);
        assert(weather.getPrecLevel() <= 9);
        assert(weather.getPrecType() == 1);
        assert(weather.getWeatherDescription().toLowerCase().contains("overcast"));
    }
}
