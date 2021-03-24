package pwcg.campaign.context;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class StalingradMapResolver
{

    public static FrontMapIdentifier resolveStalingradMap(Campaign campaign, FrontMapIdentifier mapIdentifier) throws PWCGException
    {
        if (campaign == null)
        {
            return mapIdentifier;
        }
        
        if (mapIdentifier == FrontMapIdentifier.STALINGRAD_MAP || mapIdentifier == FrontMapIdentifier.EAST1944_MAP)
        {
            if (DateUtils.isDateInRange(campaign.getDate(), DateUtils.getDateYYYYMMDD("19440101"), DateUtils.getDateYYYYMMDD("19450601")))
            {
                mapIdentifier = FrontMapIdentifier.EAST1944_MAP;
            }
            else
            {
                mapIdentifier = FrontMapIdentifier.STALINGRAD_MAP;
            }
        }
        
        return mapIdentifier;
    }

}
