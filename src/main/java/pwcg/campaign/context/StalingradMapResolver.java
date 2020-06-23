package pwcg.campaign.context;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
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
            if (campaign.getDate().before(DateUtils.getDateYYYYMMDD("19440101")))
            {
                mapIdentifier = FrontMapIdentifier.STALINGRAD_MAP;
            }
            else
            {
                mapIdentifier = FrontMapIdentifier.EAST1944_MAP;
            }
        }
        
        return mapIdentifier;
    }

}
