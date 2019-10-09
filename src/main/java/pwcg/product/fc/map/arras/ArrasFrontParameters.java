package pwcg.product.fc.map.arras;

import pwcg.campaign.context.FrontParameters;

public class ArrasFrontParameters extends FrontParameters
{
    static private double STALINGRAD_XMIN = 0;
    static private double STALINGRAD_XMAX = 230400;

    static private double STALINGRAD_ZMIN = 0;
    static private double STALINGRAD_ZMAX = 358400;
    
    public ArrasFrontParameters ()
    {
        xMin = STALINGRAD_XMIN;
        xMax = STALINGRAD_XMAX;
        zMin = STALINGRAD_ZMIN;
        zMax = STALINGRAD_ZMAX;
    }
}
