package pwcg.product.bos.map.east1945;

import pwcg.campaign.context.MapArea;

public class East1945MapArea extends MapArea
{
    static private double EAST1945_XMIN = 0;
    static private double EAST1945_XMAX = 384000;

    static private double EAST1945_ZMIN = 0;
    static private double EAST1945_ZMAX = 460800;
    
    public East1945MapArea ()
    {
        xMin = EAST1945_XMIN;
        xMax = EAST1945_XMAX;
        zMin = EAST1945_ZMIN;
        zMax = EAST1945_ZMAX;
    }
}
