package pwcg.product.bos.map;

import java.util.Date;

import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.mission.options.MapSeasonalParameters;
import pwcg.mission.options.MapSeasonalParameters.Season;

public abstract class MapSeasonBase
{
    protected MapSeasonalParameters winter = new MapSeasonalParameters();
    protected MapSeasonalParameters spring = new MapSeasonalParameters();
    protected MapSeasonalParameters summer = new MapSeasonalParameters();
    protected MapSeasonalParameters autumn = new MapSeasonalParameters();

    protected abstract void makeWinter();
    protected abstract void makeSpring();
    protected abstract void makeSummer();
    protected abstract void makeAutumn();
    
    public MapSeasonBase()
    {
        makeWinter();
        makeSpring();
        makeSummer();
        makeAutumn();
    }

    public MapSeasonalParameters getSeasonBasedParameters(Date date) throws PWCGException
    {
        Season season = PWCGContext.getInstance().getCurrentMap().getMapClimate().getSeason(date);
        if (season == Season.WINTER)
        {
            return winter;
        }
        if (season == Season.SPRING)
        {
            return spring;
        }
        if (season == Season.SUMMER)
        {
            return summer;
        }
        if (season == Season.AUTUMN)
        {
            return autumn;
        }
        
        throw new PWCGException("Badly defined season: " + season);
    }


}
