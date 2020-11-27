package pwcg.mission.options;

import java.util.Date;

import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;


public class SeasonStringBuilder 
{
    public static String getClearSkys(Date date) throws PWCGException
    {
        String skys = addCloudPattern(date, "00_clear_");
        return skys;
    }

    public static String getLightSkys(Date date) throws PWCGException
    {
        String skys = addCloudPattern(date, "01_Light_");
        return skys;
    }

    public static String getAverageSkys(Date date) throws PWCGException
    {
        String skys = addCloudPattern(date, "03_Heavy_");
        return skys;
    }

    public static String getHeavySkys(Date date) throws PWCGException
    {
        String skys = addCloudPattern(date, "02_Medium_");
        return skys;
    }

    public static String getOvercastSkys(Date date) throws PWCGException
    {
        String skys = addCloudPattern(date, "04_Overcast_");
        return skys;
    }

    private static String addCloudPattern(Date date, String skys) throws PWCGException
    {
        MapSeasonalParameters mapSeasonalParameters = PWCGContext.getInstance().getCurrentMap().getMapSeason().getSeasonBasedParameters(date);
        String seasonString = mapSeasonalParameters.getSeason();
        String weather = seasonString + "\\" + skys;
        
        int cloudPattern = determineCloudPattern();
        
        weather += "0" + cloudPattern + "\\sky.ini";
        return weather;
    }

    private static int determineCloudPattern()
    {
        int cloudPattern = 0;
        
        // A chance for the overcast layer
        int overcastRoll = RandomNumberGenerator.getRandom(100);
        if (overcastRoll < 10)
        {
            cloudPattern = RandomNumberGenerator.getRandom(10);
        }
        return cloudPattern;
    }
}
