package pwcg.mission.options;

import pwcg.campaign.Campaign;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;

public class WindLayer
{
    private int layer;
    private int direction;
    private int speed;

    public WindLayer(Campaign campaign, int layer, int direction, int speed) throws PWCGException
    {
        this.layer = layer;
        this.direction = direction;
        this.speed = speed;
        
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        int maxWind = configManager.getIntConfigParam(ConfigItemKeys.MaxWindKey);
        if (speed > maxWind)
        {
            this.speed = maxWind;
        }
    }

    public int getLayer()
    {
        return layer;
    }

    public int getDirection()
    {
        return direction;
    }

    public int getSpeed()
    {
        return speed;
    }
}
