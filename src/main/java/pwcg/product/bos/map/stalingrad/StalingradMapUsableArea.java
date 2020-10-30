package pwcg.product.bos.map.stalingrad;

import pwcg.campaign.context.MapArea;

public class StalingradMapUsableArea extends MapArea
{
    static private double STALINGRAD_USABLE_XMIN = 20000;
    static private double STALINGRAD_USABLE_XMAX = 210400;

    static private double STALINGRAD_USABLE_ZMIN = 20000;
    static private double STALINGRAD_USABLE_ZMAX = 338400;

    
    public StalingradMapUsableArea ()
    {
        xMin = STALINGRAD_USABLE_XMIN;
        xMax = STALINGRAD_USABLE_XMAX;
        zMin = STALINGRAD_USABLE_ZMIN;
        zMax = STALINGRAD_USABLE_ZMAX;
    }
}
