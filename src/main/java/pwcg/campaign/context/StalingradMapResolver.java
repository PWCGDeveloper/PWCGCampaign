package pwcg.campaign.context;

import java.util.Date;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class StalingradMapResolver
{

    public static FrontMapIdentifier resolveStalingradMap(Date date, FrontMapIdentifier mapIdentifier) throws PWCGException
    {
        if (mapIdentifier == FrontMapIdentifier.STALINGRAD_MAP || mapIdentifier == FrontMapIdentifier.EAST1944_MAP)
        {
            if (DateUtils.isDateInRange(date, DateUtils.getDateYYYYMMDD("19440101"), DateUtils.getDateYYYYMMDD("19450601")))
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
