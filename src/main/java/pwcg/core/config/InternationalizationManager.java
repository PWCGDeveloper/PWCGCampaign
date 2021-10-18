package pwcg.core.config;

import pwcg.campaign.io.json.InternationalizationRecordsIO;
import pwcg.core.exception.PWCGException;

public class InternationalizationManager
{
    public static InternationalizationRecords internationalizationRecords = new InternationalizationRecords();
    private static InternationalizationManager instance = new InternationalizationManager();

    public static String getTranslation (String english) throws PWCGException
    {
        english = english.trim();

        String internationalizationFile  = ConfigManagerGlobal.getInstance().getStringConfigParam(ConfigItemKeys.InternationalizationFileKey);
        if (internationalizationFile.isEmpty())
        {
            return english;
        }
        
        initialize(internationalizationFile);
        
        instance.logMissingTranslation(english);
        return instance.getTranslationFromConfiguredInternationalization(english);
    }

    private static void initialize(String internationalizationFile) throws PWCGException
    {
        if (!internationalizationRecords.isInitialized())
        {
            internationalizationRecords = InternationalizationRecordsIO.readJson(internationalizationFile);
        }
    }

    private void logMissingTranslation(String english)
    {
        if (!english.isEmpty())
        {
            if (!internationalizationRecords.hasTranslation(english))
            {
                System.out.println("English=" + english);
            }
        }
    }
    
    private String getTranslationFromConfiguredInternationalization (String english)
    {
        if (internationalizationRecords.hasTranslation(english))
        {
            return internationalizationRecords.getTranslation(english);
        }
        return english;        
    }
}
