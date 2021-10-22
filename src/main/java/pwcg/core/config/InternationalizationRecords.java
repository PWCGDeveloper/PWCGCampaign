package pwcg.core.config;

import java.util.Map;
import java.util.TreeMap;

public class InternationalizationRecords
{
    public Map<String, String> translations = new TreeMap<>();
    
    public String getTranslation (String english)
    {
        if (translations.containsKey(english))
        {
            return translations.get(english);
        }
        return english;        
    }
    
    public boolean hasTranslation (String english)
    {
        if (translations.containsKey(english))
        {
            return true;
        }
        return false;        
    }
    
    public boolean isInitialized ()
    {
        if (translations.isEmpty())
        {
            return false;
        }
        return true;        
    }
    
    public void add(String key, String value)
    {
        translations.put(key, value);
    }

    public Map<String, String> getTranslations()
    {
        return translations;
    }
}
