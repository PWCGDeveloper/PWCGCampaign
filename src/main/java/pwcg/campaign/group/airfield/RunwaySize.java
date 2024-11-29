package pwcg.campaign.group.airfield;

import pwcg.campaign.plane.PlaneType.PlaneSize;

public enum RunwaySize
{
    SMALL,
    MEDIUM,
    LARGE;
    
    boolean fitsOnRunaway(RunwaySize runwaySize, PlaneSize planeSize) 
    {
        if (planeSize == PlaneSize.PLANE_SIZE_SMALL) 
        {
            return true;
        }
        else if (planeSize == PlaneSize.PLANE_SIZE_MEDIUM) {
            if (runwaySize != RunwaySize.SMALL) 
            {
                return true;
            }
            else 
            {
                return false;
            }
        }
        else if (planeSize == PlaneSize.PLANE_SIZE_LARGE) {
            if (runwaySize == RunwaySize.LARGE) 
            {
                return true;
            }
            else 
            {
                return false;
            }
        }
        else
        {
            return true;
        }
    }
}
