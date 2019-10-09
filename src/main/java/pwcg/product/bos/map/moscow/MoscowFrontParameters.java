package pwcg.product.bos.map.moscow;

import pwcg.campaign.context.FrontParameters;

public class MoscowFrontParameters extends FrontParameters
{
    static private double MOSCOW_XMIN = 0;
    static private double MOSCOW_XMAX = 281600;

    static private double MOSCOW_ZMIN = 0;
    static private double MOSCOW_ZMAX = 281600;
    
    public MoscowFrontParameters ()
    {
        xMin = MOSCOW_XMIN;
        xMax = MOSCOW_XMAX;
        zMin = MOSCOW_ZMIN;
        zMax = MOSCOW_ZMAX;
    }
}
