package pwcg.mission.options;

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
public class MissionWeatherHazeTest
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
        Mockito.when(configManager.getIntConfigParam(ConfigItemKeys.MinHazeKey)).thenReturn(2);
        Mockito.when(configManager.getIntConfigParam(ConfigItemKeys.MaxHazeKey)).thenReturn(8);
    }

    @Test
    public void testHazeClearSkies() throws PWCGException
    {
        Mockito.when(missionWeather.getCloudConfig()).thenReturn("00_Clear_");
        double haze = MissionWeatherHaze.createHaze(campaign, missionWeather);
        assert (haze >= 0.2);
        assert (haze <= 0.4);
    }

    @Test
    public void testHazeLightSkies() throws PWCGException
    {
        Mockito.when(missionWeather.getCloudConfig()).thenReturn("01_Light_");
        double haze = MissionWeatherHaze.createHaze(campaign, missionWeather);
        assert (haze >= 0.2);
        assert (haze <= 0.6);
    }

    @Test
    public void testHazeAverageSkies() throws PWCGException
    {
        Mockito.when(missionWeather.getCloudConfig()).thenReturn("01_Medium_");
        double haze = MissionWeatherHaze.createHaze(campaign, missionWeather);
        assert (haze >= 0.3);
        assert (haze <= 0.8);
    }

    @Test
    public void testHazeHeavySkies() throws PWCGException
    {
        Mockito.when(missionWeather.getCloudConfig()).thenReturn("01_Heavy_");
        double haze = MissionWeatherHaze.createHaze(campaign, missionWeather);
        assert (haze >= 0.5);
        assert (haze <= 0.8);
    }

    @Test
    public void testHazeOvercastSkies() throws PWCGException
    {
        Mockito.when(missionWeather.getCloudConfig()).thenReturn("01_Overcast_");
        double haze = MissionWeatherHaze.createHaze(campaign, missionWeather);
        assert (haze >= 0.6);
        assert (haze <= 0.8);
    }

    @Test
    public void testHazeBadConfigMax() throws PWCGException
    {
        Mockito.when(configManager.getIntConfigParam(ConfigItemKeys.MinHazeKey)).thenReturn(11);
        Mockito.when(configManager.getIntConfigParam(ConfigItemKeys.MaxHazeKey)).thenReturn(11);

        Mockito.when(missionWeather.getCloudConfig()).thenReturn("01_Overcast_");
        double haze = MissionWeatherHaze.createHaze(campaign, missionWeather);
        assert (haze == 1.0);
    }

}
