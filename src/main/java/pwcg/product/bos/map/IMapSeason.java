package pwcg.product.bos.map;

import java.util.Date;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.core.exception.PWCGException;
import pwcg.mission.options.MapSeasonalParameters;

public interface IMapSeason
{
    MapSeasonalParameters getSeasonBasedParameters(FrontMapIdentifier mapIdentifier, Date date) throws PWCGException;
}