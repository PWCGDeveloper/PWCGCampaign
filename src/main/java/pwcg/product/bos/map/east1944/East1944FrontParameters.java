package pwcg.product.bos.map.east1944;

import pwcg.campaign.context.FrontParameters;

public class East1944FrontParameters extends FrontParameters
{
    static private double EAST1944_XMIN = 0;
    static private double EAST1944_XMAX = 230400;

    static private double EAST1944_ZMIN = 0;
    static private double EAST1944_ZMAX = 358400;
    
    public East1944FrontParameters ()
    {
        xMin = EAST1944_XMIN;
        xMax = EAST1944_XMAX;
        zMin = EAST1944_ZMIN;
        zMax = EAST1944_ZMAX;
    }
}
