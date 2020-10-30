package pwcg.product.fc.map.arras;

import pwcg.campaign.context.MapArea;

public class ArrasMapUsableArea extends MapArea
{
    static private double ARRAS_USABLE_XMIN = 15000;
    static private double ARRAS_USABLE_XMAX = 151400;

    static private double ARRAS_USABLE_ZMIN = 15000;
    static private double ARRAS_USABLE_ZMAX = 151400;
    
    public ArrasMapUsableArea ()
    {
        xMin = ARRAS_USABLE_XMIN;
        xMax = ARRAS_USABLE_XMAX;
        zMin = ARRAS_USABLE_ZMIN;
        zMax = ARRAS_USABLE_ZMAX;
    }
}
