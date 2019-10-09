package pwcg.product.bos.map.bodenplatte;

import pwcg.campaign.context.FrontParameters;

public class BodenplatteFrontParameters extends FrontParameters
{
    static private double BODENPLATTE_XMIN = 0;
    static private double BODENPLATTE_XMAX = 384000;

    static private double BODENPLATTE_ZMIN = 0;
    static private double BODENPLATTE_ZMAX = 460800;
    
    public BodenplatteFrontParameters ()
    {
        xMin = BODENPLATTE_XMIN;
        xMax = BODENPLATTE_XMAX;
        zMin = BODENPLATTE_ZMIN;
        zMax = BODENPLATTE_ZMAX;
    }
}
