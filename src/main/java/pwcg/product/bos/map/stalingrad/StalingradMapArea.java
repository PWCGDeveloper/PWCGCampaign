package pwcg.product.bos.map.stalingrad;

import pwcg.campaign.context.MapArea;

public class StalingradMapArea extends MapArea
{
    static private double STALINGRAD_XMIN = 0;
    static private double STALINGRAD_XMAX = 230400;

    static private double STALINGRAD_ZMIN = 0;
    static private double STALINGRAD_ZMAX = 358400;
    
    public StalingradMapArea ()
    {
        xMin = STALINGRAD_XMIN;
        xMax = STALINGRAD_XMAX;
        zMin = STALINGRAD_ZMIN;
        zMax = STALINGRAD_ZMAX;
    }
}
