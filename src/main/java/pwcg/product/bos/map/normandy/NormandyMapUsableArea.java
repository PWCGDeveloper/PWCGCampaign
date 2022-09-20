package pwcg.product.bos.map.normandy;

import pwcg.campaign.context.MapArea;

public class NormandyMapUsableArea extends MapArea
{
    static private double NORMANDY_USABLE_XMIN = 30000;
    static private double NORMANDY_USABLE_XMAX = 341000;

    static private double NORMANDY_USABLE_ZMIN = 25000;
    static private double NORMANDY_USABLE_ZMAX = 371000;
    
    public NormandyMapUsableArea ()
    {
        xMin = NORMANDY_USABLE_XMIN;
        xMax = NORMANDY_USABLE_XMAX;
        zMin = NORMANDY_USABLE_ZMIN;
        zMax = NORMANDY_USABLE_ZMAX;
    }
}
