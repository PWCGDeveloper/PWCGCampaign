package pwcg.core.config;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import pwcg.core.exception.PWCGException;

public class ConfigSet
{
	private String configSetName;
	private TreeMap<String, ConfigItem> configMap = new TreeMap<String, ConfigItem>();

	public String getConfigSetName()
	{
		return configSetName;
	}

	public void setConfigSetName(String configSetName)
	{
		this.configSetName = configSetName;
	}

    public ConfigItem getConfigItem(String parameterKey)
    {
        return configMap.get(parameterKey);
    }

    public boolean hasConfigItem(String parameterKey)
    {
        return (configMap.containsKey(parameterKey));
    }

    public void addConfigItem(String parameterKey, ConfigItem configItem)
    {
        configMap.put(parameterKey, configItem);
    }
    
    public void modifyConfigItemValue(String parameterKey, String value)
    {
        if (hasConfigItem(parameterKey))
        {
            ConfigItem configItem = configMap.get(parameterKey);
            configItem.setValue(value);
        }
    }
    
    public void removeConfigItem(String parameterKey)
    {
        if (configMap.containsKey(parameterKey))
        {
            configMap.remove(parameterKey);
        }
    }
    
    public List<String> getConfigItemNames()
    {
        List<String> configItemNames = new ArrayList<>(configMap.keySet());
        return configItemNames;
    }
    
    public ConfigSet copy()
    {
        TreeMap<String, ConfigItem> configMapCopy = new TreeMap<>();
        for (String parameterKey : configMap.keySet())
        {
            ConfigItem configItem = configMap.get(parameterKey);
            ConfigItem copyConfigItem = configItem.copy();
            configMapCopy.put(parameterKey, copyConfigItem);
        }

        ConfigSet configSetCopy = new ConfigSet();
        configSetCopy.setConfigSetName(configSetName);
        configSetCopy.configMap = configMapCopy;
        
        return configSetCopy;
    }

    public ConfigSet merge(ConfigSet userConfigSet)
    {
        ConfigSet mergedConfigSet = this.copy();
        ConfigSet userCopy = userConfigSet.copy();
        mergedConfigSet.configMap.putAll(userCopy.configMap);
        return mergedConfigSet;
    }    

    public void validate() throws PWCGException
    {
        for (ConfigItem configItem : configMap.values())
        {
            configItem.validate();
        }
    }
}
