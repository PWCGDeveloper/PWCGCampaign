package pwcg.product.fc.map.westernfront;

import pwcg.campaign.context.MapArea;

public class WesternFrontMapArea extends MapArea
{
    static private double WESTERN_FRONT_XMIN = 0;
    static private double WESTERN_FRONT_XMAX = 345600;

    static private double WESTERN_FRONT_ZMIN = 0;
    static private double WESTERN_FRONT_ZMAX = 422400;
    
    public WesternFrontMapArea ()
    {
        xMin = WESTERN_FRONT_XMIN;
        xMax = WESTERN_FRONT_XMAX;
        zMin = WESTERN_FRONT_ZMIN;
        zMax = WESTERN_FRONT_ZMAX;
    }
}
