package pwcg.product.bos.map;

import java.util.Date;

import pwcg.mission.options.MapSeasonalParameters.Season;

public interface IMapClimate 
{    
    Season getSeason(Date date);
    int getTemperature(Date date);
}
