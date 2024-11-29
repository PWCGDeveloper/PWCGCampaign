package pwcg.product.fc.map.westernfront;

import pwcg.campaign.context.MapArea;

public class WesternFrontMapUsableArea extends MapArea
{
    static private double WESTERN_FRONT_USABLE_XMIN = 140800;
    static private double WESTERN_FRONT_USABLE_XMAX = 495000;

    static private double WESTERN_FRONT_USABLE_ZMIN = 140000;
    static private double WESTERN_FRONT_USABLE_ZMAX = 570000;
    
    public WesternFrontMapUsableArea ()
    {
        xMin = WESTERN_FRONT_USABLE_XMIN;
        xMax = WESTERN_FRONT_USABLE_XMAX;
        zMin = WESTERN_FRONT_USABLE_ZMIN;
        zMax = WESTERN_FRONT_USABLE_ZMAX;
    }
}
