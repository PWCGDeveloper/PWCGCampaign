package pwcg.product.bos.map.bodenplatte;

import pwcg.campaign.context.MapArea;

public class BodenplatteMapUsableArea extends MapArea
{
    static private double BODENPLATTE_USABLE_XMIN = 20000;
    static private double BODENPLATTE_USABLE_XMAX = 364000;

    static private double BODENPLATTE_USABLE_ZMIN = 20000;
    static private double BODENPLATTE_USABLE_ZMAX = 440800;
    
    public BodenplatteMapUsableArea ()
    {
        xMin = BODENPLATTE_USABLE_XMIN;
        xMax = BODENPLATTE_USABLE_XMAX;
        zMin = BODENPLATTE_USABLE_ZMIN;
        zMax = BODENPLATTE_USABLE_ZMAX;
    }
}
