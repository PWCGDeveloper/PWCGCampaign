package pwcg.product.bos.map.kuban;

import pwcg.campaign.context.MapArea;

public class KubanMapArea extends MapArea
{
    static private double KUBAN_XMIN = 0;
    static private double KUBAN_XMAX = 358400;

    static private double KUBAN_ZMIN = 0;
    static private double KUBAN_ZMAX = 460800;
    
    public KubanMapArea ()
    {
        xMin = KUBAN_XMIN;
        xMax = KUBAN_XMAX;
        zMin = KUBAN_ZMIN;
        zMax = KUBAN_ZMAX;
    }
    
}
