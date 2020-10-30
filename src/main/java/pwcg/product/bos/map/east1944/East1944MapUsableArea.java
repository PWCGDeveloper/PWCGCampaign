package pwcg.product.bos.map.east1944;

import pwcg.campaign.context.MapArea;

public class East1944MapUsableArea extends MapArea
{
    static private double EAST1944_USABLE_XMIN = 20000;
    static private double EAST1944_USABLE_XMAX = 210400;

    static private double EAST1944_USABLE_ZMIN = 20000;
    static private double EAST1944_USABLE_ZMAX = 338400;

    public East1944MapUsableArea ()
    {
        xMin = EAST1944_USABLE_XMIN;
        xMax = EAST1944_USABLE_XMAX;
        zMin = EAST1944_USABLE_ZMIN;
        zMax = EAST1944_USABLE_ZMAX;
    }
}
