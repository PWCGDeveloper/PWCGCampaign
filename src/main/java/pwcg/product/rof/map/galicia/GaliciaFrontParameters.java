package pwcg.product.rof.map.galicia;

import pwcg.campaign.context.FrontParameters;

public class GaliciaFrontParameters extends FrontParameters
{
    static private double GALICIA_XMIN = 1000;
    static private double GALICIA_XMAX = 179000;

    static private double GALICIA_ZMIN = 1000;
    static private double GALICIA_ZMAX = 179000;
    
    public GaliciaFrontParameters ()
    {
        xMin = GALICIA_XMIN;
        xMax = GALICIA_XMAX;
        zMin = GALICIA_ZMIN;
        zMax = GALICIA_ZMAX;
    }
}
