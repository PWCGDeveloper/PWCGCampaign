package pwcg.core.config;

import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class ConfigEditResolver
{
    public ConfigSet resolveUserEdits(ConfigSet defaultConfigSet, ConfigSet editedConfigSet)
    {
        ConfigSet userModifiedConfigSet = new ConfigSet();
        userModifiedConfigSet.setConfigSetName(editedConfigSet.getConfigSetName());
        for (String parameterKey : defaultConfigSet.getConfigItemNames())
        {
            ConfigItem editedValue = editedConfigSet.getConfigItem(parameterKey);
            ConfigItem defaultValue = defaultConfigSet.getConfigItem(parameterKey);
            if (editedValue != null)
            {
                if (defaultValue  != null)
                {
                    if (!editedValue.getValue().equals(defaultValue.getValue()))
                    {
                        userModifiedConfigSet.addConfigItem(parameterKey, editedValue);
                    }
                }
            }
            else
            {
                PWCGLogger.log(LogLevel.ERROR, "Edited value is no longer in use: " + parameterKey);
            }
        }
        
        return userModifiedConfigSet;
    }

}
