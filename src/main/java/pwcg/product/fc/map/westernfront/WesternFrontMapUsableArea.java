package pwcg.product.fc.map.westernfront;

import pwcg.campaign.context.MapArea;

public class WesternFrontMapUsableArea extends MapArea
{
    static private double WESTERN_FRONT_USABLE_XMIN = 32000;
    static private double WESTERN_FRONT_USABLE_XMAX = 313600;

    static private double WESTERN_FRONT_USABLE_ZMIN = 32000;
    static private double WESTERN_FRONT_USABLE_ZMAX = 390000;
    
    public WesternFrontMapUsableArea ()
    {
        xMin = WESTERN_FRONT_USABLE_XMIN;
        xMax = WESTERN_FRONT_USABLE_XMAX;
        zMin = WESTERN_FRONT_USABLE_ZMIN;
        zMax = WESTERN_FRONT_USABLE_ZMAX;
    }
}
