package pwcg.product.bos.map.moscow;

import pwcg.campaign.context.MapArea;

public class MoscowMapUsableArea extends MapArea
{
    static private double MOSCOW_USABLE_XMIN = 20000;
    static private double MOSCOW_USABLE_XMAX = 261600;

    static private double MOSCOW_USABLE_ZMIN = 20000;
    static private double MOSCOW_USABLE_ZMAX = 221600;
    
    public MoscowMapUsableArea ()
    {
        xMin = MOSCOW_USABLE_XMIN;
        xMax = MOSCOW_USABLE_XMAX;
        zMin = MOSCOW_USABLE_ZMIN;
        zMax = MOSCOW_USABLE_ZMAX;
    }
}
