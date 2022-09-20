package pwcg.product.bos.map.normandy;

import pwcg.campaign.context.MapArea;

public class NormandyMapArea extends MapArea
{
    static private double BODENPLATTE_XMIN = 0;
    static private double BODENPLATTE_XMAX = 396800;

    static private double BODENPLATTE_ZMIN = 0;
    static private double BODENPLATTE_ZMAX = 396800;
    
    public NormandyMapArea ()
    {
        xMin = BODENPLATTE_XMIN;
        xMax = BODENPLATTE_XMAX;
        zMin = BODENPLATTE_ZMIN;
        zMax = BODENPLATTE_ZMAX;
    }
}
