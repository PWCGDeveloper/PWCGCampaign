package pwcg.mission.options;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class WindLayerTest
{
    @Mock Campaign campaign;
    @Mock ConfigManagerCampaign configManager;
    
    @Before 
    public void setup() throws PWCGException
    {
        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManager);
        Mockito.when(configManager.getIntConfigParam(ConfigItemKeys.MaxWindKey)).thenReturn(7);
    }

    @Test
    public void testAcceptedWindLayer() throws PWCGException
    {
        WindLayer windLayer = new WindLayer(campaign, 1000, 90, 2);        
        assert(windLayer.getLayer() == 1000);
        assert(windLayer.getDirection() == 90);
        assert(windLayer.getSpeed() == 2);
    }

    @Test
    public void testBadSpeedWindLayer() throws PWCGException
    {
        WindLayer windLayer = new WindLayer(campaign, 1000, 90, 10);        
        assert(windLayer.getLayer() == 1000);
        assert(windLayer.getDirection() == 90);
        assert(windLayer.getSpeed() == 7);
    }

}
