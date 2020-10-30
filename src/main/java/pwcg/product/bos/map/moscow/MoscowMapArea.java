package pwcg.product.bos.map.moscow;

import pwcg.campaign.context.MapArea;

public class MoscowMapArea extends MapArea
{
    static private double MOSCOW_XMIN = 0;
    static private double MOSCOW_XMAX = 281600;

    static private double MOSCOW_ZMIN = 0;
    static private double MOSCOW_ZMAX = 281600;
    
    public MoscowMapArea ()
    {
        xMin = MOSCOW_XMIN;
        xMax = MOSCOW_XMAX;
        zMin = MOSCOW_ZMIN;
        zMax = MOSCOW_ZMAX;
    }
}
