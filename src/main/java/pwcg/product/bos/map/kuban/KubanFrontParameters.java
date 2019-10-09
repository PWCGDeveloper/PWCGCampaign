package pwcg.product.bos.map.kuban;

import pwcg.campaign.context.FrontParameters;

public class KubanFrontParameters extends FrontParameters
{
    static private double KUBAN_XMIN = 0;
    static private double KUBAN_XMAX = 358400;

    static private double KUBAN_ZMIN = 0;
    static private double KUBAN_ZMAX = 460800;
    
    public KubanFrontParameters ()
    {
        xMin = KUBAN_XMIN;
        xMax = KUBAN_XMAX;
        zMin = KUBAN_ZMIN;
        zMax = KUBAN_ZMAX;
    }
    
}
