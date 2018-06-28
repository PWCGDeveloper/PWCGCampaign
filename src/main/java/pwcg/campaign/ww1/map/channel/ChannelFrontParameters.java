package pwcg.campaign.ww1.map.channel;

import pwcg.campaign.context.FrontParameters;

public class ChannelFrontParameters extends FrontParameters
{
    static private double CHANNEL_XMIN = 40000;
    static private double CHANNEL_XMAX = 230400;

    static private double CHANNEL_ZMIN = 40000;
    static private double CHANNEL_ZMAX = 281600;
    
    public ChannelFrontParameters ()
    {
        xMin = CHANNEL_XMIN;
        xMax = CHANNEL_XMAX;
        zMin = CHANNEL_ZMIN;
        zMax = CHANNEL_ZMAX;
    }
}
