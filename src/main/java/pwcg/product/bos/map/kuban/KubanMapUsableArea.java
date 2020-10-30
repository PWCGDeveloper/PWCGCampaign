package pwcg.product.bos.map.kuban;

import pwcg.campaign.context.MapArea;

public class KubanMapUsableArea extends MapArea
{
    static private double KUBAN_USABLE_XMIN = 20000;
    static private double KUBAN_USABLE_XMAX = 338400;

    static private double KUBAN_USABLE_ZMIN = 20000;
    static private double KUBAN_USABLE_ZMAX = 440800;
    
    public KubanMapUsableArea ()
    {
        xMin = KUBAN_USABLE_XMIN;
        xMax = KUBAN_USABLE_XMAX;
        zMin = KUBAN_USABLE_ZMIN;
        zMax = KUBAN_USABLE_ZMAX;
    }
    
}
