package pwcg.product.bos.map;

import java.util.Date;

import pwcg.core.exception.PWCGException;
import pwcg.mission.options.MapSeasonalParameters;

public interface IMapSeason
{
    MapSeasonalParameters getSeasonBasedParameters(Date date) throws PWCGException;
}