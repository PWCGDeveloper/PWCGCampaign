package pwcg.product.fc.map.arras;

import pwcg.campaign.context.MapArea;

public class ArrasMapArea extends MapArea
{
    static private double ARRAS_XMIN = 0;
    static private double ARRAS_XMAX = 166400;

    static private double ARRAS_ZMIN = 0;
    static private double ARRAS_ZMAX = 166400;
    
    public ArrasMapArea ()
    {
        xMin = ARRAS_XMIN;
        xMax = ARRAS_XMAX;
        zMin = ARRAS_ZMIN;
        zMax = ARRAS_ZMAX;
    }
}
