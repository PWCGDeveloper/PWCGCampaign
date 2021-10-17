package pwcg.core.config;

import java.util.HashMap;
import java.util.Map;

public class InternationalizationManager
{
    public static Map<String, String> translations = new HashMap<>();
    private static InternationalizationManager instance = new InternationalizationManager();

    public static String getTranslation (String english)
    {
        return instance.getTranslationFromConfiguredInternationalization(english);
    }
    
    private String getTranslationFromConfiguredInternationalization (String english)
    {
        return english;        
    }
}
