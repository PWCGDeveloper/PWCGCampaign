package pwcg.mission.options;

import java.util.Date;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;


public class SeasonStringBuilder 
{
    public static String getClearSkys(FrontMapIdentifier mapIdentifier, Date date) throws PWCGException
    {
        String skys = addCloudPattern(mapIdentifier, date, "00_clear_");
        return skys;
    }

    public static String getLightSkys(FrontMapIdentifier mapIdentifier, Date date) throws PWCGException
    {
        String skys = addCloudPattern(mapIdentifier, date, "01_Light_");
        return skys;
    }

    public static String getAverageSkys(FrontMapIdentifier mapIdentifier, Date date) throws PWCGException
    {
        String skys = addCloudPattern(mapIdentifier, date, "02_Medium_");
        return skys;
    }

    public static String getHeavySkys(FrontMapIdentifier mapIdentifier, Date date) throws PWCGException
    {
        String skys = addCloudPattern(mapIdentifier, date, "03_Heavy_");
        return skys;
    }

    public static String getOvercastSkys(FrontMapIdentifier mapIdentifier, Date date) throws PWCGException
    {
        String skys = addCloudPattern(mapIdentifier, date, "04_Overcast_");
        return skys;
    }

    private static String addCloudPattern(FrontMapIdentifier mapIdentifier, Date date, String skys) throws PWCGException
    {
        MapSeasonalParameters mapSeasonalParameters = PWCGContext.getInstance().getMap(mapIdentifier).getMapSeason().
                getSeasonBasedParameters(mapIdentifier, date);
        String seasonString = mapSeasonalParameters.getSeason();
        String weather = seasonString + "\\" + skys;
        
        int cloudPattern = determineCirrusCloudDensityAndGroundFog(skys);
        
        weather += "0" + cloudPattern + "\\sky.ini";
        return weather;
    }

    private static int determineCirrusCloudDensityAndGroundFog(String skys)
    {
        int cloudPattern = 0;
        if (skys.toLowerCase().contains("clear") || skys.toLowerCase().contains("light"))
        {
            // Even numbers have no ground fog - use this for light weather
            cloudPattern = RandomNumberGenerator.getRandom(2);
            cloudPattern = cloudPattern * 2;
        }
        else if (skys.toLowerCase().contains("heavy") || skys.toLowerCase().contains("overcast"))
        {
            // Odd numbers have ground fog- use this for heavy weather
            cloudPattern = 5 + RandomNumberGenerator.getRandom(5);
            if ((cloudPattern %2) == 0)
            {
                ++cloudPattern;
            }
        }
        else
        {
            cloudPattern = 2 + RandomNumberGenerator.getRandom(6);
        }
        return cloudPattern;
    }
}
