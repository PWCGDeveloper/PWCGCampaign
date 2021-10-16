package pwcg.mission.options;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
public class MissionWeatherWindTest
{
    @Mock Campaign campaign;
    @Mock ConfigManagerCampaign configManager;
    @Mock MissionWeather missionWeather;
    
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        PWCGContext.getInstance().setCurrentMap(FrontMapIdentifier.BODENPLATTE_MAP);

        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManager);
        
        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManager);
        Mockito.when(configManager.getIntConfigParam(ConfigItemKeys.MaxWindKey)).thenReturn(20);
        
        Mockito.when(missionWeather.getWindDirection()).thenReturn(90);
        Mockito.when(missionWeather.getTurbulence()).thenReturn(1.0);
    }

    @Test
    public void testWindClearSkies() throws PWCGException
    {
        Mockito.when(missionWeather.getCloudConfig()).thenReturn("00_Clear_");
        List<WindLayer> windLayers = MissionWeatherWind.createWind(campaign, missionWeather);
        Assertions.assertTrue (windLayers.size() == 5);
        
        Assertions.assertTrue (windLayers.get(0).getLayer() == 0);
        Assertions.assertTrue (windLayers.get(0).getDirection() == 90);
        Assertions.assertTrue (windLayers.get(0).getSpeed() >= 1);
        Assertions.assertTrue (windLayers.get(0).getSpeed() <= 3);
        
        Assertions.assertTrue (windLayers.get(1).getLayer() == 500);
        Assertions.assertTrue (windLayers.get(1).getDirection() == 90);
        Assertions.assertTrue (windLayers.get(1).getSpeed() >= 3);
        Assertions.assertTrue (windLayers.get(1).getSpeed() <= 7);
        
        Assertions.assertTrue (windLayers.get(2).getLayer() == 1000);
        Assertions.assertTrue (windLayers.get(2).getDirection() == 90);
        Assertions.assertTrue (windLayers.get(2).getSpeed() >= 3);
        Assertions.assertTrue (windLayers.get(2).getSpeed() <= 8);
        
        Assertions.assertTrue (windLayers.get(3).getLayer() == 3000);
        Assertions.assertTrue (windLayers.get(3).getDirection() == 90);
        Assertions.assertTrue (windLayers.get(3).getSpeed() >= 5);
        Assertions.assertTrue (windLayers.get(3).getSpeed() <= 10);
        
        Assertions.assertTrue (windLayers.get(4).getLayer() == 5000);
        Assertions.assertTrue (windLayers.get(4).getDirection() == 90);
        Assertions.assertTrue (windLayers.get(4).getSpeed() >= 5);
        Assertions.assertTrue (windLayers.get(4).getSpeed() <= 11);
    }

    @Test
    public void testWindLightSkies() throws PWCGException
    {
        Mockito.when(missionWeather.getCloudConfig()).thenReturn("01_Light_");
        List<WindLayer> windLayers = MissionWeatherWind.createWind(campaign, missionWeather);
        
        Assertions.assertTrue (windLayers.get(0).getLayer() == 0);
        Assertions.assertTrue (windLayers.get(0).getDirection() == 90);
        Assertions.assertTrue (windLayers.get(0).getSpeed() >= 2);
        Assertions.assertTrue (windLayers.get(0).getSpeed() <= 4);
        
        Assertions.assertTrue (windLayers.get(1).getLayer() == 500);
        Assertions.assertTrue (windLayers.get(1).getDirection() == 90);
        Assertions.assertTrue (windLayers.get(1).getSpeed() >= 4);
        Assertions.assertTrue (windLayers.get(1).getSpeed() <= 8);
        
        Assertions.assertTrue (windLayers.get(2).getLayer() == 1000);
        Assertions.assertTrue (windLayers.get(2).getDirection() == 90);
        Assertions.assertTrue (windLayers.get(2).getSpeed() >= 4);
        Assertions.assertTrue (windLayers.get(2).getSpeed() <= 9);
        
        Assertions.assertTrue (windLayers.get(3).getLayer() == 3000);
        Assertions.assertTrue (windLayers.get(3).getDirection() == 90);
        Assertions.assertTrue (windLayers.get(3).getSpeed() >= 6);
        Assertions.assertTrue (windLayers.get(3).getSpeed() <= 11);
        
        Assertions.assertTrue (windLayers.get(4).getLayer() == 5000);
        Assertions.assertTrue (windLayers.get(4).getDirection() == 90);
        Assertions.assertTrue (windLayers.get(4).getSpeed() >= 6);
        Assertions.assertTrue (windLayers.get(4).getSpeed() <= 12);
    }

    @Test
    public void testWindMediumSkies() throws PWCGException
    {
        Mockito.when(missionWeather.getCloudConfig()).thenReturn("01_Medium_");
        List<WindLayer> windLayers = MissionWeatherWind.createWind(campaign, missionWeather);
        
        Assertions.assertTrue (windLayers.get(0).getLayer() == 0);
        Assertions.assertTrue (windLayers.get(0).getDirection() == 90);
        Assertions.assertTrue (windLayers.get(0).getSpeed() >= 3);
        Assertions.assertTrue (windLayers.get(0).getSpeed() <= 5);
        
        Assertions.assertTrue (windLayers.get(1).getLayer() == 500);
        Assertions.assertTrue (windLayers.get(1).getDirection() == 90);
        Assertions.assertTrue (windLayers.get(1).getSpeed() >= 5);
        Assertions.assertTrue (windLayers.get(1).getSpeed() <= 9);
        
        Assertions.assertTrue (windLayers.get(2).getLayer() == 1000);
        Assertions.assertTrue (windLayers.get(2).getDirection() == 90);
        Assertions.assertTrue (windLayers.get(2).getSpeed() >= 5);
        Assertions.assertTrue (windLayers.get(2).getSpeed() <= 10);
        
        Assertions.assertTrue (windLayers.get(3).getLayer() == 3000);
        Assertions.assertTrue (windLayers.get(3).getDirection() == 90);
        Assertions.assertTrue (windLayers.get(3).getSpeed() >= 7);
        Assertions.assertTrue (windLayers.get(3).getSpeed() <= 11);
        
        Assertions.assertTrue (windLayers.get(4).getLayer() == 5000);
        Assertions.assertTrue (windLayers.get(4).getDirection() == 90);
        Assertions.assertTrue (windLayers.get(4).getSpeed() >= 7);
        Assertions.assertTrue (windLayers.get(4).getSpeed() <= 13);
    }

    @Test
    public void testWindHeavySkies() throws PWCGException
    {
        Mockito.when(missionWeather.getCloudConfig()).thenReturn("01_Heavy_");
        List<WindLayer> windLayers = MissionWeatherWind.createWind(campaign, missionWeather);
        
        Assertions.assertTrue (windLayers.get(0).getLayer() == 0);
        Assertions.assertTrue (windLayers.get(0).getDirection() == 90);
        Assertions.assertTrue (windLayers.get(0).getSpeed() >= 5);
        Assertions.assertTrue (windLayers.get(0).getSpeed() <= 7);
        
        Assertions.assertTrue (windLayers.get(1).getLayer() == 500);
        Assertions.assertTrue (windLayers.get(1).getDirection() == 90);
        Assertions.assertTrue (windLayers.get(1).getSpeed() >= 6);
        Assertions.assertTrue (windLayers.get(1).getSpeed() <= 11);
        
        Assertions.assertTrue (windLayers.get(2).getLayer() == 1000);
        Assertions.assertTrue (windLayers.get(2).getDirection() == 90);
        Assertions.assertTrue (windLayers.get(2).getSpeed() >= 7);
        Assertions.assertTrue (windLayers.get(2).getSpeed() <= 11);
        
        Assertions.assertTrue (windLayers.get(3).getLayer() == 3000);
        Assertions.assertTrue (windLayers.get(3).getDirection() == 90);
        Assertions.assertTrue (windLayers.get(3).getSpeed() >= 8);
        Assertions.assertTrue (windLayers.get(3).getSpeed() <= 13);
        
        Assertions.assertTrue (windLayers.get(4).getLayer() == 5000);
        Assertions.assertTrue (windLayers.get(4).getDirection() == 90);
        Assertions.assertTrue (windLayers.get(4).getSpeed() >= 9);
        Assertions.assertTrue (windLayers.get(4).getSpeed() <= 15);
    }

    @Test
    public void testWindOvercastSkies() throws PWCGException
    {
        Mockito.when(missionWeather.getCloudConfig()).thenReturn("01_Overcast_");
        List<WindLayer> windLayers = MissionWeatherWind.createWind(campaign, missionWeather);
        
        Assertions.assertTrue (windLayers.get(0).getLayer() == 0);
        Assertions.assertTrue (windLayers.get(0).getDirection() == 90);
        Assertions.assertTrue (windLayers.get(0).getSpeed() >= 5);
        Assertions.assertTrue (windLayers.get(0).getSpeed() <= 7);
        
        Assertions.assertTrue (windLayers.get(1).getLayer() == 500);
        Assertions.assertTrue (windLayers.get(1).getDirection() == 90);
        Assertions.assertTrue (windLayers.get(1).getSpeed() >= 6);
        Assertions.assertTrue (windLayers.get(1).getSpeed() <= 11);
        
        Assertions.assertTrue (windLayers.get(2).getLayer() == 1000);
        Assertions.assertTrue (windLayers.get(2).getDirection() == 90);
        Assertions.assertTrue (windLayers.get(2).getSpeed() >= 7);
        Assertions.assertTrue (windLayers.get(2).getSpeed() <= 11);
        
        Assertions.assertTrue (windLayers.get(3).getLayer() == 3000);
        Assertions.assertTrue (windLayers.get(3).getDirection() == 90);
        Assertions.assertTrue (windLayers.get(3).getSpeed() >= 8);
        Assertions.assertTrue (windLayers.get(3).getSpeed() <= 13);
        
        Assertions.assertTrue (windLayers.get(4).getLayer() == 5000);
        Assertions.assertTrue (windLayers.get(4).getDirection() == 90);
        Assertions.assertTrue (windLayers.get(4).getSpeed() >= 9);
        Assertions.assertTrue (windLayers.get(4).getSpeed() <= 15);
    }

    @Test
    public void reduceWondForConfig() throws PWCGException
    {
        Mockito.when(configManager.getIntConfigParam(ConfigItemKeys.MaxWindKey)).thenReturn(3);

        Mockito.when(missionWeather.getCloudConfig()).thenReturn("01_Overcast_");
        List<WindLayer> windLayers = MissionWeatherWind.createWind(campaign, missionWeather);
        
        Assertions.assertTrue (windLayers.get(0).getLayer() == 0);
        Assertions.assertTrue (windLayers.get(0).getDirection() == 90);
        Assertions.assertTrue (windLayers.get(0).getSpeed() == 3);
        
        Assertions.assertTrue (windLayers.get(1).getLayer() == 500);
        Assertions.assertTrue (windLayers.get(1).getDirection() == 90);
        Assertions.assertTrue (windLayers.get(0).getSpeed() == 3);
        
        Assertions.assertTrue (windLayers.get(2).getLayer() == 1000);
        Assertions.assertTrue (windLayers.get(2).getDirection() == 90);
        Assertions.assertTrue (windLayers.get(0).getSpeed() == 3);
        
        Assertions.assertTrue (windLayers.get(3).getLayer() == 3000);
        Assertions.assertTrue (windLayers.get(3).getDirection() == 90);
        Assertions.assertTrue (windLayers.get(0).getSpeed() == 3);
        
        Assertions.assertTrue (windLayers.get(4).getLayer() == 5000);
        Assertions.assertTrue (windLayers.get(4).getDirection() == 90);
        Assertions.assertTrue (windLayers.get(0).getSpeed() == 3);
    }

}
