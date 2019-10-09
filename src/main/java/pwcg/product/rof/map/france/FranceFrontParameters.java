package pwcg.product.rof.map.france;

import pwcg.campaign.context.FrontParameters;

public class FranceFrontParameters extends FrontParameters
{
    static public double FRANCE_XMIN = 0;
    static public double FRANCE_XMAX = 281600;

    static public double FRANCE_ZMIN = 0;
    static public double FRANCE_ZMAX = 358000;
    
    public FranceFrontParameters ()
    {
        xMin = FRANCE_XMIN;
        xMax = FRANCE_XMAX;
        zMin = FRANCE_ZMIN;
        zMax = FRANCE_ZMAX;
    }
}
