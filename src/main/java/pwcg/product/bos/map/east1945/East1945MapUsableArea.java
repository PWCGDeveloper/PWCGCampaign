package pwcg.product.bos.map.east1945;

import pwcg.campaign.context.MapArea;

public class East1945MapUsableArea extends MapArea
{
    static private double EAST1945_USABLE_XMIN = 20000;
    static private double EAST1945_USABLE_XMAX = 364000;

    static private double EAST1945_USABLE_ZMIN = 20000;
    static private double EAST1945_USABLE_ZMAX = 440800;
    
    public East1945MapUsableArea ()
    {
        xMin = EAST1945_USABLE_XMIN;
        xMax = EAST1945_USABLE_XMAX;
        zMin = EAST1945_USABLE_ZMIN;
        zMax = EAST1945_USABLE_ZMAX;
    }
}
